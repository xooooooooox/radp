# ChangeLog

## 3.21

### Break Changes

- Refactor module `radp-logging-spring-boot-starter`. Improved logging clarity and extensibility for various use cases.

### Features

- Add Redis key management utilities
  - Introduced RedisKeyConstants for standardized key creation.
  - Added RedisKeyProvider interface for modular key management.
- Add a comprehensive testing framework in `radp-spring-test`.
  - Implemented embedded server support for Redis, Zookeeper, and Kafka.
  - Added SPI extension mechanism for embedded server.

### Bug Fixes

- Fix `class file for edu.umd.cs.findbugs.annotations.SuppressFBWarnings not found`.
- Fix and optimize `TtlThreadPoolTaskExecutor` and `ExceptionHandlingAsyncTaskExecutor`.
- Fix error handling in embedded servers
  - Improved exception handling in EmbeddedRedisServer, EmbeddedZookeeperServer,
    EmbeddedKafkaServer.

### Chore

- dependencies
  - Upgrade `org.springframework.boot:spring-boot-starter-parent` from `3.4.4` to `3.4.5`.
  - Upgrade `testcontainers.version` from `1.17.6` to `1.21.0`.
  - DependencyManagement Add `com.redis:testcontainers-redis:2.2.2`.
  - Override `kafka.version` from `3.8.1` to `3.9.0`. Resolve
    `WARNING: Discovered 3 'junit-platform.properties' configuration files on the classpath`.
  - Optimize dependency to resolve module cycles.
  - Remove unused `mongodb.version` property.
  - Exclude `spring-boot-starter-loggin` in `radp-spring-boot-test`.
  - Remove unused radp-spring-test dependency from radp-spring-framework.
  - Remove redundant dependency from `radp-integration-test`.
- parent
  - Remove redundant profile `code-review`.
- build
  - Remove the `maven.install.skip` property from the radp-smoke-tests-archetype-xx
    module.
- scaffold
  - Update scaffold default radpVersion to `3.21`
  - Update `application-logback.yaml` and `logback-test.xml`
  - Update `.gitlab-ci.yml`
  - Add `.idea`
  - Add `RedisKeyProvider` enum
  - Optimize `.gitignore`, `.gitattributes`
  - Relocate assert classes to a new package
- malicious
  - Switch from GNU GPLv3 to Apache 2.0
  - Optimize .gitignore
  - Add IDE config for copyright and scope settings

### Refactor

- Relocate ResponseBuilder to dto package
- Refactor `LocalCallFirstCluster` and `DubboExceptionFilter`
  - reduce method complexity
  - refactor the code to improve readability and maintainability
  - refactor any remaining issues with deprecated methods
- Optimize `RedissonService`
  - Update the `setNx` methods in RedissonService.java to use the non-deprecated
    alternatives

### Test

- Add module `radp-smoke-tests-redis`,
- Add module`radp-smoke-tests-logging`
- Add module `radp-smoke-tests-test`
  - Add test cases for TestContainers
  - Add test cases for EmbeddedServers
- Add RedissonServiceTest for radp-redis-spring-boot-starter

### Documentation

## 3.20.2

### fix

- Fix `BaseException` to properly set the cause when a `Throwable` is passed as the last
  parameter in varargs
- Fix `BaseException` to handle placeholder mismatches when a `Throwable` is passed as a
  parameter
- Add `ErrorCodeLoader.getErrMessage(String errCode)` method to get the raw message
  template without placeholder
  replacement
- Remove deprecated ListenableFuture methods in `TtlThreadPoolTaskExecutor`
- Add `serialVersionUID` to improve serialization consistency
- Use a proper constructor method for adaptive extension instantiation
- Simplify instance checks in `ExceptionHandlingAsyncTaskExecutor`
- Fix `ExtensionLoader`, replace deprecated newInstance usage
- Fix `AccessLogConfiguration` NPE

### chore

- parent
  - Optimize profile `auto-update-local-catalog` for disable default excludes
  - Fix profile `code-review`, `unit-test`, `integration-test`
  - Add profile `aggregate-reports`
    - Integrate sonarqube
    - Added `maven-jxr-plugin` for cross-referencing source code
    - Configure `maven-site-plugin` and `project-info-reports-plugin` for reports
