#!/bin/bash
set -e

./mvnw clean -Dauto.layered.enabled=true -Pcoding,env-dev package || sh mvnw clean -Dauto.layered.enabled=true -Pcoding,env-dev package
docker build -f ./Dockerfile -t ${imageNamespace}/${appName} .
docker push ${imageNamespace}/${appName}