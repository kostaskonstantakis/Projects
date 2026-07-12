import yt_dlp
import spotipy
from spotipy.oauth2 import SpotifyClientCredentials
from pathlib import Path
import json
from datetime import datetime
from .audio_processor import AudioProcessor
import os

class AudioDownloader:
    """Download audio from various sources (YouTube, Spotify, SoundCloud, etc.)"""
    
    def __init__(self):
        self.processor = AudioProcessor()
        self.ydl_opts = {
            'format': 'bestaudio/best',
            'postprocessors': [{
                'key': 'FFmpegExtractAudio',
                'preferredcodec': 'wav',
                'preferredquality': '192',
            }],
            'quiet': False,
            'no_warnings': False,
        }
    
    def download_and_convert(self, url: str, source: str, job_id: str):
        """Download audio and convert to 8-bit"""
        output_dir = Path("outputs") / job_id
        output_dir.mkdir(parents=True, exist_ok=True)
        downloads_dir = Path("downloads") / job_id
        downloads_dir.mkdir(parents=True, exist_ok=True)
        
        try:
            # Download based on source
            if source == "youtube":
                audio_file = self._download_youtube(url, downloads_dir)
            elif source == "spotify":
                audio_file = self._download_spotify(url, downloads_dir)
            elif source == "soundcloud":
                audio_file = self._download_soundcloud(url, downloads_dir)
            else:
                raise ValueError(f"Unsupported source: {source}")
            
            # Convert to 8-bit
            self.processor.convert_to_8bit(str(audio_file), job_id)
            
        except Exception as e:
            self._update_status(output_dir, "error", f"Error: {str(e)}")
            raise
    
    def _download_youtube(self, url: str, output_dir: Path) -> str:
        """Download audio from YouTube"""
        opts = self.ydl_opts.copy()
        opts['outtmpl'] = str(output_dir / '%(title)s.%(ext)s')
        
        with yt_dlp.YoutubeDL(opts) as ydl:
            info = ydl.extract_info(url, download=True)
            filename = ydl.prepare_filename(info)
            return filename
    
    def _download_spotify(self, url: str, output_dir: Path) -> str:
        """Download audio from Spotify (requires YouTube fallback)"""
        try:
            client_id = os.getenv("SPOTIFY_CLIENT_ID")
            client_secret = os.getenv("SPOTIFY_CLIENT_SECRET")
            
            if not client_id or not client_secret:
                raise ValueError("Spotify credentials not set in environment")
            
            sp = spotipy.Spotify(auth_manager=SpotifyClientCredentials(
                client_id=client_id,
                client_secret=client_secret
            ))
            
            # Get track info
            track = sp.track(url)
            artist = track['artists'][0]['name']
            title = track['name']
            
            # Search YouTube for track
            search_query = f"{artist} {title}"
            yt_url = self._search_youtube(search_query)
            
            if not yt_url:
                raise ValueError("Could not find track on YouTube")
            
            return self._download_youtube(yt_url, output_dir)
        
        except ImportError:
            raise RuntimeError("Spotipy not installed. Install with: pip install spotipy")
    
    def _download_soundcloud(self, url: str, output_dir: Path) -> str:
        """Download audio from SoundCloud"""
        opts = self.ydl_opts.copy()
        opts['outtmpl'] = str(output_dir / '%(title)s.%(ext)s')
        
        with yt_dlp.YoutubeDL(opts) as ydl:
            info = ydl.extract_info(url, download=True)
            filename = ydl.prepare_filename(info)
            return filename
    
    def _search_youtube(self, query: str) -> str:
        """Search YouTube for a track"""
        with yt_dlp.YoutubeDL({'quiet': True}) as ydl:
            info = ydl.extract_info(f"ytsearch:{query}", download=False)
            if info and 'entries' in info and len(info['entries']) > 0:
                return info['entries'][0]['webpage_url']
        return None
    
    def _update_status(self, output_dir: Path, status: str, message: str):
        """Update job status file"""
        status_data = {
            "status": status,
            "message": message,
            "timestamp": datetime.now().isoformat(),
            "progress": 0 if status == "error" else 50
        }
        
        status_file = output_dir / "status.json"
        with open(status_file, "w") as f:
            json.dump(status_data, f)