- dependencies
  - Upgrade `maven-wrapper-plugin.version` from `3.2.0` to `3.3.2`
  - Upgrade `maven-archetype-plugin.version` from `3.2.0` to `3.3.1`
  - Upgrade `sonar-maven-plugin.version` from `3.9.1.2184` to `5.1.0.4751`
  - Upgrade `maven-surefire-plugin` from `3.0.0-M7` to `3.1.2`
  - Upgrade `maven-failsafe-plugin` from `3.0.0-M3` to `3.1.2`
  - Upgrade `org.jacoco:jacoco-maven-plugin` from `0.8.7` to `0.8.9`
  - DependencyManagement add dependency `com.google.code.findbugs:annotations:3.0.1`
  - PluginManagement add plugin `org.apache.maven.plugins:maven-jxr-plugin:3.3.0`
  - PluginManagement add plugin
    `org.apache.maven.plugins:maven-project-info-reports-plugin:3.6.2`
  - PluginManagement add plugin `org.apache.maven.plugins:maven-site-plugin:3.12.1`
  - PluginManagement add plugin `org.apache.maven.plugins:maven-checkstyle-plugin:3.3.1`
  - PluginManagement add plugin `org.apache.maven.plugins:archetype-packaging:3.2.0`
- scaffold
  - Update scaffold default radpVersion to `3.20.2`
  - Optimize `.mvn`
  - Add `.gitattributes`
  - Add default ApplicationTests
  - Add GitHub Issue Templates

### docs

- Add Javadoc and resolve Javadoc warnings

## 3.19.1

- fix
  - Fix ErrorCodeLoader
  - Fix `BaseException` message formatting with parameters
  - Fix `ExceptionUtils.clientException`, `ExceptionUtils.serverException` and
    `ExceptionUtils.thirdServiceException` message formatting issue with placeholders
  - Add unit test for `ExceptionUtils`, `ServerAssert`, `MessageFormatter`, etc.
  - Fix the default value for PageResult#total not work
- docs
  - Add GitHub Issue Templates
- scaffold
  - Update scaffold default radpVersion to `3.19.1`
  - Fix `XxxAssert` message formatting issue with placeholders

## 3.19

### feat

- Optimize Assert

### chore

- parent
  - Add profile `env-sit`,`o-all-env`
- dependencies
  - Upgrade jib-maven-plugin from 3.4.4 to 3.4.5
- scaffold
  - Update scaffold default radpVersion to `3.19`
  - Optimize build.sh
  - Optimize the pom.xml of scaffold-xx xx-types module
  - Optimize the pom.xml of scaffold-xx xx-app module
  - Fix entrypoint.sh `Unrecognized option: --spring.config.additional-location=`
  - Optimize application-local.yaml for exposure env endpoint and unrestricted loggers
    endpoint
  - Optimize dev-ops docker-compose-app.yaml

## 3.18.1

### chore

- dependencies
  - Override liquibase version to 4.31.1
- scaffold
  - Update scaffold default radpVersion to `3.18.1`
  - Optimize entrypoint.sh
  - Optimize liquibase
    - Fixed the issue with duplicate initialization caused by inconsistent filenames
      recognized in changesets
      - see <https://docs.liquibase.com/change-types/includeall.html>
      -
      see <https://docs.liquibase.com/start/release-notes/liquibase-release-notes/liquibase-4.31.1.html?utm_source=chatgpt.com>
    - Optimized changelog-init.yaml example, migration/20241018 directory structure, and
      multienvironment support

## 3.18

### chore

- dependencies
  - Upgrade `org.sonatype.central:central-publishing-maven-plugin` from `0.6.0` to `0.7.0`
  - Upgrade `org.springdoc:springdoc-openapi` from `2.4.0` to `2.8.6`
    - see https://github.com/springdoc/springdoc-openapi/issues/2687
- parent
  - Support for publishing snapshots to the central portal
  - Add profile `env.uat`
  - Optimize profile `repo-central`, add property `auto.layered.enabled` and
    `auto.assembly.enabled`
  - profile `coding` add property `user.docker.build.namespace`
- scaffold
  - Update scaffold default radpVersion to `3.18`
  - Optimize`.mvn/settings.xml`
    - add profile `repo-central`
    - profile `default` add property `setting.docker.build.namespace`
  - Support **writerside** and **mdbook** to manage project documentation
  - Fix `layers.xml`, `Dockerfile`, `build.sh`
  - Fix liquibase plugin properties file
  - Add `application-uat.yaml`
  - Optimize `docker.build.image_name`
  - Optimize dev-ops
    - Rename `COMPOSE_PROJECT_NAME` and network
    - Add up.sh, down.sh, ps.sh
  - Fix build.sh and start.sh
  - application-actuator.yml enable probe endpoint
  - Fix Dockerfile and springboot entrypoint.sh
  - Optimize .gitlab-ci.yml

## 3.17

### chore

- parent
  - 优化 profile `auto-jib`, 拆分两个 profile `auto-jib-buildTar` 以及
    `auto-jib-dockerBuild`, 解决 `jib:buildTar`
    不支持 multi platform 引起的构建失败问题
  - Add profile `o-release`, `o-tar`, `publish-harbor`
  - Add profile `publish-artifactory`
