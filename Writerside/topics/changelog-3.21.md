# 3.21

- Dependencies:
  - Upgrade Spring Boot parent from `3.4.4` to `3.4.5`.
  - Upgrade `testcontainers.version` from `1.17.6` to `1.21.0`.
  - Upgrade `com.github.codemonstur:embedded-redis` from `0.11.0` to `1.4.3`.
  - Add `com.redis:testcontainers-redis:2.2.2`.
  - Override `kafka.version` from `3.8.1` to `3.9.0` to consolidate `junit-platform.properties`.
  - Clean up unused properties and redundant dependencies.
- Build:
  - Add `io.spring.javaformat:spring-javaformat-maven-plugin:0.0.45` to `PluginManagement`.
  - Move `git-commit-id-maven-plugin` and `versions-maven-plugin` to the root POM.
  - Optimize profiles `code-review`, `unit-test`, and `aggregate-reports`.
  - Fix missing `relativePath` entries.
  - Optimize `radp-smoke-tests-archetype`.
- Scaffold default `radpVersion` is `3.21`.
