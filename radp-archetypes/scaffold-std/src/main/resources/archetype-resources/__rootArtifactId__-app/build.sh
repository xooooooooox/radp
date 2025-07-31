#!/bin/bash
set -e

runtime_env=${1:-dev}
artifactory_domain=${2:?}
artifactory_username=${3:?}
artifactory_password=${4:?}

cur_dir="$(cd "$(dirname "$(readlink -f "${BASH_SOURCE[0]}")")" && pwd)"
project_root="${cur_dir}/.."

# 1. build image
docker build \
--build-arg RUNTIME_ENV=${runtime_env} \
--build-arg ARTIFACTORY_DOMAIN=${artifactory_domain} \
--build-arg ARTIFACTORY_USERNAME=${artifactory_username} \
--build-arg ARTIFACTORY_PASSWORD=${artifactory_password} \
-f ${cur_dir}/Dockerfile \
-t ${imageNamespace}/${appName} "$project_root"

# 2. push image
docker push ${imageNamespace}/${appName}
