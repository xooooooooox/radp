#!/bin/sh
echo "Content-type: text/plain"
echo ""

# Get Redis connection details from environment variables
REDIS_HOST=${REDIS_HOST:-localhost}
REDIS_PORT=${REDIS_PORT:-6379}

# Check Redis connection
if redis-cli -h $REDIS_HOST -p $REDIS_PORT ping > /dev/null 2>&1; then
  echo "OK"
  exit 0
else
  echo "Redis connection failed"
  exit 1
fi