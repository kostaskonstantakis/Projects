import librosa
import numpy as np
from scipy import signal
from pathlib import Path
import json
from datetime import datetime
import torch
from .source_separator import SourceSeparator

class AudioProcessor:
    """Handle audio processing: source separation and 8-bit conversion"""
    
    def __init__(self):
        self.separator = SourceSeparator()
        self.target_sr = 22050  # 22kHz for 8-bit retro sound
        
    def convert_to_8bit(self, input_file: str, job_id: str):
        """Convert audio to 8-bit with source separation"""
        output_dir = Path("outputs") / job_id
        output_dir.mkdir(parents=True, exist_ok=True)
        
        try:
            # Update status
            self._update_status(output_dir, "loading", "Loading audio file...")
            
            # Load audio
            y, sr = librosa.load(input_file, sr=None)
            
            # Separate sources using DEMUCS
            self._update_status(output_dir, "separating", "Separating audio sources...")
            sources = self.separator.separate(y, sr)
            
            # Convert each source to 8-bit
            self._update_status(output_dir, "converting", "Converting to 8-bit...")
            converted_sources = {}
            for source_name, audio in sources.items():
                converted_sources[source_name] = self._convert_source_to_8bit(audio, sr)
            
            # Mix converted sources back together
            self._update_status(output_dir, "mixing", "Mixing sources...")
            output_audio = self._mix_sources(converted_sources, sr)
            
            # Save output
            self._update_status(output_dir, "saving", "Saving audio...")
            output_path = output_dir / "output.wav"
            librosa.output.write_wav(str(output_path), output_audio, sr=self.target_sr)
            
            # Create preview (first 30 seconds)
            preview_path = output_dir / "preview.wav"
            preview_samples = int(self.target_sr * 30)
            librosa.output.write_wav(str(preview_path), output_audio[:preview_samples], sr=self.target_sr)
            
            # Final status
            self._update_status(output_dir, "completed", "Conversion completed!")
            
        except Exception as e:
            self._update_status(output_dir, "error", f"Error: {str(e)}")
            raise
    
    def _convert_source_to_8bit(self, audio: np.ndarray, sr: int) -> np.ndarray:
        """Convert single audio source to 8-bit with intelligent dithering"""
        
        # Resample to lower bit depth friendly rate
        audio_resampled = librosa.resample(audio, orig_sr=sr, target_sr=self.target_sr)
        
        # Normalize to -1 to 1
        if np.max(np.abs(audio_resampled)) > 0:
            audio_resampled = audio_resampled / np.max(np.abs(audio_resampled))
        
        # Apply noise shaping for better 8-bit quality
        audio_8bit = self._noise_shaping(audio_resampled, bits=8)
        
        return audio_8bit
    
    def _noise_shaping(self, audio: np.ndarray, bits: int = 8) -> np.ndarray:
        """
        Apply noise shaping to reduce quantization artifacts.
        Uses simple first-order error diffusion.
        """
        levels = 2 ** bits
        quantizer_scale = (levels - 1) / 2
        
        output = np.zeros_like(audio)
        error = 0
        
        for i in range(len(audio)):
            # Add previous error (simple first-order)
            sample = audio[i] + error * 0.5
            
            # Quantize
            quantized = np.round(sample * quantizer_scale) / quantizer_scale
            quantized = np.clip(quantized, -1.0, 1.0)
            
            # Calculate error for next iteration
            error = sample - quantized
            output[i] = quantized
        
        return output
    
    def _mix_sources(self, sources: dict, sr: int) -> np.ndarray:
        """Mix separated sources back together"""
        mixed = np.zeros_like(list(sources.values())[0])
        
        # Average mix of all sources
        for source_audio in sources.values():
            mixed += source_audio
        
        mixed = mixed / len(sources)
        
        # Normalize
        if np.max(np.abs(mixed)) > 1.0:
            mixed = mixed / np.max(np.abs(mixed))
        
        return mixed
    
    def _update_status(self, output_dir: Path, status: str, message: str):
        """Update job status file"""
        status_data = {
            "status": status,
            "message": message,
            "timestamp": datetime.now().isoformat(),
            "progress": self._get_progress(status)
        }
        
        status_file = output_dir / "status.json"
        with open(status_file, "w") as f:
            json.dump(status_data, f)
    
    def _get_progress(self, status: str) -> int:
        """Get progress percentage for status"""
        progress_map = {
            "loading": 10,
            "separating": 30,
            "converting": 60,
            "mixing": 80,
            "saving": 90,
            "completed": 100,
            "error": 0
        }
        return progress_map.get(status, 0)