- scaffold
  - Update scaffold default radpVersion to `3.17`
  - Fix `.github/trigger-releases.yml`
  - Change the version of the generated project from `1.0.0-SNAPSHOT` to `1.0-SNAPSHOT`
  - Optimize dev-ops/app
  - Optimize `.mvn/settings.xml`

## 3.16.1

- fix
  - Fix when deploy to artifactory got ERROR _the parameters 'url' for wagon-maven-plugin
    are missing or invalid_
  - Fix GitHub Action got error _Unable to decrypt gpg passphrase_
- parent
  - Rename profile `auto-archetype-xx` to `auto-upload-catalog-xx`
- scaffold
  - Update [.mvn/settings.xml](.mvn/settings.xml)
  - Update scaffold default radpVersion to `3.16.1`

## 3.16

- feat
  - `radp-jasypt-spring-boot-starter` add Test
- fix
  - Fix `logging.pattern.console` not work
- parent
  - Optimize profile `auto-jib`
  - Optimize archetype-catalog.xml deploy to self-hosted artifactory
- dependencies
  - pluginManagement add `maven-resources-plugin`
  - pluginManagement add `maven-enforcer-plugin`
  - pluginManagement add `wagon-maven-plugin`
- scaffold
  - Delete `JasyptTest`, Add blank Junit5 `ApiTest`
  - Optimize `application.yaml`, add `application-jasypt.yaml`
  - Update scaffold default radpVersion to `3.15.1`
  - Update `application-logback.yaml`
  - Optimize application.yaml, add application-webmvc.yaml
  - .mvn/settings.xml Add properties `auto.archetype.catalog.artifactory`
- writerside
  -
  Update [1.1.1-use_archetype_create_project.md](Writerside/topics/1.1.1-use_archetype_create_project.md)

## 3.15.1

- dependencies
  - Upgrade `commons-io:commons-io` from `2.13.0` to `2.17.0` to ensure compatibility with
    `org.apache.tika:tika-core`
- scaffold
  - dev-ops add `docker-compose-pgadmin.yaml` and `docker-compose-redis-commander.yaml`
  - Update scaffold default radpVersion to `3.15.1`
  - Fix scaffold dev-ops

## 3.15

- feature
  - `radp-common` add `FileUtils`
- dependencies
  - Upgrade the spring boot version from `3.2.3` to `3.4.4`
  - Upgrade the spring cloud version from `2023.0.0` to `2024.0.0`
  - dependencyManagement add `central-publishing-maven-plugin:0.6.0`,
    `maven-javadoc-plugin:3.5.0`
  - `space.x9x.radp:radp` 沿用 `radp-dependenncies` 中声明的 `maven-deploy-plugin` 而不是
    `spring-boot-dependencies` 中声明的 `3.10.1` 版本
  - Fix dependencyManagement for `org.apache.dubbo.dubbo-dependencies-zookeeper`
  - Change dependencyManagement
    `com.github.xingfudeshi:knife4j-openapi3-jakarta-spring-boot-starter:4.1.0` to
    `com.github.xiaoming:knife4j-openapi3-jakarta-spring-boot-starter:4.1.0`
  - Remove the redundant plugin version in `radp-parent`
  - Remove duplicate plugin in `radp-depdencies`
  - 修复由于依赖传递的问题, 导致的 spring framework 版本被降级的问题
- parent
  - add properties `app.build.base_image.jdk8`, `app.build.base_image.jdk11`,
    `app.build.base_image.jdk17`
- scaffold
  - Optimize dev-ops
  - Fix application-dev.yaml
  - Optimize docker build base image
  - Update scaffold default radpVersion to 3.15

## 3.14

- feature
  - Remove module `radp-tomcat-spring-boot-starter`
  - Rename module `radp-swagger3-spring-boot-starter` to
    `radp-springdoc-webmvc-spring-boot-starter`
  - Add module `radp-springdoc-webflux-spring-boot-starter`
  - Disable autoconfiguration `BootstrapLogAutoConfiguration`,
    `AccessLogAutoConfiguration`,
    `WebAPIAutoConfiguration`
  - Enable autoconfiguration `AsyncTaskExecutionAutoConfiguration`
- fix
  - Resolve `AsyncTaskExecutionAutoConfiguration` problem:
    `org.springframework.boot.task.TaskExecutorBuilder' is deprecated since version 3.2.0 and marked for removal`
- dependencies
  - Optimize pluginManagement. Use radp-dependencies manage maven plugin version, use
    radp-parent manage plugin
    configuration
  - Change `com.github.xiaoymin:knife4j-openapi3-spring-boot-starter:4.1.0` to
    `com.github.xingfudeshi:knife4j-openapi3-jakarta-spring-boot-starter:4.1.0`
