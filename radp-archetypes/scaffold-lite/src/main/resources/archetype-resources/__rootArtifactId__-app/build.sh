#!/bin/bash

./mvnw clean package -Penv-dev || exit 1docker build -f ./Dockerfile -t xooooooooox/${appName} .
