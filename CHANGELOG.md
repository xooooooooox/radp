# ChangeLog

## 2.20.2

### fix

- Fix `BaseException` to properly set the cause when a `Throwable` is passed as the last parameter in varargs
- Fix `BaseException` to handle placeholder mismatches when a `Throwable` is passed as a parameter
- Add `ErrorCodeLoader.getErrMessage(String errCode)` method to get the raw message template without placeholder
  replacement
- Fix `BootstrapLogConfiguration`
- Fix `AccessLogConfiguration` NPE

### chore

- parent
  - Optimize profile `auto-update-local-catalog` for disable default excludes
  - Optimize and fix profile `core-review`, `unit-test`, `integration-test`
  - Add profile `aggregate-reports`
    - Add `maven-jxr-plugin` for cross-referencing source code
    - Configure `maven-site-plugin` and `project-info-reports-plugin` for reports
- dependencies
  - Upgrade `maven-wrapper-plugin.version` from `3.2.0` to `3.3.2`
  - Upgrade `maven-resources-plugin.version` from `3.2.0` to `3.3.1`
  - Upgrade `maven-archetype-plugin.version` from `3.2.0` to `3.3.1`
  - Upgrade `maven-surefire-plugin` from `3.0.0-M7` to `3.1.2`
  - Upgrade `maven-failsafe-plugin` from `3.0.0-M3` to `3.1.2`
  - Upgrade `org.jacoco:jacoco-maven-plugin` from `0.8.7` to `0.8.9`
  - DependencyManagement add dependency `com.google.code.findbugs:annotations:3.0.1`
  - PluginManagement add plugin `org.apache.maven.plugins:maven-jxr-plugin:3.3.0`
  - PluginManagement add plugin `org.apache.maven.plugins:maven-project-info-reports-plugin:3.6.2`
  - PluginManagement add plugin `org.apache.maven.plugins:maven-site-plugin:3.12.1`
  - PluginManagement add plugin `org.apache.maven.plugins:maven-checkstyle-plugin:3.3.1`
  - PluginManagement add plugin org.apache.maven.plugins:archetype-packaging:3.2.0
- scaffold
  - Update scaffold default radpVersion to `2.20`
  - Optimize `.mvn`
  - Add `.gitattributes`
  - Add default ApplicationTests
  - Add GitHub Issue Templates

### docs

- Add Javadoc and resolve Javadoc warnings

## 2.19.1

- fix
  - Fix ErrorCodeLoader
  - Fix `BaseException` message formatting with parameters
  - Fix `ExceptionUtils.clientException`, `ExceptionUtils.serverException` and
    `ExceptionUtils.thirdServiceException` message formatting issue with placeholders
  - Add unit test for `ExceptionUtils`, `ServerAssert`, `MessageFormatter`, etc.
- docs
  - Add GitHub Issue Templates
- scaffold
  - Update scaffold default radpVersion to `2.19.1`
  - Fix `XxxAssert` message formatting issue with placeholders

## 2.19

- feat
  - Optimize Assert
- parent
  - Add profile `env-sit`
  - Add profile `o-all-env`
- dependencies
  - Upgrade `com.google.cloud.tools:jib-maven-plugin` from 3.4.4 to 3.4.5
  - Upgrade `org.yaml:snakeyaml` from 1.30 to 2.3
- scaffold
  - Update scaffold default radpVersion to `2.19`
  - Optimize build.sh
  - Optimize the pom.xml of scaffold-xx xx-types module
  - Optimize the pom.xml of scaffold-xx xx-app module
  - Fix entrypoint.sh

## 2.18.1

- dependencies
  - Override `liquibase.version` to 4.31.1
  - Override `commons-lang3.version` to 3.17.0
- scaffold
  - Update scaffold default radpVersion to `2.18.1`
  - Optimize entrypoint.sh
  - Optimize liquibase
    - Fixed the issue with duplicate initialization caused by inconsistent filenames recognized in changesets
      - see <https://docs.liquibase.com/change-types/includeall.html>
      -
      see <https://docs.liquibase.com/start/release-notes/liquibase-release-notes/liquibase-4.31.1.html?utm_source=chatgpt.com>
    - Optimized changelog-init.yaml example, migration/20241018 directory structure, and multienvironment support

## 2.18

- dependencies
  - Upgrade `org.sonatype.central:central-publishing-maven-plugin` from `0.6.0` to `0.7.0`
- parent
  - Supports publishing snapshots to the central portal
  - Add profile `env.uat`
  - Optimize profile `repo-central`, add property `auto.layered.enabled` and `auto.assembly.enabled`
  - profile `coding` add property `user.docker.build.namespace`
- scaffold
  - Update scaffold default radpVersion to `2.18`
  - Optimize`.mvn/settings.xml`
    - add profile `repo-central`
    - profile `default` add property `setting.docker.build.namespace`
  - Support writerside and mdbook to manage project documentation
  - Fix `layers.xml`, `Dockerfile`, `build.sh`
  - Fix liquibase plugin properties file
  - Add `application-uat.yaml`
  - Optimize `docker.build.image_name`
  - Optimize dev-ops
    - Rename `COMPOSE_PROJECT_NAME` and network
    - Add up.sh, down.sh, ps.sh
  - Fix build.sh and start.sh
  - Fix Dockerfile and entrypoint.sh
  - application-actuator.yml enable probe endpoint
  - Optimize .gitlab-ci.yml

## 2.17

- parent
  - 优化 profile `auto-jib`, 拆分两个 profile `auto-jib-buildTar` 以及 `auto-jib-dockerBuild`, 解决 `jib:buildTar`
      不支持 multi platform 引起的构建失败问题
  - Add profile `o-release`, `o-tar`, `publish-harbor`
  - Add profile `publish-artifactory`
