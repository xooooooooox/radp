# v16

## Features

- Add tests for `radp-jasypt-spring-boot-starter`.

## Fixes

- Fix `logging.pattern.console` not taking effect.

## Chore — Parent / Dependencies

- Optimize profile `auto-jib`.
- Optimize archetype catalog deployment to a self-hosted Artifactory.
- In `PluginManagement`, add:
  - `maven-resources-plugin`
  - `maven-enforcer-plugin`

## Chore — Scaffold

- Remove `JasyptTest` and add a blank JUnit 5 `ApiTest`.
- Optimize `application.yaml` and add `application-jasypt.yaml`.
- Update `application-logback.yaml`.
- Optimize `application.yaml` and add `application-webmvc.yaml`.
- In `.mvn/settings.xml`, add `auto.archetype.catalog.artifactory`.

## Writerside

- Update `1.1.1-use_archetype_create_project.md`.



