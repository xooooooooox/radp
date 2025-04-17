#!/usr/bin/env bash
set -e

cur_dir="$(cd "$(dirname "$(readlink -f "${BASH_SOURCE[0]}")")" && pwd)"
app_dir="$cur_dir"/app
infra_dir="$cur_dir"/infra

pushd "$app_dir" || exit 1
docker-compose ps
popd || exit 1
