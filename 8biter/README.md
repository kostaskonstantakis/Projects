# 🎵 8biter - Audio to 8-bit Converter

Convert modern music into authentic 8-bit chiptune while preserving all instruments using AI source separation.

## ✨ Features

- 🎸 **Source Separation**: AI-powered instrument isolation (vocals, drums, bass, etc.)
- 📊 **Smart Quantization**: Advanced noise shaping for optimal audio quality
- 🌐 **Multi-Source Support**: YouTube, Spotify, SoundCloud, or upload your own files
- 🎨 **Modern UI**: React + Tailwind CSS frontend
- 🚀 **Single Container**: Unified Docker setup with Nginx + Python + Node.js

## 🐳 Quick Start

### Prerequisites
- Docker & Docker Compose
- `.env` file with Spotify API credentials (optional)

### Setup

```bash
cd 8biter

# Create .env for Spotify (optional)
cp .env.example .env
# Edit .env and add Spotify credentials

# Build and run
docker-compose up --build
```

**Access:**
- Frontend: `http://localhost` (port 80)
- Backend API: `http://localhost:8000` (port 8000)

# Access:
# - Frontend: http://localhost:3000
# - Backend API: http://localhost:8000
```

### Local Setup

#### Backend
```bash
cd backend

# Create virtual environment
python -m venv venv
source venv/bin/activate  # On Windows: venv\Scripts\activate

# Install dependencies
pip install -r requirements.txt

# Install FFmpeg
# Ubuntu/Debian: sudo apt-get install ffmpeg
# macOS: brew install ffmpeg
# Windows: Download from https://ffmpeg.org/download.html

# Run server
uvicorn app.main:app --reload
```

#### Frontend
```bash
cd frontend

# Install dependencies
npm install

# Start dev server
npm run dev

# Access: http://localhost:3000
```

## API Endpoints

### File Upload & Conversion
- `POST /convert/file` - Upload and convert audio file
  ```bash
  curl -X POST -F "file=@song.mp3" http://localhost:8000/convert/file
  ```

### URL Conversion
- `POST /convert/url?url=<URL>&source=<youtube|spotify|soundcloud>`

### Status & Download
- `GET /status/{job_id}` - Check conversion progress
- `GET /download/{job_id}` - Download converted audio
- `GET /preview/{job_id}` - Preview first 30 seconds
- `DELETE /cleanup/{job_id}` - Clean up temporary files

## How It Works

1. **Upload/Download** - Audio is uploaded or downloaded from URL
2. **Source Separation** - DEMUCS AI model separates into 4 stems:
   - Vocals
   - Drums
   - Bass
   - Other
3. **Individual 8-bit Conversion** - Each stem is converted to 8-bit with:
   - Lower sample rate (22kHz)
   - 8-bit depth quantization
   - Error diffusion/noise shaping
4. **Remixing** - All stems are mixed back together
5. **Output** - User can download full audio or preview

## Performance Tips

- **GPU Acceleration**: If CUDA is available, the model will use GPU for faster separation
- **Large Files**: Conversions may take 5-15 minutes depending on file size
- **Browser**: Chrome/Firefox recommended for best compatibility

## Troubleshooting

### DEMUCS Model Not Loading
```bash
# Download model manually
python -c "from demucs.pretrained import get_model; get_model('htdemucs')"
```

### FFmpeg Not Found
```bash
# Ubuntu/Debian
sudo apt-get install ffmpeg

# macOS
brew install ffmpeg

# Windows
# Download from https://ffmpeg.org/download.html and add to PATH
```

### Out of Memory
- Reduce batch size in source_separator.py
- Process smaller audio files
- Use GPU if available

## Future Enhancements

- [ ] WebGL audio visualization
- [ ] Batch processing
- [ ] Custom 8-bit parameter tuning (bitrate, sample rate)
- [ ] Audio effects library
- [ ] Social sharing integration
- [ ] Playlist conversion

## License

MIT

## Contributing

Contributions welcome! Feel free to submit issues and PRs.
