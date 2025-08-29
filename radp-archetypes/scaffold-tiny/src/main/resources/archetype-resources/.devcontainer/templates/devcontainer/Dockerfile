FROM mcr.microsoft.com/devcontainers/base:ubuntu-22.04

# Utilities: install jq and yq (supports amd64/arm64)
RUN set -eux; \
    apt-get update; \
    apt-get install -y --no-install-recommends ca-certificates curl jq wget; \
    arch="$(dpkg --print-architecture)"; \
    case "$arch" in \
      amd64) yq_arch=amd64 ;; \
      arm64) yq_arch=arm64 ;; \
      armhf) yq_arch=arm ;; \
      *) yq_arch=amd64 ;; \
    esac; \
    wget -qO /usr/local/bin/yq "https://github.com/mikefarah/yq/releases/latest/download/yq_linux_${yq_arch}"; \
    chmod +x /usr/local/bin/yq; \
    rm -rf /var/lib/apt/lists/*

# Set timezone
ENV TZ=Asia/Shanghai
