# v15

## Features

- Add `FileUtils` to `radp-common`.

## Dependencies

- In `DependencyManagement`, add:
  - `central-publishing-maven-plugin:0.6.0`
  - `maven-javadoc-plugin:3.5.0`
- Keep `space.x9x.radp:radp` using the `maven-deploy-plugin` defined in `radp-dependencies` instead of the one from
  `spring-boot-dependencies`.
- Fix `org.apache.dubbo:dubbo-dependencies-zookeeper`.
- Change Knife4j starter coordinates to `com.github.xiaoming:knife4j-openapi3-jakarta-spring-boot-starter:4.1.0`.
- Remove redundant plugin versions from `radp-parent` and duplicate plugin definitions from `radp-dependencies`.
- Fix an issue where transitive dependencies downgraded the Spring Framework version.

## Parent

- Add properties:
  - `app.build.base_image.jdk8`
  - `app.build.base_image.jdk11`
  - `app.build.base_image.jdk17`

## Scaffold

- Optimize dev-ops.
- Optimize Docker base image configuration.
- Fix `application-dev.yaml`.

