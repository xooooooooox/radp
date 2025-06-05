FROM nginx:alpine

# Install Redis CLI for testing Redis connection
RUN apk add --no-cache redis

# Copy our custom configuration and scripts
COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY api.sh /api.sh
COPY health.sh /health.sh

# Make scripts executable
RUN chmod +x /api.sh /health.sh

# Expose port 8080
EXPOSE 8080

# Start Nginx and our API script
CMD ["/bin/sh", "-c", "/api.sh & nginx -g 'daemon off;'"]