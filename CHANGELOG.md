# ChangeLog

## 2.19

- feat
  - [x] Optimize Assert
- parent
  - [x] Add profile `env-sit`
  - [x] Add profile `o-all-env`
- dependencies
  - [x] Upgrade `com.google.cloud.tools:jib-maven-plugin` from 3.4.4 to 3.4.5
  - [x] Upgrade `org.yaml:snakeyaml` from 1.30 to 2.3
- scaffold
  - [x] Update scaffold default radpVersion to `2.19`
  - [x] Optimize build.sh
  - [x] Optimize the pom.xml of scaffold-xx xx-types module 
  - [x] Optimize the pom.xml of scaffold-xx xx-app module
  - [x] Fix entrypoint.sh

## 2.18.1

- dependencies
  - [x] Override liquibase.version to 4.31.1
  - [x] Override commons-lang3.version to 3.17.0
- scaffold
  - [x] Update scaffold default radpVersion to `2.18.1`
  - [x] Optimize entrypoint.sh
  - [x] Optimize liquibase
    - Fixed issue with duplicate initialization caused by inconsistent filenames recognized in changesets
      - see <https://docs.liquibase.com/change-types/includeall.html>
      - see <https://docs.liquibase.com/start/release-notes/liquibase-release-notes/liquibase-4.31.1.html?utm_source=chatgpt.com>
    - Optimized changelog-init.yaml example, migration/20241018 directory structure, and multienvironment support

## 2.18

- dependencies
  - [x] Upgrade `org.sonatype.central:central-publishing-maven-plugin` from `0.6.0` to `0.7.0`
- parent
  - [x] Supports publishing snapshots to the central portal
  - [x] Add profile `env.uat`
  - [x] Optimize profile `repo-central`, add property `auto.layered.enabled` and `auto.assembly.enabled`
  - [x] profile `coding` add property `user.docker.build.namespace`
- scaffold
  - [x] Update scaffold default radpVersion to `2.18`
  - [x] Optimize`.mvn/settings.xml` 
    - add profile `repo-central`
    - profile `default` add property `setting.docker.build.namespace`
  - [x] Support writerside and mdbook to manage project documentation
  - [x] Fix `layers.xml`, `Dockerfile`, `build.sh`
  - [x] Fix liquibase plugin properties file
  - [x] Add `application-uat.yaml`
  - [x] Optimize `docker.build.image_name`
  - [x] Optimize dev-ops
    - Rename `COMPOSE_PROJECT_NAME` and network
    - Add up.sh, down.sh, ps.sh  
  - [x] Fix build.sh and start.sh
  - [x] Fix Dockerfile and entrypoint.sh
  - [x] application-actuator.yml enable probe endpoint
  - [x] Optimize .gitlab-ci.yml

## 2.17

- parent
    - [x] 优化 profile `auto-jib`, 拆分两个 profile `auto-jib-buildTar` 以及 `auto-jib-dockerBuild`, 解决 `jib:buildTar`
      不支持 multi platform 引起的构建失败问题
    - [x] Add profile `o-release`, `o-tar`, `publish-harbor`
    - [x] Add profile `publish-artifactory`
- scaffold
    - [x] Update scaffold default radpVersion to `2.17`
    - [x] Fix .github/trigger-releases.yml
    - [X] Change generated project version from `1.0.0-SNAPSHOT` to `1.0-SNAPSHOT`
    - [x] Optimize `.mvn/settings.xml`
    - [x] Optimize dev-ops/app

## 2.16.1

- fix
    - [x] Fix when deploy to artifactory got ERROR _the parameters 'url' for wagon-maven-plugin are missing or invalid_
    - [x] Fix GitHub Action got error Unable to decrypt gpg passphrase
- parent
    - [x] Rename profile `auto-archetype-xx` to `auto-upload-catalog-xx`
- scaffold
    - [x] Update `.mvn/settings.xml`
    - [x] Update scaffold default radpVersion to `2.16.1`

## 2.16

- feat
    - [x] `radp-jasypt-spring-boot-starter` add Test
- fix
    - [x] Fix `logging.pattern.console` not work
- parent
    - [x] Optimize profile `auto-jib`
    - [x] pluginManagement add `maven-enforcer-plugin`
    - [x] Optimize archetype-catalog.xml deploy to self-hosted artifactory
- dependencies
    - [x] pluginManagement add `maven-resources-plugin`
- scaffold
    - [x] Delete `JasyptTest`, Add blank Junit5 `ApiTest`
    - [x] Optimize `application.yaml`, add `application-jasypt.yaml`
    - [x] Update scaffold default radpVersion to `2.16`
    - [x] Update `application-logback.yaml`
    - [x] Optimize application.yaml, add application-webmvc.yaml
- writerside
    - [x] Update [1.1.1-use_archetype_create_project.md](Writerside/topics/1.1.1-use_archetype_create_project.md)

## 2.15.1

- scaffold
    - [x] fix scaffold dev-ops
    - [x] Update scaffold default radpVersion to `2.15.1`

## 2.15

- feature
    - [x] `radp-common` add `FileUtils`
