#!/bin/bash

pushd .. || exit 1
./mvnw clean -Dauto.layered.enabled=true -Pcoding,env-dev package || sh mvnw clean -Dauto.layered.enabled=true -Pcoding,env-dev package
popd || exit 1
docker build -f ./Dockerfile -t ${imageNamespace}/${appName} .
docker push ${imageNamespace}/${appName} .
