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

# Set timezone
ENV TZ=Asia/Shanghai
