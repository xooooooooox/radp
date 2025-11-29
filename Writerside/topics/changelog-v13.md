# v13

## Scaffold

- Add PostgreSQL template `application.yaml`.
- Add properties `docker.build.base_image` and `docker.build.image_tag`.
- Improve `.gitignore`.
- Fix `maven-release-plugin` SCM configuration.
- Optimize `docker-compose-app.yaml`.
- Adjust Hikari logging configuration in `application-local.yaml` and `application-dev.yaml`.
- Remove `application-homelab.yaml`.
- Optimize actuator configuration.
- Add `project.name`.
- Fix assembly JAR profiles activation by CLI.
- Include `bin/catalina.sh`, `bin/catalina.bat`, `bin/startup.sh`, and `bin/shutdown.sh` in assembly.

## Dependencies

- Replace `pl.project13.maven:git-commit-id-plugin` with `io.github.git-commit-id:git-commit-id-maven-plugin`.
- Upgrade `commons-io:commons-io` to `2.13.0`.
- Upgrade `springdoc-openapi` to `2.4.0`.
- Upgrade MyBatis-Plus and `mybatis-spring-boot`, and use the MyBatis-Plus BOM.
- Upgrade Spring Cloud to a newer compatible release.

## Features

- Add `MultiResult` to `radp-spring-framework`.
- In `radp-logging-spring-boot-starter` logback template, add `NopStatusListener`.
- In `ResponseBuilder`, add `Result buildFailure(ErrorCode errorCode)`.
- In `SingleResult`, add `build()` method.
- Optimize profiles `auto-layered` and `auto-assembly`.

## Fixes

- Fix missing `PaginationInnerInterceptor` by explicitly declaring `mybatis-plus-jsqlparser`.
- Fix CI/CD pipelines after upgrading the stack.
- Fix `ResponseBuilder` bug in `radp-spring-framework`.
- Fix internal SPI registration under `META-INF`.
- Fix `ApplicationContextHelper#getBean`.
- Fix `RestExceptionHandler`.

