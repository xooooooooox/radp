#!/bin/bash

# 检查容器是否已存在
if docker container inspect ${appName} &>/dev/null; then
    read -p "容器 '${appName}' 已存在，是否需要删除它？(y/n): " choice
    if [[ "$choice" =~ ^[Yy]$ ]]; then
        echo "正在删除容器 'chatgpt-api'..."
        docker rm -f chatgpt-api
    else
        echo "操作中止。"
        exit 0
    fi
fi

# 启动新容器
echo "正在启动容器 '${appName}'..."
docker run --name ${appName} \
  -p 8888:8888 \
  -e SPRING_PROFILES_ACTIVE=dev \
  -d xooooooooox/${appName}
