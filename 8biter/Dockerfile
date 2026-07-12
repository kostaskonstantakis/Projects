# Multi-stage build: Frontend + Backend + Nginx in one image
FROM python:3.11-slim as base

WORKDIR /app

# Install system dependencies (including supervisor)
RUN apt-get update && apt-get install -y \
    ffmpeg \
    libsndfile1 \
    nginx \
    supervisor \
    curl \
    && rm -rf /var/lib/apt/lists/*

# Install Node.js for frontend build
RUN curl -fsSL https://deb.nodesource.com/setup_18.x | bash - && \
    apt-get install -y nodejs && \
    rm -rf /var/lib/apt/lists/*

# ===== Stage 1: Build Frontend =====
FROM base as frontend-builder
WORKDIR /app/frontend

COPY frontend/package*.json ./
RUN npm install

COPY frontend .
RUN npm run build

# ===== Stage 2: Build Backend + serve both =====
FROM base as production

WORKDIR /app

# Copy Python requirements
COPY backend/requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

# Copy backend code
COPY backend/app ./app
COPY backend/app/__init__.py ./app/

# Copy built frontend from frontend-builder stage
COPY --from=frontend-builder /app/frontend/dist /app/frontend_dist

# Create necessary directories
RUN mkdir -p uploads outputs downloads

# Configure Nginx
RUN rm /etc/nginx/sites-enabled/default || true
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Configure Supervisor to run both nginx and fastapi
RUN mkdir -p /var/log/supervisor && mkdir -p /etc/supervisor/conf.d

COPY --chown=root:root <<'EOF' /etc/supervisor/conf.d/app.conf
[supervisord]
nodaemon=true
logfile=/var/log/supervisor/supervisord.log
pidfile=/var/run/supervisord.pid

[program:nginx]
command=/usr/sbin/nginx -g "daemon off;"
autostart=true
autorestart=true
priority=999
stdout_logfile=/var/log/supervisor/nginx_stdout.log
stderr_logfile=/var/log/supervisor/nginx_stderr.log

[program:fastapi]
command=uvicorn app.main:app --host 0.0.0.0 --port 8000
directory=/app
autostart=true
autorestart=true
priority=999
stdout_logfile=/var/log/supervisor/fastapi_stdout.log
stderr_logfile=/var/log/supervisor/fastapi_stderr.log
EOF

EXPOSE 80 8000

CMD ["/usr/bin/supervisord", "-c", "/etc/supervisor/conf.d/app.conf"]
