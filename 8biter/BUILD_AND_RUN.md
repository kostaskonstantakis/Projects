# 🐳 8biter in Docker Desktop - Step by Step

## ✅ Prerequisites Check

```bash
docker --version
docker-compose --version
```

Expected output:
- Docker Desktop 4.x+
- docker-compose 2.x+

---

## 🏗️ Step 1: Build the Docker Image

```bash
cd /mnt/c/Users/gussl/Projects/8biter
docker-compose build
```

**What this does:**
- Builds Python 3.11 base image
- Installs ffmpeg, nginx, supervisor
- Installs Node.js
- Builds React frontend (Vite)
- Installs Python dependencies
- Creates single unified image

**Expected time:** 5-10 minutes (first time)

---

## 🚀 Step 2: Run the Container

**Option A: Foreground (see logs in real-time)**
```bash
docker-compose up
```

**Option B: Background (detached)**
```bash
docker-compose up -d
```

---

## ✨ Step 3: Verify Everything Works

### Check container is running
```bash
docker-compose ps
```

Should show:
```
NAME          STATUS        PORTS
8biter-app-1  Up 2 minutes  0.0.0.0:80->80/tcp, 0.0.0.0:8000->8000/tcp
```

### Test endpoints
```bash
# Frontend
curl http://localhost

# Backend health check
curl http://localhost:8000/health

# API docs
# Open in browser: http://localhost:8000/docs
```

### View logs
```bash
# All logs
docker-compose logs

# Realtime logs
docker-compose logs -f

# Only FastAPI logs
docker-compose logs app | grep fastapi

# Only Nginx logs
docker-compose logs app | grep nginx
```

---

## 🌐 Access Points

| What | URL | Purpose |
|------|-----|---------|
| **Web UI** | http://localhost | React frontend |
| **API** | http://localhost:8000 | FastAPI server |
| **API Docs** | http://localhost:8000/docs | Swagger UI |
| **Upload Audio** | http://localhost/api/convert/file | Convert uploaded file |

---

## 🛑 Stop the Container

```bash
# Stop (keeps data)
docker-compose stop

# Stop and remove
docker-compose down

# Stop and remove everything (including volumes)
docker-compose down -v
```

---

## 🔧 Troubleshooting

### Ports already in use?
```bash
# Change in docker-compose.yml:
ports:
  - "8080:80"    # Access at http://localhost:8080
  - "8001:8000"  # API at http://localhost:8001
```

### Need to rebuild after changes?
```bash
# Rebuild without cache
docker-compose build --no-cache
docker-compose up
```

### Check what's inside container?
```bash
# Open shell
docker-compose exec app bash

# List frontend files
ls -la /app/frontend_dist

# List backend files
ls -la /app/app

# Check supervisor status
supervisorctl status
```

### Frontend not loading?
```bash
# Verify Nginx is serving files
docker-compose exec app nginx -t

# Check Nginx error log
docker-compose logs app | grep nginx

# Rebuild frontend
docker-compose build --no-cache
docker-compose up
```

### API request fails?
```bash
# Test FastAPI directly
curl http://localhost:8000/health

# Check Python version
docker-compose exec app python --version

# Check installed packages
docker-compose exec app pip list
```

---

## 📊 Monitor Docker Desktop

1. **Open Docker Desktop** app
2. **Go to "Containers"** tab
3. **Look for "8biter"** container
4. **Click to see:**
   - Real-time logs
   - Resource usage (CPU, Memory)
   - Port mappings
   - Volume mounts

---

## 🎯 Next Steps

After container is running:

1. **Test upload:** http://localhost (drag & drop audio file)
2. **Check conversion:** Monitor progress in UI
3. **Download result:** Click download button
4. **API testing:** http://localhost:8000/docs

---

**🎵 Ready to convert music to 8-bit!**
