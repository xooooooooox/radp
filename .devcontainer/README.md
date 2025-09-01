# DevContainer Guide (Updated)

This project uses DevContainer to provide a consistent local development environment and manages peripheral dependencies (MySQL, Redis, etc.) via composable templates and helper scripts. This document is aligned with the current repository configuration.

## Quick Start

- Prerequisites:
  - Docker Desktop (or another Docker engine)
  - VS Code (Dev Containers / Remote - Containers extension recommended) or JetBrains Gateway
- After opening the project:
  1) VS Code: Use the Command Palette and select “Reopen in Container”.
  2) JetBrains Gateway: Choose “Connect to Dev Container”, point to this project directory, and start your IDE.

On the first start, postCreateCommand will run automatically, invoking .devcontainer/devcontainer_helper to link templates and start dependency services.

## Current DevContainer Structure & Defaults

- devcontainer.json
  - dockerComposeFile: `.devcontainer/templates/devcontainer/docker-compose.yml`
  - service: `devcontainer`
  - workspaceFolder: `/home/vscode/workspace`
  - features:
    - docker-in-docker: latest
    - Java Feature: `ghcr.io/devcontainers/features/java:1`
      - version: `8` (Amazon Corretto)
      - Maven: `3.9.9` (enabled)
  - forwardPorts: 8888 (app), 9999 (management), 3306 (MySQL), 6379 (Redis)
  - postCreateCommand:
    - `bash .devcontainer/devcontainer_helper setup --env dev && bash .devcontainer/devcontainer_helper up --env dev`
- DevContainer compose file: `.devcontainer/templates/devcontainer/docker-compose.yml`
  - Builds the base development container from `.devcontainer/templates/devcontainer/Dockerfile` (mounts `../..:/workspace:cached`, network name `scaffold-std-demo-network`).

Note: If you need a higher JDK version, adjust `version` in devcontainer.json’s Java Feature, or add additional Features as needed.

## Managing Templates & Dependencies (Recommended)

Use `.devcontainer/devcontainer_helper` to manage templates (MySQL, Redis, Kafka, etc.), environment variables, and compose file combinations.

- Configuration file: `.devcontainer/setup.yml`
  - available: the list of available templates
  - enabled: templates enabled by default (currently `mysql`, `redis`)
- Environment file resolution order (if they exist, appended to `docker compose --env-file` in order):
  1) `.devcontainer/.env`
  2) `.devcontainer/env/.env.<svc>` (common vars per enabled template)
  3) `.devcontainer/env/.env.<svc>.<ENV>` (env-specific overrides, e.g., dev/test)
- Compose file resolution priority (per enabled template):
  1) `.devcontainer/compose.<svc>.yml` (created as symlink by `setup`)
  2) `.devcontainer/docker-compose.<svc>.yml`
  3) `.devcontainer/templates/<svc>/docker-compose.yml`

Common commands:
- Initialize/link templates (updates devcontainer.json postCreateCommand and creates compose links):
  - `bash .devcontainer/devcontainer_helper setup --env dev`
- Start dependencies:
  - `bash .devcontainer/devcontainer_helper up --env dev`
  - Options: `--no-build`, `--dry-run`
- Stop dependencies:
  - `bash .devcontainer/devcontainer_helper down --env dev [--volumes] [--remove-orphans]`

Enable/disable templates:
1) Edit `.devcontainer/setup.yml`, add/remove entries in `devcontainer.templates.enabled` (e.g., add `app`).
2) Run `bash .devcontainer/devcontainer_helper setup --env dev` to re-generate links.
3) Run `bash .devcontainer/devcontainer_helper up --env dev` to start the updated service composition.

## Tips for VS Code & JetBrains

- VS Code: postCreateCommand runs automatically on first container entry; if it fails, try “Rebuild Container”.
- JetBrains: install Docker, .env, and Spring related plugins (recommended plugins are listed in devcontainer.json under `customizations.jetbrains.plugins`).
- Port forwarding: 8888/9999/3306/6379 by default; adjust `forwardPorts` in devcontainer.json as needed.

## devcontainer.json Key Fields (as in this repo)

- name: `scaffold-std-demo`
- dockerComposeFile: `[
  "templates/devcontainer/docker-compose.yml"
]`
- service: `devcontainer`
- workspaceFolder: `/home/vscode/workspace`
- features:
  - `docker-in-docker: latest`
  - `ghcr.io/devcontainers/features/java:1` (version: 8, jdkDistro: amzn, installMaven: true, mavenVersion: 3.9.9)
- customizations:
  - VS Code extensions: `vscjava.vscode-java-pack`, `pivotal.vscode-boot-dev-pack`, etc.
  - JetBrains plugins: listed under `customizations.jetbrains.plugins`
- postCreateCommand: `bash .devcontainer/devcontainer_helper setup --env dev && bash .devcontainer/devcontainer_helper up --env dev`

Tip: `workspaceFolder` is `/home/vscode/workspace`; the devcontainer template compose mounts the host repo to `/workspace` for lower-level containers. This does not affect your IDE working directory.

## Template Directory Overview

- `.devcontainer/templates/devcontainer/`: DevContainer base service (the development container itself)
- `.devcontainer/templates/<svc>/docker-compose.yml`: Concrete service templates (mysql, redis, kafka, ...)
- `.devcontainer/compose.<svc>.yml`: Symlinks generated after `setup` for unified composition

Default enabled templates (setup.yml):
- mysql
- redis

If you want to enable the application template (`app`) to run the app inside the container, add `app` to `enabled` and run `setup`, then `up`.

## Troubleshooting

- Docker not running or insufficient resources: ensure Docker is running and has enough CPU/memory.
- Port conflicts: adjust `forwardPorts` in devcontainer.json or free the local ports.
- postCreateCommand failed: inside the container, run:
  - `bash .devcontainer/devcontainer_helper setup --env dev`
  - `bash .devcontainer/devcontainer_helper up --env dev`
  Check `.devcontainer/.env` and `.devcontainer/env/*`, and use `docker compose logs` to inspect services.
- Missing yq/jq: the script will prompt you to install; follow the prompts and retry.

## References

- DevContainer Specification: https://containers.dev/implementors/json_reference/
- VS Code Dev Containers docs: https://code.visualstudio.com/docs/devcontainers/containers
- GitHub Codespaces docs: https://docs.github.com/en/codespaces/setting-up-your-project-for-codespaces/adding-a-dev-container-configuration/introduction-to-dev-containers