- scaffold
  - Update scaffold default radpVersion to `2.17`
  - Fix `.github/trigger-releases.yml`
  - Change generated a project version from `1.0.0-SNAPSHOT` to `1.0-SNAPSHOT`
    - Optimize `.mvn/settings.xml`
    - Optimize dev-ops/app

## 2.16.1

- fix
  - Fix when deploy to artifactory got ERROR _the parameters 'url' for wagon-maven-plugin are missing or invalid_
  - Fix GitHub Action got error Unable to decrypt gpg passphrase
- parent
  - Rename profile `auto-archetype-xx` to `auto-upload-catalog-xx`
- scaffold
  - Update `.mvn/settings.xml`
  - Update scaffold default radpVersion to `2.16.1`

## 2.16

- feat
  - `radp-jasypt-spring-boot-starter` add Test
- fix
  - Fix `logging.pattern.console` not work
- parent
  - Optimize profile `auto-jib`
  - pluginManagement add `maven-enforcer-plugin`
  - Optimize archetype-catalog.xml deploy to self-hosted artifactory
- dependencies
  - pluginManagement add `maven-resources-plugin`
- scaffold
  - Delete `JasyptTest`, Add blank Junit5 `ApiTest`
  - Optimize `application.yaml`, add `application-jasypt.yaml`
  - Update scaffold default radpVersion to `2.16`
  - Update `application-logback.yaml`
  - Optimize application.yaml, add application-webmvc.yaml
- writerside
  - Update [1.1.1-use_archetype_create_project.md](Writerside/topics/1.1.1-use_archetype_create_project.md)

## 2.15.1

- scaffold
  - fix scaffold dev-ops
  - Update scaffold default radpVersion to `2.15.1`

## 2.15

- feature
  - `radp-common` add `FileUtils`
- dependencies
  - dependencyManagement add `central-publishing-maven-plugin:0.6.0`, `maven-javadoc-plugin:3.5.0`
  - `space.x9x.radp:radp` 沿用 `radp-dependenncies` 中声明的 `maven-deploy-plugin` 而不是
      `spring-boot-dependencies` 中声明的版本
  - Remove a redundant plugin version in `radp-parent`
    - 修复由于依赖传递的问题, 导致的 spring framework 版本被降级的问题
    - Fix dependencyManagement for `org.apache.dubbo.dubbo-dependencies-zookeeper`
- parent
  - add properties `app.build.base_image.jdk8`, `app.build.base_image.jdk11`, `app.build.base_image.jdk17`
- scaffold
  - Optimize dev-ops
  - Optimize docker build base image
  - Update scaffold default radpVersion to 2.15

## 2.14

- feature
  - Remove module `radp-tomcat-spring-boot-starter`
  - Rename module `radp-swagger3-spring-boot-starter` to `radp-springdoc-webmvc-spring-boot-starter`
  - Add module `radp-springdoc-webflux-spring-boot-starter`
  - Disable autoconfiguration `BootstrapLogAutoConfiguration`, `AccessLogAutoConfiguration`,
      `WebAPIAutoConfiguration`
  - Enable autoconfiguration `AsyncTaskExecutionAutoConfiguration`
- dependencies
  - Optimize pluginManagement. Use radp-dependencies manage maven plugin version, use radp-parent manage plugin
      configuration
  - Optimize radp-dependencies, use `springdoc-openapi` bom instead`
- parent
  - `radp-parent` add properties `java.version`, `maven.compiler.source`, `maven.compiler.target`,
      `project.build.sourceEncoding` 等
- doc
  - writerside update `about.md`
- scaffold

## 2.13

- scaffold
  - optimise .gitignore
  - fix maven-release-plugin scm
  - optimize actuator
  - assembly bin/catalina.sh, bin/catalina.bat, bin/startup.sh, bin/shutdown.sh
  - optimize assembly jar profiles active by cli not work
  - optimize application-xx.yaml
  - optimize gitlab CI/CD
- dependencies
  - `radp-spring-framework` change `javax.validation:validation-api` to `jakarta.validation:validatation-api`
  - upgrade spring-boot-parent from `2.7.12` to `2.7.18`
- feature
  - optimize profile `auto-layered` and `auto-assembly`
  - `RestExceptionHandler` add handler `MethodArgumentTypeMismatchException`
  - add `MultiResult`
  - `ResultBuilder` add method signature ` buildFailure(ErrorCode errorCode)`
  - `SingleResult` add method signature `build()`
  - Optimize gitlab CI/CD
  - `radp-logging-spring-boot-starter` template 增加 `NopStatusListener`
- fix
  - fix META-INF/internal resource SPI
  - fix `radp-spring-framework` ResponseBuilder bug
  - fix `ApplicationContextHelper#getBean` bug

## 2.12

- 从这个版本开始, 新增 future 分支, 基于 JDK17+SpringBoot3.x 进行开发

## 0.11

- fix archetype auto-release profile does not work
- fix archetype import error

## 0.10

- Fix GitHub Action maven cache not work
- Change default jdk from `java 8.0.432+6-tem` to `java 8.0.442+6-amzn`. Because no available `java 8.0.432+6-tem`
  on arm64
- Update archetype application-dev.yaml

## 0.9

- Optimize GitHub Action: maven cache
- Optimize auto-assembly profile

## 0.8

- Update archetype
- Optimize GitHub Actions
- 新增 `rapd-design-pattern-framework`, 对设计模式的使用进行抽象和封装, 降低设计模式的应用难度以及统一编码风格
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

- Add Utility classes for regex `RegexUtils`, ID generation `NanoIdGenerator` `SnowflakeGenerator`

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