- dependencies
    - [x] dependencyManagement add `central-publishing-maven-plugin:0.6.0`, `maven-javadoc-plugin:3.5.0`
    - [x] `space.x9x.radp:radp` 沿用 `radp-dependenncies` 中声明的 `maven-deploy-plugin` 而不是
      `spring-boot-dependencies` 中声明的版本
    - [x] Remove redundant plugin version in `radp-parent`
    - [x] 修复由于依赖传递的问题, 导致的 spring framework 版本被降级的问题
    - [x] Fix dependencyManagement for `org.apache.dubbo.dubbo-dependencies-zookeeper`
- parent
    - [x] add properties `app.build.base_image.jdk8`, `app.build.base_image.jdk11`, `app.build.base_image.jdk17`
- scaffold
    - [x] Optimize dev-ops
    - [x] Optimize docker build base image
    - [x] Update scaffold default radpVersion to 2.15

## 2.14

- feature
    - [x] Remove module `radp-tomcat-spring-boot-starter`
    - [x] Rename module `radp-swagger3-spring-boot-starter` to `radp-springdoc-webmvc-spring-boot-starter`
    - [x] Add module `radp-springdoc-webflux-spring-boot-starter`
    - [x] Disable autoconfiguration `BootstrapLogAutoConfiguration`, `AccessLogAutoConfiguration`,
      `WebAPIAutoConfiguration`
    - [x] Enable autoconfiguration `AsyncTaskExecutionAutoConfiguration`
- dependencies
    - [x] Optimize pluginManagement. Use radp-dependencies manage maven plugin version, use radp-parent manage plugin
      configuration
    - [x] Optimize radp-dependencies, use `springdoc-openapi` bom instead`
- parent
    - [x] `radp-parent` add properties `java.version`, `maven.compiler.source`, `maven.compiler.target`,
      `project.build.sourceEncoding` 等
- doc
    - [x] writerside update `about.md`
- scaffold

## 2.13

- scaffold
    - [X] optimise .gitignore
    - [x] fix maven-release-plugin scm
    - [x] optimize actuator
    - [x] assembly bin/catalina.sh, bin/catalina.bat, bin/startup.sh, bin/shutdown.sh
    - [x] optimize assembly jar profiles active by cli not work
    - [x] optimize application-xx.yaml
    - [x] optimize gitlab CI/CD
- dependencies
    - [x] `radp-spring-framework` change `javax.validation:validation-api` to `jakarta.validation:validatation-api`
    - [x] upgrade spring-boot-parent from `2.7.12` to `2.7.18`
- feature
    - [x] optimize profile `auto-layered` and `auto-assembly`
    - [x] `RestExceptionHandler` add handler `MethodArgumentTypeMismatchException`
    - [x]  add `MultiResult`
    - [x] `ResultBuilder` add method signature ` buildFailure(ErrorCode errorCode)`
    - [x] `SingleResult` add method signature `build()`
    - [x] Optimize gitlab CI/CD
    - [x] `radp-logging-spring-boot-starter` template 增加 `NopStatusListener`
- fix
    - [x] fix META-INF/internal resource SPI
    - [x] fix `radp-spring-framework` ResponseBuilder bug
    - [x] fix `ApplicationContextHelper#getBean` bug

## 2.12

- [x] 从这个版本开始, 新增 future 分支, 基于 JDK17+SpringBoot3.x 进行开发

## 0.11

- [x] fix archetype auto-release profile not work
- [x] fix archetype import error

## 0.10

- [x] Fix GitHub Action maven cache not work
- [x] Change default jdk from `java 8.0.432+6-tem` to `java 8.0.442+6-amzn`. Because no available `java 8.0.432+6-tem`
  on arm64
- [x] Update archetype application-dev.yaml

## 0.9

- [x] Optimize GitHub Action: maven cache
- [x] Optimize auto-assembly profile

## 0.8

- [x] Update archetype
- [x] Optimize GitHub Actions
- [x] 新增 `rapd-design-pattern-framework`, 对设计模式的使用进行抽象和封装, 降低设计模式的应用难度以及统一编码风格
    - 决策树设计模式抽象
- [x] archetype
    - assembly 优化
    - 分层构建优化

## 0.7

- [x] Support publish GitLab pages via GitLab CI/CD
- [x] Optimize gitlab ci pipeline, use remote template
- [x] Update archetype, .mvn, .github actions

## 0.6

- [x] Support GitLab CI/CD
- [x] Provide ubuntu/24.04 Vagrantfile for mac arm64

## 0.5

- [x] dependency management shiro and jwt version
- [x] add Utility classes for base64 and digest
- [x] ci/cd supported. Publish packages to github packages and maven central portal

## 0.3

- [x] Add Utility classes for regex `RegexUtils`, ID generation `NanoIdGenerator` `SnowflakeGenerator`

## 0.2

### features

- [x] 优化分页查询

### fix

- [x] refine archetype includes and remove redundant entries

## 0.1.3

- [x] 支持发布构件到 central, GitHub packages, self-hosted artifactory
- [x] Use GitHUb Actions build and publish GitHub Pages
- [x] DDD 脚手架
- [x] 基础通用组件