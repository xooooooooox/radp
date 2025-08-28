ARG VARIANT="11"
FROM mcr.microsoft.com/vscode/devcontainers/java:0-${VARIANT}

# [Option] Install Maven
ARG INSTALL_MAVEN="false"
ARG MAVEN_VERSION=""

# [Option] Install Gradle
ARG INSTALL_GRADLE="false"
ARG GRADLE_VERSION=""
RUN \
if [ "${INSTALL_MAVEN}" = "true" ]; then \
  su vscode -c "umask 0002 && \
  	. /usr/local/sdkman/bin/sdkman-init.sh && \
	sdk update && \
	for i in {1..3}; do \
		echo \"Attempt $i to install Maven ${MAVEN_VERSION}\" && \
		sdk install maven \"${MAVEN_VERSION}\" && break || \
		echo \"Maven installation failed, retrying in 5 seconds...\" && \
		sleep 5; \
	done";\
fi && \
if [ "${INSTALL_GRADLE}" = "true" ]; then \
  su vscode -c "umask 0002 && \
	. /usr/local/sdkman/bin/sdkman-init.sh && \
	sdk update && \
	for i in {1..3}; do \
		echo \"Attempt $i to install Gradle ${GRADLE_VERSION}\" && \
		sdk install gradle \"${GRADLE_VERSION}\" && break || \
		echo \"Gradle installation failed, retrying in 5 seconds...\" && \
		sleep 5; \
	done"; \
fi

# [Option] Install Node.js
ARG NODE_VERSION="lts/*"
RUN \
if [ "${NODE_VERSION}" != "none" ]; then \
	su vscode -c "umask 0002 && \
		. /usr/local/share/nvm/nvm.sh && \
		nvm install ${NODE_VERSION} 2>&1"; \
fi

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
