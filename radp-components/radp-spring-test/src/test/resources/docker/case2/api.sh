#!/bin/sh

# Get Redis connection details from environment variables
REDIS_HOST=${REDIS_HOST:-localhost}
REDIS_PORT=${REDIS_PORT:-6379}

echo "Starting API service with Redis connection to $REDIS_HOST:$REDIS_PORT"

# Test Redis connection
while true; do
  # Try to ping Redis
  if redis-cli -h $REDIS_HOST -p $REDIS_PORT ping >/dev/null 2>&1; then
    echo "Successfully connected to Redis at $REDIS_HOST:$REDIS_PORT"
    break
  else
    echo "Waiting for Redis at $REDIS_HOST:$REDIS_PORT..."
    sleep 1
  fi
done

# Keep the script running
while true; do
  sleep 10
done