- parent
  - `radp-parent` add properties `java.version`, `maven.compiler.source`,
    `maven.compiler.target`,
    `project.build.sourceEncoding` 等
- doc
  - writerside update `about.md`

## 3.13

- scaffold
  - add postgresql template application.yaml
  - add property `docker.build.base_image` and `docker.build.image_tag`
  - optimise .gitignore
  - fix maven-release-plugin scm
  - Dockerfile base image `eclipse-temurin:11-jdk` -> `eclipse-temurin:17-jdk`
  - optimize docker-compose-app.yaml
  - application-local.yaml and application-dev.yaml hikari log
  - delete application-homelab.yaml
  - optimize actuator
  - add project.name
  - optimize assembly jar profiles active by cli not work
  - assembly bin/catalina.sh, bin/catalina.bat, bin/startup.sh, bin/shutdown.sh
- dependencies
  - `pl.project13.maven:git-commit-id-plugin:4.9.10` ->
    `io.github.git-commit-id:git-commit-id-maven-plugin:6.0.0`
  - upgrade `commons-io:commons-io:2.7` to `commons-io:commons-io:2.13.0`
  - upgrade springdoc-openapi version from 1.6.15 to 2.4.0
  - upgrade `com.baomidou:mybatis-plus-boot-starter:2.5.7` to
    `com.baomidou:mybatis-plus-spring-boot3-starter:2.5.7`
  - use springdoc-openapi bom
  - upgrade the spring-cloud version from `2021.0.5` to `2023.0.0`
  - upgrade mybatis-spring-boot version from `2.1.4` to `3.0.4`
  - upgrade the mybatis-plus version from `3.5.7` to `3.5.9`, use mybatis-plus-bom instead
- feature
  - `radp-spring-framework` add `MultiResult`
  - `radp-logging-spring-boot-starter` template/logback-spring.xml 增加
    `NopStatusListener`
  - `ResponseBuilder` add method signature `Result buildFailure(ErrorCode errorCode)`
  - optimize profile `auto-layered` and `auto-assembly`
- fix
  - fix can't find symbol `PaginationInnerInterceptor`, 需要显式声明
    `mybatis-plus-jsqlparser`
  - fix GitLab CI/CD after upgrade to Spring Boot 3 (JDK17)
  - fix GitHub Actions after upgrade to Spring Boot 3 (JDK17)
  - fix `radp-spring-framework` ResponseBuilder bug
  - fix META-INF/internal resource SPI
  - fix ApplicationContextHelper#getBean bug
  - fix `RestExceptionHandler`

## 3.12

- 基于 JDK17 + SpringBoot3.x
- netty-resolver-dns-native-macos 增加 arm64 支持
- `javax.validate:validation-api` -> `jakarta.validate:jakarta.validation-api`
- `javax.servlet.*` -> `jakarta.servlet.*`
- 允许在 `future` 分支出发 GitHub Actions

## 2.12

- 从这个版本开始, 新增 future 分支, 基于 JDK17+SpringBoot3.x 进行开发

## 0.11

- fix archetype auto-release profile does not work
- fix archetype import error

## 0.10

- Fix GitHub Action maven cache not work
- Change default jdk from `java 8.0.432+6-tem` to `java 8.0.442+6-amzn`.
  Because no available `java 8.0.432+6-tem` on arm64
- Update archetype application-dev.yaml

## 0.9

- Optimize GitHub Action: maven cache
- Optimize auto-assembly profile

## 0.8

- Update archetype
- Optimize GitHub Actions
- 新增 `rapd-design-pattern-framework`, 对设计模式的使用进行抽象和封装,
  降低设计模式的应用难度以及统一编码风格
  - 决策树设计模式抽象
- archetype
  - assembly 优化
  - 分层构建优化

## 0.7

- Support for publishing GitLab pages via GitLab CI/CD
- Optimize gitlab ci pipeline, use remote template
- Update archetype, `.mvn`, `.github` actions

## 0.6

- Support GitLab CI/CD
- Provide ubuntu/24.04 Vagrantfile for mac arm64

## 0.5

- dependency management shiro and jwt version
- add Utility classes for base64 and digest
- ci/cd supported. Publish packages to GitHub packages and maven central portal

## 0.3

- Add Utility classes for regex `RegexUtils`, ID generation `NanoIdGenerator`
  `SnowflakeGenerator`

## 0.2

### features

- 优化分页查询

### fix

- refine archetype includes and removes redundant entries

## 0.1.3

- 支持发布构件到 central, GitHub packages, self-hosted artifactory
- Use GitHub Actions to build and publish GitHub Pages
- DDD 脚手架
- 基础通用组件
