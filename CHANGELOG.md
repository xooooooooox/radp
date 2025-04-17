# ChangeLog

## 3.18

- dependencies
  - [x] Upgrade `org.sonatype.central:central-publishing-maven-plugin` from `0.6.0` to `0.7.0` 
- parent
  - [x] Support publish snapshots to central-portal 
- scaffold
  - [x] Update scaffold default radpVersion to `3.18`
  - [x] `.mvn/settings.xml` add profile `repo-central`

## 3.17

- parent
  - [x] 优化 profile `auto-jib`, 拆分两个 profile `auto-jib-buildTar` 以及 `auto-jib-dockerBuild`, 解决 `jib:buildTar` 不支持 multi platform 引起的构建失败问题
  - [x] Add profile `o-release`, `o-tar`, `publish-harbor`
  - [x] Add profile `publish-artifactory`
- scaffold
  - [x] Update scaffold default radpVersion to `3.17`
  - [x] Fix .github/trigger-releases.yml
  - [X] Change generated project version from `1.0.0-SNAPSHOT` to `1.0-SNAPSHOT`
  - [x] Optimize dev-ops/app
  - [x] Optimize `.mvn/settings.xml`

## 3.16.1

- fix
    - [x] Fix when deploy to artifactory got ERROR _the parameters 'url' for wagon-maven-plugin are missing or invalid_
    - [x] Fix GitHub Action got error _Unable to decrypt gpg passphrase_
- parent
    - [x] Rename profile `auto-archetype-xx` to `auto-upload-catalog-xx`
- scaffold
    - [x] Update [.mvn/settings.xml](.mvn/settings.xml)
    - [x] Update scaffold default radpVersion to `3.16.1`

## 3.16

- feat
    - [x] `radp-jasypt-spring-boot-starter` add Test
- fix
    - [x] Fix `logging.pattern.console` not work
- parent
    - [x] Optimize profile `auto-jib`
    - [x] Optimize archetype-catalog.xml deploy to self-hosted artifactory
- dependencies
    - [x] pluginManagement add `maven-resources-plugin`
    - [x] pluginManagement add `maven-enforcer-plugin`
    - [x] pluginManagement add `wagon-maven-plugin`
- scaffold
    - [x] Delete `JasyptTest`, Add blank Junit5 `ApiTest`
    - [x] Optimize `application.yaml`, add `application-jasypt.yaml`
    - [x] Update scaffold default radpVersion to `3.15.1`
    - [x] Update `application-logback.yaml`
    - [x] Optimize application.yaml, add application-webmvc.yaml
    - [x] .mvn/settings.xml Add properties `auto.archetype.catalog.artifactory`
- writerside
    - [x] Update [1.1.1-use_archetype_create_project.md](Writerside/topics/1.1.1-use_archetype_create_project.md)

## 3.15.1

- dependencies
    - [x] Upgrade `commons-io:commons-io` from `2.13.0` to `2.17.0` to ensure compatibility with
      `org.apache.tika:tika-core`
- scaffold
    - [x] dev-ops add `docker-compose-pgadmin.yaml` and `docker-compose-redis-commander.yaml`
    - [x] Update scaffold default radpVersion to `3.15.1`
    - [x] Fix scaffold dev-ops

## 3.15

- feature
    - [x] `radp-common` add `FileUtils`
- dependencies
    - [x] Upgrade spring boot version from `3.2.3` to `3.4.4`
    - [x] Upgrade spring cloud version from `2023.0.0` to `2024.0.0`
    - [x] dependencyManagement add `central-publishing-maven-plugin:0.6.0`, `maven-javadoc-plugin:3.5.0`
    - [x] `space.x9x.radp:radp` 沿用 `radp-dependenncies` 中声明的 `maven-deploy-plugin` 而不是
      `spring-boot-dependencies` 中声明的 `3.10.1` 版本
    - [x] Fix dependencyManagement for `org.apache.dubbo.dubbo-dependencies-zookeeper`
    - [x] Change dependencyManagement `com.github.xingfudeshi:knife4j-openapi3-jakarta-spring-boot-starter:4.1.0` to
      `com.github.xiaoming:knife4j-openapi3-jakarta-spring-boot-starter:4.1.0`
    - [x] Remove redundant plugin version in `radp-parent`
    - [x] Remove duplicate plugin in `radp-depdencies`
    - [x] 修复由于依赖传递的问题, 导致的 spring framework 版本被降级的问题
- parent
    - [x] add properties `app.build.base_image.jdk8`, `app.build.base_image.jdk11`, `app.build.base_image.jdk17`
- scaffold
    - [x] Optimize dev-ops
    - [x] Fix application-dev.yaml
    - [x] Optimize docker build base image
    - [x] Update scaffold default radpVersion to 3.15

