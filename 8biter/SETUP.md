# 🚀 8biter Single Container Setup

## What Changed ✨

### Before
- ❌ 3 separate containers (backend, frontend, nginx)
- ❌ Network communication overhead
- ❌ 8bitconverter directory was empty

### Now
- ✅ **1 unified container** with everything
- ✅ localhost:80 for frontend
- ✅ localhost:8000 for backend
- ✅ Nginx + Python + Node.js all in one image
- ✅ Supervisor manages both processes

## Architecture 🏗️

```
┌─────────────────────────────────────┐
│         Docker Container             │
├─────────────────────────────────────┤
│ Nginx (port 80)                      │
│  ├─ Serves /app/frontend_dist/      │
│  └─ Proxies /api/* → localhost:8000 │
├─────────────────────────────────────┤
│ FastAPI (port 8000)                 │
│  ├─ /convert/file                   │
│  ├─ /convert/url                    │
│  ├─ /status/{job_id}                │
│  └─ /download/{job_id}              │
├─────────────────────────────────────┤
│ Supervisor (process manager)        │
│  ├─ Manages Nginx                   │
│  └─ Manages FastAPI                 │
└─────────────────────────────────────┘
```

## Build & Run 🐳

```bash
# Build the image
docker-compose build

# Run the container
docker-compose up

# Or build and run in one go
docker-compose up --build

# Stop
docker-compose down
```

## Access Points 🌐

| Service | URL | Purpose |
|---------|-----|---------|
| Frontend | http://localhost | Web UI (React + Tailwind) |
| Backend | http://localhost:8000 | API (FastAPI) |
| API Docs | http://localhost:8000/docs | Swagger UI |
| Logs | docker-compose logs -f app | Real-time logs |

## File Structure 📁

```
8biter/
├── Dockerfile              # Single unified image
├── docker-compose.yml      # One service "app"
├── nginx.conf              # Updated for single container
├── backend/
│   ├── app/               # FastAPI application
│   └── requirements.txt
├── frontend/
│   ├── src/               # React components
│   ├── index.html
│   └── package.json
├── uploads/               # File upload storage
├── outputs/               # Conversion results
└── downloads/             # Downloaded files
```

## Environment Variables 🔐

```bash
cp .env.example .env

# Edit .env with your Spotify credentials (optional):
SPOTIFY_CLIENT_ID=your_id_here
SPOTIFY_CLIENT_SECRET=your_secret_here
```

## Troubleshooting 🔧

### Container won't start
```bash
docker-compose logs app
# Check for missing dependencies or port conflicts
```

### Port 80/8000 already in use
```bash
# Change ports in docker-compose.yml
ports:
  - "8080:80"    # Access at localhost:8080
  - "8001:8000"  # API at localhost:8001
```

### Frontend not loading
```bash
# Ensure frontend build was successful
docker-compose build --no-cache

# Check Nginx is serving files
docker-compose exec app ls -la /app/frontend_dist
```

### API requests failing
```bash
# Verify FastAPI is running
docker-compose logs app | grep fastapi

# Test API directly
curl http://localhost:8000/health
```

## Performance Benefits ⚡

- 🚀 **Faster startup**: Single build, single container
- 💾 **Less memory**: One OS instead of three
- 🔗 **No network overhead**: Everything on localhost
- 📦 **Smaller deployment**: Single image to manage
- 🛡️ **Simplified security**: Single container firewall rules

---

**Ready to convert music to 8-bit chiptunes! 🎵**
