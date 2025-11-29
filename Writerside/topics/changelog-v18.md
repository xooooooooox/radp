# v18

## Dependencies

- Upgrade `org.sonatype.central:central-publishing-maven-plugin` from `0.6.0` to `0.7.0`.

## Build

- Add support for publishing snapshots to the Central portal.
- Add profile `env.uat`.
- Optimize profile `repo-central`:
  - Add properties `auto.layered.enabled` and `auto.assembly.enabled`.
- In profile `coding`, add property `user.docker.build.namespace`.

## Scaffold

- Optimize `.mvn/settings.xml`:
  - Add profile `repo-central`.
  - In profile `default`, add `setting.docker.build.namespace`.
- Add support for Writerside and mdBook to manage project documentation.
- Fix `layers.xml`, `Dockerfile`, and `build.sh`.
- Fix Liquibase plugin properties file.
- Add `application-uat.yaml`.
- Optimize `docker.build.image_name`.
- Optimize dev-ops:
  - Rename `COMPOSE_PROJECT_NAME` and network.
  - Add scripts `up.sh`, `down.sh`, and `ps.sh`.
- Fix `build.sh` and `start.sh`.
- Enable probe endpoints in `application-actuator.yml`.
- Fix Dockerfile and Spring Boot `entrypoint.sh`.
- Optimize `.gitlab-ci.yml`.
