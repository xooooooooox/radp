# v19

## Features

- Optimize custom assertion utilities.

## Build

- Add profiles `env-sit` and `o-all-env`.

## Dependencies

- Upgrade `com.google.cloud.tools:jib-maven-plugin` from `3.4.4` to `3.4.5`.

## Scaffold

- Optimize `build.sh`.
- Optimize POMs for `scaffold-xx` `xx-types` and `xx-app` modules.
- Fix `entrypoint.sh` (`Unrecognized option: --spring.config.additional-location=`).
- Optimize `application-local.yaml` for exposing env and loggers endpoints.
- Optimize `dev-ops/docker-compose-app.yaml`.
