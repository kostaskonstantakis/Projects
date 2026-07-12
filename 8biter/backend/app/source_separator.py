import torch
import numpy as np
from pathlib import Path
import warnings

warnings.filterwarnings("ignore")

class SourceSeparator:
    """Separate audio sources using DEMUCS (Music Source Separation)"""
    
    def __init__(self, model_name: str = "htdemucs"):
        """
        Initialize DEMUCS model
        
        Available models:
        - htdemucs (default): High-quality source separation
        - mdx: Facebook's Music Demixing Benchmark
        """
        self.device = "cuda" if torch.cuda.is_available() else "cpu"
        self.model_name = model_name
        self._load_model()
    
    def _load_model(self):
        """Load DEMUCS model"""
        try:
            from demucs.pretrained import get_model
            self.model = get_model(self.model_name)
            self.model = self.model.to(self.device)
            self.model.eval()
        except ImportError:
            raise RuntimeError("DEMUCS not installed. Install with: pip install demucs")
    
    def separate(self, audio: np.ndarray, sr: int) -> dict:
        """
        Separate audio into stems: vocals, drums, bass, other
        
        Args:
            audio: Input audio array (mono or stereo)
            sr: Sample rate
        
        Returns:
            Dictionary with keys: vocals, drums, bass, other
        """
        # Convert to mono if stereo
        if len(audio.shape) > 1:
            audio = np.mean(audio, axis=0)
        
        # Normalize
        if np.max(np.abs(audio)) > 0:
            audio = audio / np.max(np.abs(audio))
        
        # Convert to tensor
        audio_tensor = torch.from_numpy(audio).float().to(self.device)
        
        # Add batch and channel dimensions if needed
        if audio_tensor.dim() == 1:
            audio_tensor = audio_tensor.unsqueeze(0).unsqueeze(0)
        elif audio_tensor.dim() == 2:
            audio_tensor = audio_tensor.unsqueeze(0)
        
        # Separate
        with torch.no_grad():
            stems = self.model.separate(audio_tensor)
        
        # Convert back to numpy
        sources = {}
        stem_names = ["vocals", "drums", "bass", "other"]
        
        for i, stem_name in enumerate(stem_names):
            if stems.shape[1] > i:
                stem_audio = stems[0, i].cpu().numpy()
                # Average channels if stereo
                if stem_audio.ndim > 1:
                    stem_audio = np.mean(stem_audio, axis=0)
                sources[stem_name] = stem_audio
        
        return sources
