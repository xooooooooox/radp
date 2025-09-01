# DevContainer 开发说明

本项目使用 DevContainer 提供一致的本地开发环境，并通过可组合的模板与脚本管理周边依赖（MySQL、Redis 等）。

## 快速开始

- 前置条件：
  - 已安装 Docker Desktop（或其它 Docker 引擎）
  - VS Code（建议安装 Remote - Containers / Dev Containers 扩展）或 JetBrains Gateway
- 打开项目后：
  1) 使用 VS Code：命令面板选择“Reopen in Container”；
  2) 使用 JetBrains Gateway：选择“Connect to Dev Container”，指向本项目目录并启动 IDE。

第一次启动会自动执行 postCreateCommand，调用 .devcontainer/devcontainer_helper 执行模板链接与依赖服务启动。

## 当前 DevContainer 结构与默认行为

- devcontainer.json
  - dockerComposeFile: `.devcontainer/templates/devcontainer/docker-compose.yml`
  - service: `devcontainer`
  - workspaceFolder: `/home/vscode/workspace`
  - features:
    - docker-in-docker: latest
    - Java Feature: `ghcr.io/devcontainers/features/java:1`
      - version: `8`（Amazon Corretto）
      - Maven: `3.9.9`（已启用）
  - forwardPorts: 8888（应用）、9999（管理端口）、3306（MySQL）、6379（Redis）
  - postCreateCommand:
    - `bash .devcontainer/devcontainer_helper setup --env dev && bash .devcontainer/devcontainer_helper up --env dev`
- DevContainer 的 compose 文件：`.devcontainer/templates/devcontainer/docker-compose.yml`
  - 以 `.devcontainer/templates/devcontainer/Dockerfile` 构建基础开发容器（挂载 `../..:/workspace:cached`，网络名 `scaffold-std-demo-network`）。

说明：若项目需要更高版本 JDK，可在 devcontainer.json 的 Java Feature 配置中调整 `version`，或按需添加额外 Feature。

## 模板与依赖服务管理（推荐方式）

使用 `.devcontainer/devcontainer_helper` 管理模板（MySQL、Redis、Kafka 等）与环境变量、compose 文件组合。

- 配置文件：`.devcontainer/setup.yml`
  - available: 可选模板列表
  - enabled: 默认启用模板（当前为 `mysql`、`redis`）
- 环境变量文件解析顺序（若存在则依次加入 docker compose --env-file）：
  1) `.devcontainer/.env`
  2) `.devcontainer/env/.env.<svc>`（每个启用模板的通用变量）
  3) `.devcontainer/env/.env.<svc>.<ENV>`（按环境覆写，例如 dev/test）
- compose 文件解析优先级（每个启用模板）：
  1) `.devcontainer/compose.<svc>.yml`（脚本 `setup` 会创建指向模板的符号链接）
  2) `.devcontainer/docker-compose.<svc>.yml`
  3) `.devcontainer/templates/<svc>/docker-compose.yml`

常用命令：
- 初始化/链接模板（会更新 devcontainer.json 的 postCreateCommand 并创建 compose 链接）：
  - `bash .devcontainer/devcontainer_helper setup --env dev`
- 启动依赖服务：
  - `bash .devcontainer/devcontainer_helper up --env dev`
  - 选项：`--no-build`、`--dry-run`
- 停止依赖服务：
  - `bash .devcontainer/devcontainer_helper down --env dev [--volumes] [--remove-orphans]`

启用/禁用模板步骤：
1) 编辑 `.devcontainer/setup.yml`，在 `devcontainer.templates.enabled` 中增删条目（例如添加 `app`）。
2) 运行 `bash .devcontainer/devcontainer_helper setup --env dev` 重新生成链接。
3) 运行 `bash .devcontainer/devcontainer_helper up --env dev` 启动更新后的服务组合。

## VS Code 与 JetBrains 使用提示

- VS Code：首次进入容器会自动执行 postCreateCommand；如失败，可从命令面板选择“Rebuild Container”。
- JetBrains：建议安装 Docker、.env、Spring 相关插件（devcontainer.json 已包含推荐插件列表）。
- 端口转发：默认已映射 8888/9999/3306/6379，可在 devcontainer.json 的 `forwardPorts` 调整。

## devcontainer.json 关键字段（与当前仓库一致）

- name: `scaffold-std-demo`
- dockerComposeFile: `["templates/devcontainer/docker-compose.yml"]`
- service: `devcontainer`
- workspaceFolder: `/home/vscode/workspace`
- features:
  - `docker-in-docker: latest`
  - `ghcr.io/devcontainers/features/java:1`（version: 8, jdkDistro: amzn, installMaven: true, mavenVersion: 3.9.9）
- customizations:
  - VS Code 扩展：`vscjava.vscode-java-pack`、`pivotal.vscode-boot-dev-pack` 等
  - JetBrains 插件：已在 `customizations.jetbrains.plugins` 中列出
- postCreateCommand: `bash .devcontainer/devcontainer_helper setup --env dev && bash .devcontainer/devcontainer_helper up --env dev`

提示：`workspaceFolder` 为 `/home/vscode/workspace`；模板的 devcontainer compose 把宿主仓库挂载到 `/workspace` 供底层容器使用，这不会影响 IDE 的工作目录。

## 模板目录概览

- `.devcontainer/templates/devcontainer/`: DevContainer 基础服务（开发容器本体）
- `.devcontainer/templates/<svc>/docker-compose.yml`: 具体服务模板（mysql、redis、kafka...）
- `.devcontainer/compose.<svc>.yml`: 执行 setup 后生成的符号链接，便于统一组合

默认启用模板（setup.yml）：
- mysql
- redis

如需启用应用模板（app）以在容器中运行应用，请在 `enabled` 中加入 `app` 并执行 `setup`、`up`。

## 常见问题（Troubleshooting）

- Docker 未启动或资源不足：请确保 Docker 正常运行并分配足够 CPU/内存。
- 端口占用：调整 devcontainer.json 的 `forwardPorts` 或释放本机端口。
- postCreateCommand 失败：在容器终端内执行：
  - `bash .devcontainer/devcontainer_helper setup --env dev`
  - `bash .devcontainer/devcontainer_helper up --env dev`
  检查 `.devcontainer/.env` 与 `.devcontainer/env/*` 配置，并用 `docker compose logs` 查看服务日志。
- 缺少 yq/jq：脚本会提示安装；按提示安装后重试。

## 参考

- DevContainer 规范：https://containers.dev/implementors/json_reference/
- VS Code Dev Containers 文档：https://code.visualstudio.com/docs/devcontainers/containers
- GitHub Codespaces 文档：https://docs.github.com/en/codespaces/setting-up-your-project-for-codespaces/adding-a-dev-container-configuration/introduction-to-dev-containers
