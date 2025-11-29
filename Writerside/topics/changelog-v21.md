# v21

## Breaking changes

- Refactor `radp-logging-spring-boot-starter` to provide clearer and more extensible logging.

## Features

- Add Redis key management utilities to standardize key creation and validation.
- Add a comprehensive testing support module in `radp-spring-test`.

## Bug fixes

- Fix `class file for edu.umd.cs.findbugs.annotations.SuppressFBWarnings not found`.
- Fix and optimize `TtlThreadPoolTaskExecutor` and `ExceptionHandlingAsyncTaskExecutor`.
- Fix error handling in embedded servers.
- Fix `Unable to find a URL to the parent project. The parent menu will NOT be added.`.
- Resolve transitive dependency issues.

## Refactor

- Relocate `ResponseBuilder` to the DTO package.
- Refactor `LocalCallFirstCluster` and `DubboExceptionFilter` to reduce complexity and improve readability.
- Optimize `RedissonService` to use non-deprecated `setNx` methods.

## Tests

- Add modules:
  - `radp-smoke-tests-redis`
  - `radp-smoke-tests-logging`
  - `radp-smoke-tests-test`
- Add tests for Testcontainers and embedded servers.
- Add `RedissonServiceTest` for `radp-redis-spring-boot-starter`.

## Scaffold

- Update `application-logback.yaml` and `logback-test.xml`.
- Add `RedisKeyProvider` enum.
- Relocate assert classes to a new package.
- Optimize `.gitignore`, `.gitattributes`, `.gitlab-ci.yml`.
- Add `.idea` settings for copyright and scope.
- Change license from GNU GPLv3 to Apache 2.0.
- Optimize CheckStyle-IDEA plugin configuration and IDEA CodeStyle.
