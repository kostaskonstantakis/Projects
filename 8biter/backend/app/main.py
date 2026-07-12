from fastapi import FastAPI, UploadFile, File, HTTPException, BackgroundTasks
from fastapi.responses import FileResponse, JSONResponse
from fastapi.middleware.cors import CORSMiddleware
from pathlib import Path
import os
from datetime import datetime
import uuid

# Import audio processing modules
from .audio_processor import AudioProcessor
from .downloader import AudioDownloader

app = FastAPI(
    title="8-bit Audio Converter",
    description="Convert audio to 8-bit while preserving all instruments using source separation",
    version="1.0.0"
)

# CORS
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# Create necessary directories
Path("uploads").mkdir(exist_ok=True)
Path("outputs").mkdir(exist_ok=True)
Path("downloads").mkdir(exist_ok=True)

processor = AudioProcessor()
downloader = AudioDownloader()

@app.get("/health")
async def health():
    """Health check endpoint"""
    return {"status": "ok"}

@app.post("/convert/file")
async def convert_file(file: UploadFile = File(...), background_tasks: BackgroundTasks = None):
    """
    Convert uploaded audio file to 8-bit
    
    - **file**: Audio file (mp3, wav, flac, ogg)
    - Returns: Job ID for tracking conversion progress
    """
    try:
        # Generate unique ID for this job
        job_id = str(uuid.uuid4())
        upload_dir = Path("uploads") / job_id
        upload_dir.mkdir(exist_ok=True)
        
        # Save uploaded file
        file_path = upload_dir / file.filename
        content = await file.read()
        with open(file_path, "wb") as f:
            f.write(content)
        
        # Start conversion in background
        background_tasks.add_task(
            processor.convert_to_8bit,
            file_path,
            job_id
        )
        
        return {
            "job_id": job_id,
            "status": "processing",
            "message": "Conversion started. Check status with /status/{job_id}"
        }
    except Exception as e:
        raise HTTPException(status_code=400, detail=str(e))

@app.post("/convert/url")
async def convert_url(url: str, source: str = "youtube", background_tasks: BackgroundTasks = None):
    """
    Convert audio from YouTube, Spotify, or other URL to 8-bit
    
    - **url**: URL to audio source
    - **source**: Source type (youtube, spotify, soundcloud, etc.)
    - Returns: Job ID for tracking conversion progress
    """
    try:
        job_id = str(uuid.uuid4())
        
        # Download audio
        background_tasks.add_task(
            downloader.download_and_convert,
            url,
            source,
            job_id
        )
        
        return {
            "job_id": job_id,
            "status": "downloading",
            "message": "Download and conversion started"
        }
    except Exception as e:
        raise HTTPException(status_code=400, detail=str(e))

@app.get("/status/{job_id}")
async def get_status(job_id: str):
    """Get conversion progress status"""
    status_file = Path("outputs") / job_id / "status.json"
    
    if not status_file.exists():
        raise HTTPException(status_code=404, detail="Job not found")
    
    import json
    with open(status_file, "r") as f:
        status = json.load(f)
    
    return status

@app.get("/download/{job_id}")
async def download_result(job_id: str):
    """Download converted audio file"""
    output_file = Path("outputs") / job_id / "output.wav"
    
    if not output_file.exists():
        raise HTTPException(status_code=404, detail="Output file not found")
    
    return FileResponse(output_file, filename=f"8bit_{job_id}.wav")

@app.get("/preview/{job_id}")
async def get_preview(job_id: str):
    """Get audio preview for listening"""
    preview_file = Path("outputs") / job_id / "preview.wav"
    
    if not preview_file.exists():
        raise HTTPException(status_code=404, detail="Preview not ready")
    
    return FileResponse(preview_file, media_type="audio/wav")

@app.delete("/cleanup/{job_id}")
async def cleanup(job_id: str):
    """Clean up temporary files for a job"""
    import shutil
    upload_dir = Path("uploads") / job_id
    output_dir = Path("outputs") / job_id
    
    if upload_dir.exists():
        shutil.rmtree(upload_dir)
    if output_dir.exists():
        shutil.rmtree(output_dir)
    
    return {"message": "Cleaned up", "job_id": job_id}

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=8000)
