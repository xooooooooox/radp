#!/bin/bash
set -e

# 获取当前脚本的目录
script_dir="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
# 获取工程根目录
project_root="$(dirname "$(dirname "$script_dir")")"
pushd "$project_root" || exit 1
./mvnw clean package -DskipTests -P env-dev

# 构建并启动
popd || exit 1
docker compose up --build -d || exit 1