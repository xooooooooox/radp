#!/bin/bash

../mvnw clean package -Penv-dev || exit 1
docker build -f ./Dockerfile -t xooooooooox/${appName} .
docker push xooooooooox/${appName} .
