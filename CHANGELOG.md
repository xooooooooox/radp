# ChangeLog

## 2.14

- feature
  - [x] Remove module `radp-tomcat-spring-boot-starter`
  - [x] Rename module `radp-swagger3-spring-boot-starter` to `radp-springdoc-webmvc-spring-boot-starter`
  - [x] Add module `radp-springdoc-webflux-spring-boot-starter`
  - [x] Disable autoconfiguration `BootstrapLogAutoConfiguration`, `AccessLogAutoConfiguration`, `WebAPIAutoConfiguration`
  - [x] Enable autoconfiguration `AsyncTaskExecutionAutoConfiguration`
- dependencies
  - [x] Optimize pluginManagement. Use radp-dependencies manage maven plugin version, use radp-parent manage plugin configuration
  - [x] Optimize radp-dependencies, use `springdoc-openapi` bom instead`
- parent
  - [x] `radp-parent` add properties `java.version`, `maven.compiler.source`, `maven.compiler.target`, `project.build.sourceEncoding` 等
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