#!/bin/bash

echo "📦 Testing Docker build for 8biter..."
docker-compose config > /dev/null 2>&1
if [ $? -eq 0 ]; then
    echo "✅ docker-compose.yml is valid"
else
    echo "❌ docker-compose.yml has errors"
    docker-compose config
    exit 1
fi

echo ""
echo "📋 Structure summary:"
echo "Frontend: $(ls -la frontend/src/ | wc -l) files in src/"
echo "Backend: $(ls -la backend/app/ | wc -l) files in app/"
echo "Configs: Dockerfile, docker-compose.yml, nginx.conf"
echo ""
echo "✅ Ready to build with: docker-compose up --build"
