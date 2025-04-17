#!/usr/bin/env bash
set -e

cur_dir="$(cd "$(dirname "$(readlink -f "${BASH_SOURCE[0]}")")" && pwd)"
app_dir="$cur_dir"/app
infra_dir="$cur_dir"/infra

down_app() {
	local app_name=${1:?"Please specify app name"}
	pushd "$app_dir" || return 1
	docker-compose --env-file "$app_dir"/.env --env-file "$app_dir"/.env.app."$app_name" -f "$app_dir"/docker-compose-"$app_name".yaml down
	popd || return 1
}

down_infr() {
	local infra_name=${2:?"Please specify infra name"}
	pushd "$infra_dir" || return 1
	docker-compose -f "$infra_dir"/docker-compose-"$infra_name".yaml down
	popd || return 1
}

main() {
	local type=${1:?}
	shift
	if [[ "$type" != "app" && "$type" != "infra" ]];then
		echo "Invalid type: $type. 第一个参数 must be 'app' or 'infra'"
		return 1
	fi
	local names=("$@")
	local failed_names=()
	for name in "${names[@]}";do
		down_"$type" "$name" || {
			failed_names+=("$name")
		}
	done
	if (( "${#failed_names[@]}" > 0 ));then
		echo "[$type]==>ERROR: failed to down ${failed_names[*]}"
	fi
}

main "$@"