## 3.14

- feature
    - [x] Remove module `radp-tomcat-spring-boot-starter`
    - [x] Rename module `radp-swagger3-spring-boot-starter` to `radp-springdoc-webmvc-spring-boot-starter`
    - [x] Add module `radp-springdoc-webflux-spring-boot-starter`
    - [x] Disable autoconfiguration `BootstrapLogAutoConfiguration`, `AccessLogAutoConfiguration`,
      `WebAPIAutoConfiguration`
    - [x] Enable autoconfiguration `AsyncTaskExecutionAutoConfiguration`
- fix
    - [x] Resolve `AsyncTaskExecutionAutoConfiguration` problem: 'org.springframework.boot.task.TaskExecutorBuilder' is
      deprecated since version 3.2.0 and marked for removal
- dependencies
    - [x] Optimize pluginManagement. Use radp-dependencies manage maven plugin version, use radp-parent manage plugin
      configuration
    - [x] Change `com.github.xiaoymin:knife4j-openapi3-spring-boot-starter:4.1.0` to
      `com.github.xingfudeshi:knife4j-openapi3-jakarta-spring-boot-starter:4.1.0`
- parent
    - [x] `radp-parent` add properties `java.version`, `maven.compiler.source`, `maven.compiler.target`,
      `project.build.sourceEncoding` 等
- doc
    - [x] writerside update `about.md`

## 3.13

- scaffold
    - [x] add postgresql template application.yaml
    - [x] add property `docker.build.base_image` and `docker.build.image_tag`
    - [X] optimise .gitignore
    - [x] fix maven-release-plugin scm
    - [x] Dockerfile base image `eclipse-temurin:11-jdk` -> `eclipse-temurin:17-jdk`
    - [x] optimize docker-compose-app.yaml
    - [x] application-local.yaml and application-dev.yaml hikari log
    - [x] delete application-homelab.yaml
    - [x] optimize actuator
    - [x] add project.name
    - [x] optimize assembly jar profiles active by cli not work
    - [x] assembly bin/catalina.sh, bin/catalina.bat, bin/startup.sh, bin/shutdown.sh
- dependencies
    - [x] `pl.project13.maven:git-commit-id-plugin:4.9.10` -> `io.github.git-commit-id:git-commit-id-maven-plugin:6.0.0`
    - [x] upgrade `commons-io:commons-io:2.7` to `commons-io:commons-io:2.13.0`
    - [x] upgrade springdoc-openapi version from 1.6.15 to 2.4.0
    - [x] upgrade `com.baomidou:mybatis-plus-boot-starter:2.5.7` to
      `com.baomidou:mybatis-plus-spring-boot3-starter:2.5.7`
    - [x] use springdoc-openapi bom
    - [x] upgrade spring-cloud version from `2021.0.5` to `2023.0.0`
    - [x] upgrade mybatis-spring-boot version from `2.1.4` to `3.0.4`
    - [x] upgrade mybatis-plus version from `3.5.7` to `3.5.9`, use mybatis-plus-bom instead
- feature
    - [x] `radp-spring-framework` add `MultiResult`
    - [x] `radp-logging-spring-boot-starter` template/logback-spring.xml 增加 `NopStatusListener`
    - [x] `ResponseBuilder` add method signature `Result buildFailure(ErrorCode errorCode)`
    - [x] optimize profile `auto-layered` and `auto-assembly`
- fix
    - [x] fix can't find symbol `PaginationInnerInterceptor`, 需要显式声明 `mybatis-plus-jsqlparser`
    - [x] fix GitLab CI/CD after upgrade to Spring Boot 3 (JDK17)
    - [x] fix GitHub Actions after upgrade to Spring Boot 3 (JDK17)
    - [x] fix `radp-spring-framework` ResponseBuilder bug
    - [x] fix META-INF/internal resource SPI
    - [x] fix ApplicationContextHelper#getBean bug
    - [x] fix `RestExceptionHandler`

## 3.12

- [x] 基于 JDK17 + SpringBoot3.x
- [x] netty-resolver-dns-native-macos 增加 arm64 支持
- [x] `javax.validate:validation-api` -> `jakarta.validate:jakarta.validation-api`
- [x] `javax.servlet.*` -> `jakarta.servlet.*`
- [x] 允许在 `future` 分支出发 GitHub Actions

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
- [x] Update archetype, `.mvn`, `.github` actions

## 0.6

- [x] Support GitLab CI/CD
- [x] Provide ubuntu/24.04 Vagrantfile for mac arm64

## 0.5

- [x] dependency management shiro and jwt version
- [x] add Utility classes for base64 and digest
- [x] ci/cd supported. Publish packages to GitHub packages and maven central portal

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