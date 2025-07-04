# Development with DevContainers

This project supports development using DevContainers, which provides a consistent, isolated development environment
with all necessary dependencies pre-configured.

## Getting Started

### Prerequisites

- [Docker Desktop](https://www.docker.com/products/docker-desktop/)
- [JetBrains Gateway](https://www.jetbrains.com/remote-development/gateway/) (for JetBrains IDEs)
- [VS Code](https://code.visualstudio.com/)
  with [Remote - Containers extension](https://marketplace.visualstudio.com/items?itemName=ms-vscode-remote.remote-containers) (
  for VS Code)

### Basic Setup

The default configuration includes a Java development environment with the following:

- Java 17
- Maven 3.9.9
- Git
- Node.js (LTS version)
- Docker-in-Docker support
- Common development tools

The development environment is built using a Dockerfile that allows for customization of Java version, Maven, Gradle,
and Node.js installation.

### Customizing Your Development Environment

This project follows a modular approach to DevContainers, allowing you to include only the services you need for your
development work.

#### Available Templates

The following templates are available in the `.devcontainer/templates` directory:

- `app`: Application service (requires MySQL)
- `kafka`: Kafka message broker
- `kafka-eagle`: Kafka Eagle monitoring tool
- `mysql`: MySQL database
- `nginx`: Nginx web server
- `ollama`: Ollama AI model server
- `pgvector`: PostgreSQL with vector support
- `rabbitmq`: RabbitMQ message broker
- `redis`: Redis database
- `redis-commander`: Redis Commander web interface
- `rocketmq`: RocketMQ message broker
- `xxl-job`: XXL-Job scheduler (requires MySQL)
- `zookeeper`: Zookeeper for distributed coordination

#### How to Include Templates

##### Using the Setup Script (Recommended)

The easiest way to set up your development environment is to use the provided setup script:

```bash
cd .devcontainer
chmod +x setup.sh
./setup.sh
```

The script will read configuration from `setup.yml` and automatically create the necessary configuration files based on
the enabled templates.

##### Manual Setup

If you prefer to set up your environment manually, there are two scenarios to consider:

###### Scenario 1: Running Templates in the DevContainer (Docker-in-Docker)

If you want to run the templates inside the DevContainer using Docker-in-Docker:

1. Decide which templates you need for your development environment
2. Copy the corresponding docker-compose.yml files from the templates directory to the `.devcontainer` directory
3. Update the `postCreateCommand` in `.devcontainer/devcontainer.json` to start the services

For example, if you need MySQL and the application service:

1. Copy `.devcontainer/templates/mysql/docker-compose.yml` to `.devcontainer/docker-compose.mysql.yml`
2. Copy `.devcontainer/templates/app/docker-compose.yml` to `.devcontainer/docker-compose.app.yml`
3. Update the `postCreateCommand` in `.devcontainer/devcontainer.json`:

```json
{
  "postCreateCommand": "cd .devcontainer && docker compose --env-file .env -f docker-compose.mysql.yml -f docker-compose.app.yml up -d"
}
```

###### Scenario 2: Running Templates on the Host Machine's Docker

If you want to run the templates on the host machine's Docker:

1. Decide which templates you need for your development environment
2. Copy the corresponding docker-compose.yml files from the templates directory to the `.devcontainer` directory
3. Update the `dockerComposeFile` in `.devcontainer/devcontainer.json`

For example, if you need MySQL and the application service:

1. Copy `.devcontainer/templates/mysql/docker-compose.yml` to `.devcontainer/docker-compose.mysql.yml`
2. Copy `.devcontainer/templates/app/docker-compose.yml` to `.devcontainer/docker-compose.app.yml`
3. Update the `dockerComposeFile` in `.devcontainer/devcontainer.json`:

```json
{
  "dockerComposeFile": [
    "docker-compose.yml",
    "docker-compose.mysql.yml",
    "docker-compose.app.yml"
  ]
}
```

### Starting the Development Environment

#### With VS Code

1. Open the project folder in VS Code
2. Click on the green button in the bottom-left corner
3. Select "Reopen in Container"

#### With JetBrains Gateway

1. Start JetBrains Gateway
2. Select "Connect to Dev Container"
3. Choose "Local Docker Container"
4. Browse to this project directory
5. Select your preferred JetBrains IDE
6. Start developing in the containerized environment

### DevContainer Configuration (devcontainer.json)

The `.devcontainer/devcontainer.json` file is the main configuration file for DevContainers. It defines how the
development container should be set up and configured.

Here's a detailed explanation of the properties in our devcontainer.json:

```json
{
  "name": "scaffold-std-demo",
  "dockerComposeFile": "docker-compose.yml",
  "service": "devcontainer",
  "workspaceFolder": "/workspace",
  "features": {
    "docker-in-docker": "latest"
  },
  "customizations": {
    "vscode": {
      "extensions": [
        "vscjava.vscode-java-pack",
        "pivotal.vscode-boot-dev-pack"
      ],
      "settings": {
        "java.home": "/docker-java-home"
      }
    }
  },
  "forwardPorts": [
    8888,
    9999,
    3306,
    6379
  ],
  "remoteUser": "vscode",
  "postCreateCommand": "cd .devcontainer && docker compose --env-file .env -f docker-compose.mysql.yml -f docker-compose.app.yml up -d"
}
```

The properties in the devcontainer.json file are:

- `name`: Identifies the DevContainer in the UI
- `dockerComposeFile`: Specifies which Docker Compose file(s) to use
- `service`: Specifies which service from the Docker Compose file to connect to (in this case, "devcontainer")
- `workspaceFolder`: The path in the container where your project files are mounted
- `features`: Additional container features to install (only docker-in-docker in this case)
- `customizations`: IDE-specific settings
- `forwardPorts`: Ports to automatically forward from the container to the local machine (8888 for the application
  server, 9999 for the management server, 3306 for MySQL, and 6379 for Redis)
- `remoteUser`: The user to run as within the container
- `postCreateCommand`: Command to run after the container is created (starts MySQL and the application service)

#### Key Properties Explained:

- **name**: Identifies the DevContainer in the UI.
- **dockerComposeFile**: Specifies which Docker Compose file(s) to use. Can be a string or an array of strings for
  multiple files.
- **service**: Specifies which service from the Docker Compose file to connect to (in this case, "devcontainer").
- **workspaceFolder**: The path in the container where your project files are mounted.
- **features**: Additional container features to install (docker-in-docker in this case).
- **customizations**: IDE-specific settings:
  - **vscode.extensions**: VS Code extensions to automatically install.
  - **vscode.settings**: VS Code settings to apply in the container.
- **forwardPorts**: Ports to automatically forward from the container to the local machine.
- **remoteUser**: The user to run as within the container.
- **postCreateCommand**: Command to run after the container is created (starts additional services).

#### Customizing DevContainer Configuration

You can customize the DevContainer configuration by:

1. Manually editing the `.devcontainer/devcontainer.json` file
2. Using the setup script to add features:
   ```bash
   cd .devcontainer
   chmod +x setup.sh
   ./setup.sh
   ```

The setup script reads configuration from `setup.yml` and updates the DevContainer configuration accordingly.

### Dockerfile Configuration

The `.devcontainer/Dockerfile` is used to build the development container image. It provides several customization
options through build arguments:

```dockerfile
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
```

#### Build Arguments

- **VARIANT**: The Java version to use (default: "11", currently set to "17")
- **INSTALL_MAVEN**: Whether to install Maven (default: "false", currently set to "true")
- **MAVEN_VERSION**: The Maven version to install (default: "", currently set to "3.9.9")
- **INSTALL_GRADLE**: Whether to install Gradle (default: "false")
- **GRADLE_VERSION**: The Gradle version to install (default: "", currently set to "8.14.1" in setup.yml but not used
  since INSTALL_GRADLE is false)
- **NODE_VERSION**: The Node.js version to install (default: "lts/*")

These arguments are customized in the `docker-compose.yml` file:

```yaml
services:
  devcontainer:
    build:
      context: .
      dockerfile: Dockerfile
      args:
        VARIANT: "17"
        INSTALL_MAVEN: "true"
        MAVEN_VERSION: "3.9.9"
        INSTALL_GRADLE: "false"
        NODE_VERSION: "lts/*"
```

### Setup.yml Configuration

The `.devcontainer/setup.yml` file is used by the setup script to configure the development environment. It defines:

1. Language versions (Java, Node.js)
2. Build tools (Maven, Gradle)
3. Available and enabled templates

Here's an example of the setup.yml file:

```yaml
devcontainer:
  languages:
    java:
      version: 17
    nodejs:
      version: lts/*
  build:
    tools:
      maven:
        enabled: true
        version: 3.9.9
      gradle:
        enabled: false
        version: 8.14.1

  templates:
    available:
      - kafka
      - kafka-eagle
      - mysql
      - nginx
      - ollama
      - pgvector
      - rabbitmq
      - redis
      - redis-commander
      - rocketmq
      - zookeeper
    enabled:
      - mysql
      - app
```

### Official Documentation

For complete documentation on devcontainer.json properties and options, refer to:

- [DevContainer Specification](https://containers.dev/implementors/json_reference/)
- [VS Code DevContainers Documentation](https://code.visualstudio.com/docs/devcontainers/containers)
- [GitHub Codespaces Documentation](https://docs.github.com/en/codespaces/setting-up-your-project-for-codespaces/adding-a-dev-container-configuration/introduction-to-dev-containers)

## Notes

- The first time you start the container, it may take a few minutes to download and build the images
- You can modify the `devcontainer.json` file to customize the development environment further
- If you need to add or remove features, you'll need to rebuild the container
- The default configuration includes MySQL and the application service, which are started automatically after the
  container is created
