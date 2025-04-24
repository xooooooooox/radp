#!/bin/bash
set -e

runtime_env=${1:-dev}

active_profile="env-${runtime_env}"
script_dir="$(cd "$(dirname "$(readlink -f "${BASH_SOURCE[0]}")")" && pwd)"
project_root="${script_dir}/.."
pushd "$script_dir" || exit 1
pushd "$project_root" || exit 1
echo "==>Building with env: ${runtime_env}"
./mvnw clean -Dauto.layered.enabled=true -Pcoding,${active_profile} package || sh mvnw clean -Dauto.layered.enabled=true -Pcoding,${active_profile} package
popd || exit 1
docker build -f ./Dockerfile -t ${imageNamespace}/${appName} .
docker push ${imageNamespace}/${appName}
popd || exit 1
