# v14

## Features

- Remove module `radp-tomcat-spring-boot-starter`.
- Rename `radp-swagger3-spring-boot-starter` to `radp-springdoc-webmvc-spring-boot-starter`.
- Add module `radp-springdoc-webflux-spring-boot-starter`.
- Disable auto-configuration:
  - `BootstrapLogAutoConfiguration`
  - `AccessLogAutoConfiguration`
  - `WebAPIAutoConfiguration`
- Enable `AsyncTaskExecutionAutoConfiguration`.

## Fixes

- Resolve `AsyncTaskExecutionAutoConfiguration` deprecation warning involving `TaskExecutorBuilder`.

## Dependencies

- Optimize plugin management:
  - Use `radp-dependencies` to manage Maven plugin versions.
  - Use `radp-parent` to manage plugin configuration.

## Build

- Add properties:
  - `java.version`
  - `maven.compiler.source`
  - `maven.compiler.target`
  - `project.build.sourceEncoding`
  - and related defaults.

## Documentation

- Update Writerside `about.md`.
