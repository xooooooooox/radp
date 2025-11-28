# ChangeLog

## *.27

### Shared changes

#### Features

- Add starter `radp-jwt-spring-boot-starter`, implementing JWT-based authentication and authorization.
- Optimize `BasePOAutoFillStrategy` to use `LoginUserResolver` to obtain the current logged-in user from context.
- Add utility methods in `ServletUtils` to wrap HTTP responses with JSON content.
- Add `#addOrder` utility method to `MybatisUtils`.
- Add `#deleteBatch` utility method to `BaseMapperX`.
- Optimize `ErrorCodeLoader`
  - Implemented resource merging for `META-INF/error/message*.properties` with override priority (libraries < app
    classes).
  - Added support for Spring `MessageSource` to enable application-specific i18n error messages.
  - Introduced fallback mechanism ensuring error resolution robustness: app messages > internal bundle > error code
    itself.
  - Improved thread-safety in message resource initialization.

#### Tests

- Add tests for `MessageFormatter`, `MessageFormatUtils`, and `ResponseBuilder`.
- Fix tests for `RedissonServiceSmokeTest`, `ElasticsearchKibanaTest`, `

#### Refactor

- Rename `SingleResult` and `PageResult` methods:
  - `#build` → `#ok`
  - `#buildFailure` → `#failed`
- Optimize custom `AbstractAssert`, removing inheritance from Spring’s `Assert`.
- Optimize `BaseConvertor`.
- Adjust built-in error codes so that codes below `1000` are reserved for framework-level internal use.
- Rename the `PageParam` constant `PAGE_SIZE_NONE` to `NO_PAGINATION`.

#### Chore — Scaffold

- Add `@Contract` annotations to `ClientAssert`, `ServerAssert`, and `ThirdServiceAssert`, and refactor null/nonnull
  handling.
- Add module `xxx-case` to `scaffold-std`.
- Replace hardcoded empty string with `PREFIX` in `RedisKeyProvider`.
- Restructure package organization in the `xxx-api` layer.
- Add `checkstyle-idea.xml`.
- Fix Writerside setup.

#### Documentation

- Improve Javadoc for `MobileConvert`, `Sm4StringEncryptor`, `JasyptUtils`, and `JwtAutoConfiguration`.

> Note: The exact Swagger dependency coordinates and default `radpVersion` differ per major version. See the
> version-specific sections below.

### 3.27

- DependencyManagement:
  - Add `io.swagger.core.v3:swagger-annotations-jakarta:2.2.29`.
  - Set `swagger-api.version=2.2.29`.
  - Upgrade `retrofit.version` from `2.9` to `3.0`.
  - Upgrade `spring-boot.version` from `3.4.5` to `3.5.8`
  - Remove `org.springframework.kafka:spring-kafka` and let it be managed by the Spring Boot BOM.
  - Reorder dependencies so that all BOMs are grouped at the top.
  - Remove property`netty.version`.
- Scaffold:  
  - Default `radpVersion` is `3.27`.
  - `xxx-type` layer uses `swagger-annotations-jakarta`.

### 2.27

- DependencyManagement:
  - Add `io.swagger.core.v3:swagger-annotations:2.2.8` (non-Jakarta).
  - Set `swagger-api.version=2.2.8`.
  - Retrofit remains on the 2.x line’s version (no `2.9 → 3.0` upgrade here).
- Scaffold:
  - Default `radpVersion` is `2.27`.
  - `xxx-type` layer uses `swagger-annotations` (non-Jakarta).

---

## *.26

### Shared changes

#### Features

- Add modules:
  - `radp-solution-excel`
  - `radp-solution-dict`
- Optimize Checkstyle configuration to ignore comments when checking trailing whitespace.
- Optimize `radp-commons`.
- Optimize `radp-jasypt-spring-boot-starter`: add SM4 encryption/decryption support with auto-configuration.
- Update Checkstyle configurations and remove unnecessary or redundant checks.
- Optimize `JacksonUtils`: add methods for parsing JSON from `File` and `URL`.
- Add Junie guidelines.
- Adjust Jacoco branch coverage threshold from `0.60` to `0.50`.

#### Dependencies

- Remove `commons-io` to resolve a transitive conflict with `fastexcel`.

#### Fixes

- Fix `RestExceptionHandler`.
- Update Checkstyle configuration paths to use `${maven.multiModuleProjectDirectory}` for improved flexibility.

#### Chore — Scaffold

- Update scaffold default `radpVersion` to `*.26` (actual value differs per major line).

### 3.26

- Uses the dependency and plugin versions compatible with Spring Boot 3.x and Jakarta APIs.
- Scaffold default `radpVersion` is `3.26`.

### 2.26.4

#### Features

- Add `LoginUserResolver` interface to resolve the current logged-in user context.

#### Fixes

- Fix `BasePOAutoFillStrategy`.

### 2.26.3

#### Fixes

- Fix `BasePO` and `TenantBasePO`.

#### Chore — Scaffold

- Update scaffold default `radpVersion` to `2.26.3`.

### 2.26.2

#### Features

- Allow multiple MyBatis autofill strategies to run for the same entity so `BasePO` audit fields can coexist with custom
  logic
  (for example, tenant-specific fields).
- Add `TenantContextHolder` and `TenantAutoFillStrategy` (via `radp-solution-tenant`) to populate `tenantId`
  automatically for `TenantBasePO`.

#### Chore — Scaffold

- Update scaffold default `radpVersion` to `2.26.2`.

### 2.26.1

#### Refactor

- Refactor the MyBatis autofill implementation.

#### Chore — Scaffold

- Update scaffold default `radpVersion` to `2.26.1`.

### 2.26

- Shares the features listed in **Shared changes**.
- Uses Spring Boot 2.x–compatible versions for Checkstyle, spring-javaformat, and other plugins.
- Scaffold default `radpVersion` is `2.26`.

---

## *.25

### Shared changes

#### Features

- Add modules:
  - `radp-solutions-tenant`
  - `radp-dynamic-datasource-spring-boot-starter`
- Optimize:
  - `devcontainer`
  - `radp-mybatis-spring-boot-starter`
  - `radp-spring-data`
  - `radp-spring-boot`:
    - Enable conditional configuration for `WebAPIAutoConfiguration` based on properties.
- Optimize `radp-commons`:
  - Optimize `SnowflakeGenerator`.
  - Add `PasswordGeneratorUtils` for password generation and validation.
  - Enhance `RandomStringUtils`:
    - Add methods to generate N-digit numbers, Mainland China mobile numbers, valid usernames, valid emails, etc.
    - Allow username validation and random username generation to use custom rules (regex-based validation, rule-based
      generation).
  - Add `isValidMobile()` helper for Mainland China mobile numbers.

#### Bug fixes

- Fix `AutofillMetaObjectHandler#updateFill` not being invoked.

#### Dependencies

- In `DependencyManagement`, add:
  - `org.bouncycastle:bcprov-jdk15to18:1.81`
  - `org.passay:passay:1.6.6`

#### Chore — Scaffold

- Standardize log file paths using `LOG_HOME` and `LOG_FILE_BASENAME`.
- Optimize `.idea` code styles.
- Optimize `checkstyle-suppressions.xml` and suppress `HideUtilityClassConstructor` globally.
- Optimize `application-local.yaml`.

#### Documentation

- Add comments in `application-logback.yaml` explaining how to adjust the log file name using `LOG_FILE_BASENAME`.

### 3.25

- Scaffold default `radpVersion` is `3.25`.

### 2.25

- Scaffold default `radpVersion` is `2.25`.
- Uses dependency versions aligned with Spring Boot 2.x.

---

## *.24

### Shared changes

#### Features

- Add devcontainer.
- Optimize `radp-logging-spring-boot-starter` to support overriding the log file basename via an environment variable.

#### Fixes

- Fix `.mvn/settings.xml`.

#### Chore — Build

- Update license.
- Optimize `jib-maven-plugin` configuration properties.

#### Chore — Scaffold

- Optimize `Dockerfile` and `.dockerignore`.
- Optimize helper script `helper`.
- Fix archetype generation failures.
- Optimize `layers.xml`.
- Optimize `.devcontainer`.

### 3.24

- Scaffold default `radpVersion` is `3.24`.

### 2.24

- Scaffold default `radpVersion` is `2.24`.

---

## *.23.1

### Shared changes

#### Chore — Build

- Add support for configuring a custom container runtime user.

#### Chore — Scaffold

- Update `.gitlab-ci.yml`.
- Optimize `Dockerfile`.
- Add `.dockerignore`.
- Add `imageRegistry` property and update fileSet includes.
- Refactor Dockerfile build script `build.sh`.

### 3.23.1

- Scaffold default `radpVersion` is `3.23.1`.

### 2.23.1

- Scaffold default `radpVersion` is `2.23.1`.

---

## *.23

### Shared changes

#### Chore — Build

- Optimize `.mvn`:
  - Add `jvm.config`.
  - Add private registry server configuration to Maven settings.
- Optimize profiles `o-release` and `o-tar`.
- Optimize assembly:
  - Change dist filename from `${project.build.finalName}-assembly.tar.gz`
    to `${project.build.finalName}-assembly-${project.version}.tar.gz`.
  - Remove the `zip` format from assembly configuration.
- Optimize `maven-release-plugin`:
  - Add `maven.release.extraPreparationProfiles`.
  - Add `maven.release.extraReleaseProfiles`.
- Add profile `o-catalog`.
- Remove redundant `auto-release` profile.
- Optimize `jib-maven-plugin` configuration to support building images without the Docker daemon.

#### Chore — Dependencies

- Remove `archetype-packaging` from `pluginManagement` to fix
  `Failed to retrieve plugin descriptor … No plugin descriptor found at META-INF/maven/plugin.xml`.

#### Chore — Scaffold

- Update scaffold configuration to match the build changes above.

### 3.23

- DependencyManagement:
  - Upgrade Spring Boot parent from `3.4.5` to `3.5.4`.
  - Upgrade `com.google.cloud.tools:jib-maven-plugin` from `3.4.5` to `3.4.6`.
- Scaffold default `radpVersion` is `3.23`.

### 2.23

- Uses a 2.x compatible Spring Boot parent (no `3.4.5 → 3.5.4` change).
- Scaffold default `radpVersion` is `2.23`.

---

## *.22

### Shared changes

#### Chore — Dependencies

- Upgrade `io.gatling.highcharts:gatling-charts-highcharts` from `3.2.1` to `3.10.0`.
- Upgrade `io.github.git-commit-id:git-commit-id-maven-plugin` from `9.0.1` to `9.0.2`.
- Remove unused MongoDB dependencies.

#### Chore — Build

- Upgrade `io.gatling:gatling-maven-plugin` from `3.0.3` to `4.6.0`.
- Remove unused `disableCompiler` configuration in the Gatling plugin.
- Update Checkstyle config location.
- Add default `pluginGroup` to `.mvn/settings.xml`.
- Optimize profile `code-review`:
  - Add `sonar.login`.
  - Add `sonar.qualitygate.wait`.
- Optimize profile `unit-test`.
- Rename profile `aggregate-reports` to `site-aggregate`.
- Remove unused `argLine` configuration in Surefire to fix Jacoco integration.
- Add profile `o-wrapper`.
- Remove property `sonar.login` to avoid deprecation warnings.
- Change `maven-release-plugin` `tagNameFormat` from `x.y.z` to `vx.y.z`.
- Add `skipViaCommandLine` option to override the `git-commit-id-maven-plugin` skip flag.

#### Chore — Scaffold

- Avoid generating incorrect `.gitlab-ci.yml`.
- Update `archetype-catalog-vcs.xml`.
- Update IDEA and Checkstyle configuration.
- Update `.editorconfig`.
- Update `application-local.yaml` to use dynamic ports.
- Update `archetype-metadata.xml`:
  - Include `.coding` directory.
  - Include `.editorconfig`.
- Update `archtype-metadata.xml` to include `.devcontainer`.
- Update `.mvn/settings.xml`:
  - Add default `pluginGroup`.
  - Replace `devops.release.arguments` with `maven.release.arguments`.
- Update profiles in `.mvn/maven.config` to include `code-review`.
- Update `.gitlab-ci.yml`.
- Optimize `checkstyle-suppressions.xml`:
  - Add a suppression for `target/generated-sources`.
- Optimize `assembly.xml` to include `jib-maven.tar`.
- Fix Writerside configuration in the scaffold.

#### Style

- Update code style and Checkstyle configuration.
- Optimize import order.

### 3.22

- Scaffold default `radpVersion` is `3.22`.

### 2.22

- Scaffold default `radpVersion` is `2.22`.

---

## *.21

### Shared changes

#### Breaking changes

- Refactor `radp-logging-spring-boot-starter` to provide clearer and more extensible logging.

#### Features

- Add Redis key management utilities to standardize key creation and validation.
- Add a comprehensive testing support module in `radp-spring-test`.

#### Bug fixes

- Fix `class file for edu.umd.cs.findbugs.annotations.SuppressFBWarnings not found`.
- Fix and optimize `TtlThreadPoolTaskExecutor` and `ExceptionHandlingAsyncTaskExecutor`.
- Fix error handling in embedded servers.
- Fix `Unable to find a URL to the parent project. The parent menu will NOT be added.`.
- Resolve transitive dependency issues.

#### Refactor

- Relocate `ResponseBuilder` to the DTO package.
- Refactor `LocalCallFirstCluster` and `DubboExceptionFilter` to reduce complexity and improve readability.
- Optimize `RedissonService` to use non-deprecated `setNx` methods.

#### Tests

- Add modules:
  - `radp-smoke-tests-redis`
  - `radp-smoke-tests-logging`
  - `radp-smoke-tests-test`
- Add tests for Testcontainers and embedded servers.
- Add `RedissonServiceTest` for `radp-redis-spring-boot-starter`.

#### Chore — Scaffold

- Update `application-logback.yaml` and `logback-test.xml`.
- Add `RedisKeyProvider` enum.
- Relocate assert classes to a new package.
- Optimize `.gitignore`, `.gitattributes`, `.gitlab-ci.yml`.
- Add `.idea` settings for copyright and scope.
- Change license from GNU GPLv3 to Apache 2.0.
- Optimize CheckStyle-IDEA plugin configuration and IDEA CodeStyle.

### 3.21

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

### 2.21

- Dependencies:
  - Override `kafka.version` from `3.1.2` to `3.9.0`.
  - Remove `maven-antrun-plugin.version`.
- Uses Spring Boot 2.x parent and compatible dependency set.
- Scaffold default `radpVersion` is `2.21`.

---

## *.20.2

### Shared changes

#### Fixes

- Fix `BaseException`:
  - Properly set the cause when a `Throwable` is passed as the last vararg parameter.
  - Handle placeholder mismatches when a `Throwable` is passed as a parameter.
- Add `ErrorCodeLoader.getErrMessage(String errCode)` to retrieve the raw message template without placeholder
  substitution.
- Remove deprecated `ListenableFuture` methods in `TtlThreadPoolTaskExecutor`.
- Add `serialVersionUID` to improve serialization stability.
- Use a more appropriate constructor for adaptive extension instantiation.
- Simplify instance checks in `ExceptionHandlingAsyncTaskExecutor`.
- Fix `ExtensionLoader` by replacing deprecated `newInstance` usage.
- Fix `AccessLogConfiguration` NPE.

#### Chore — Parent / Build

- Optimize profile `auto-update-local-catalog` by disabling default excludes.
- Fix profiles `code-review`, `unit-test`, and `integration-test`.
- Add profile `aggregate-reports`:
  - Integrate SonarQube.
  - Add `maven-jxr-plugin` for source cross-reference.
  - Configure `maven-site-plugin` and `project-info-reports-plugin`.

#### Chore — Dependencies / Plugins

- Upgrade:
  - `maven-wrapper-plugin.version` from `3.2.0` to `3.3.2`.
  - `maven-archetype-plugin.version` from `3.2.0` to `3.3.1`.
  - `sonar-maven-plugin.version` from `3.9.1.2184` to `5.1.0.4751`.
  - `maven-surefire-plugin` from `3.0.0-M7` to `3.1.2`.
  - `maven-failsafe-plugin` from `3.0.0-M3` to `3.1.2`.
  - `org.jacoco:jacoco-maven-plugin` from `0.8.7` to `0.8.9`.
- Add `com.google.code.findbugs:annotations:3.0.1` to `DependencyManagement`.
- Add to `PluginManagement`:
  - `maven-jxr-plugin:3.3.0`
  - `maven-project-info-reports-plugin:3.6.2`
  - `maven-site-plugin:3.12.1`
  - `maven-checkstyle-plugin:3.3.1`
  - `archetype-packaging:3.2.0`

#### Chore — Scaffold

- Optimize `.mvn`.
- Add `.gitattributes`.
- Add default `ApplicationTests`.
- Add GitHub issue templates.

#### Documentation

- Add Javadoc and resolve Javadoc warnings.

### 3.20.2

- Scaffold default `radpVersion` is `3.20.2`.

### 2.20.2

- Additionally overrides `maven-resources-plugin.version` from `3.2.0` to `3.3.1`.
- Scaffold default `radpVersion` is `2.20.2`.

---

## *.19.1

### Shared changes

#### Fixes

- Fix `ErrorCodeLoader`.
- Fix `BaseException` message formatting with parameters.
- Fix `ExceptionUtils.clientException`, `ExceptionUtils.serverException`,
  and `ExceptionUtils.thirdServiceException` placeholder handling.
- Fix default value for `PageResult#total`.

#### Documentation

- Add GitHub issue templates.

#### Scaffold

- Fix `XxxAssert` message formatting with placeholders.

### 3.19.1

- Scaffold default `radpVersion` is `3.19.1`.

### 2.19.1

- Scaffold default `radpVersion` is `2.19.1`.

---

## *.19

### Shared changes

#### Features

- Optimize custom assertion utilities.

#### Chore — Parent

- Add profiles `env-sit` and `o-all-env`.

#### Chore — Dependencies

- Upgrade `com.google.cloud.tools:jib-maven-plugin` from `3.4.4` to `3.4.5`.

#### Chore — Scaffold

- Optimize `build.sh`.
- Optimize POMs for `scaffold-xx` `xx-types` and `xx-app` modules.
- Fix `entrypoint.sh` (`Unrecognized option: --spring.config.additional-location=`).
- Optimize `application-local.yaml` for exposing env and loggers endpoints.
- Optimize `dev-ops/docker-compose-app.yaml`.

### 3.19

- Scaffold default `radpVersion` is `3.19`.

### 2.19

- Scaffold default `radpVersion` is `2.19`.

---

## *.18.1

### Shared changes

#### Chore — Dependencies

- Override `liquibase.version` to `4.31.1`.

#### Chore — Scaffold

- Optimize `entrypoint.sh`.
- Improve Liquibase integration:
  - Fix duplicate initialization caused by filename inconsistencies in changesets.
  - Improve the `changelog-init.yaml` example.
  - Improve `migration/20241018` directory structure and multi-environment support.

### 3.18.1

- Scaffold default `radpVersion` is `3.18.1`.

### 2.18.1

- Additionally override `commons-lang3.version` to `3.17.0`.
- Scaffold default `radpVersion` is `2.18.1`.

---

## *.18

### Shared changes

#### Chore — Dependencies

- Upgrade `org.sonatype.central:central-publishing-maven-plugin` from `0.6.0` to `0.7.0`.

#### Chore — Parent

- Add support for publishing snapshots to the Central portal.
- Add profile `env.uat`.
- Optimize profile `repo-central`:
  - Add properties `auto.layered.enabled` and `auto.assembly.enabled`.
- In profile `coding`, add property `user.docker.build.namespace`.

#### Chore — Scaffold

- Optimize `.mvn/settings.xml`:
  - Add profile `repo-central`.
  - In profile `default`, add `setting.docker.build.namespace`.
- Add support for Writerside and mdBook to manage project documentation.
- Fix `layers.xml`, `Dockerfile`, and `build.sh`.
- Fix Liquibase plugin properties file.
- Add `application-uat.yaml`.
- Optimize `docker.build.image_name`.
- Optimize dev-ops:
  - Rename `COMPOSE_PROJECT_NAME` and network.
  - Add scripts `up.sh`, `down.sh`, and `ps.sh`.
- Fix `build.sh` and `start.sh`.
- Enable probe endpoints in `application-actuator.yml`.
- Fix Dockerfile and Spring Boot `entrypoint.sh`.
- Optimize `.gitlab-ci.yml`.

### 3.18

- Scaffold default `radpVersion` is `3.18`.

### 2.18

- Scaffold default `radpVersion` is `2.18`.

---

## *.17

### Shared changes

#### Chore — Parent

- Optimize profile `auto-jib` by splitting it into:
  - `auto-jib-buildTar`
  - `auto-jib-dockerBuild`
    to work around `jib:buildTar` not supporting multi-platform builds.
- Add profiles:
  - `o-release`
  - `o-tar`
  - `publish-harbor`
  - `publish-artifactory`

#### Chore — Scaffold

- Fix `.github/trigger-releases.yml`.
- Change generated project version from `1.0.0-SNAPSHOT` to `1.0-SNAPSHOT`.
- Optimize dev-ops application configuration.
- Optimize `.mvn/settings.xml`.

### 3.17

- Scaffold default `radpVersion` is `3.17`.

### 2.17

- Scaffold default `radpVersion` is `2.17`.

---

## *.16.1

### Shared changes

#### Fixes

- Fix deployment to Artifactory:
  - `the parameters 'url' for wagon-maven-plugin are missing or invalid`.
- Fix GitHub Actions:
  - `Unable to decrypt gpg passphrase`.

#### Chore — Parent

- Rename profile `auto-archetype-xx` to `auto-upload-catalog-xx`.

#### Chore — Scaffold

- Update `.mvn/settings.xml`.

### 3.16.1

- Scaffold default `radpVersion` is `3.16.1`.

### 2.16.1

- Scaffold default `radpVersion` is `2.16.1`.

---

## *.16

### Shared changes

#### Features

- Add tests for `radp-jasypt-spring-boot-starter`.

#### Fixes

- Fix `logging.pattern.console` not taking effect.

#### Chore — Parent / Dependencies

- Optimize profile `auto-jib`.
- Optimize archetype catalog deployment to a self-hosted Artifactory.
- In `PluginManagement`, add:
  - `maven-resources-plugin`
  - `maven-enforcer-plugin`

#### Chore — Scaffold

- Remove `JasyptTest` and add a blank JUnit 5 `ApiTest`.
- Optimize `application.yaml` and add `application-jasypt.yaml`.
- Update `application-logback.yaml`.
- Optimize `application.yaml` and add `application-webmvc.yaml`.
- In `.mvn/settings.xml`, add `auto.archetype.catalog.artifactory`.

#### Writerside

- Update `1.1.1-use_archetype_create_project.md`.

### 3.16

- In addition, `PluginManagement` includes `wagon-maven-plugin`.
- Scaffold default `radpVersion` is `3.15.1` (legacy baseline value in this branch).

### 2.16

- `PluginManagement` adds `maven-resources-plugin` and `maven-enforcer-plugin` only.
- Scaffold default `radpVersion` is `2.16`.

---

## *.15.1

### Shared changes

#### Dependencies

- Upgrade `commons-io:commons-io` to `2.17.0` to ensure compatibility with `org.apache.tika:tika-core`.

#### Scaffold

- Add dev-ops files:
  - `docker-compose-pgadmin.yaml`
  - `docker-compose-redis-commander.yaml`
- Fix dev-ops configuration in the scaffold.

### 3.15.1

- Scaffold default `radpVersion` is `3.15.1`.

### 2.15.1

- Scaffold default `radpVersion` is `2.15.1`.

---

## *.15

### Shared changes

#### Features

- Add `FileUtils` to `radp-common`.

#### Dependencies

- In `DependencyManagement`, add:
  - `central-publishing-maven-plugin:0.6.0`
  - `maven-javadoc-plugin:3.5.0`
- Keep `space.x9x.radp:radp` using the `maven-deploy-plugin` defined in `radp-dependencies` instead of the one from
  `spring-boot-dependencies`.
- Fix `org.apache.dubbo:dubbo-dependencies-zookeeper`.
- Change Knife4j starter coordinates to `com.github.xiaoming:knife4j-openapi3-jakarta-spring-boot-starter:4.1.0`.
- Remove redundant plugin versions from `radp-parent` and duplicate plugin definitions from `radp-dependencies`.
- Fix an issue where transitive dependencies downgraded the Spring Framework version.

#### Parent

- Add properties:
  - `app.build.base_image.jdk8`
  - `app.build.base_image.jdk11`
  - `app.build.base_image.jdk17`

#### Scaffold

- Optimize dev-ops.
- Optimize Docker base image configuration.
- Fix `application-dev.yaml`.

### 3.15

- Upgrade Spring Boot from `3.2.3` to `3.4.4`.
- Upgrade Spring Cloud from `2023.0.0` to `2024.0.0`.
- Scaffold default `radpVersion` is `3.15`.

### 2.15

- Remains on the Spring Boot 2.x line with compatible Spring Cloud versions (no `3.2.3 → 3.4.4` or
  `2023.0.0 → 2024.0.0`).
- Scaffold default `radpVersion` is `2.15`.

---

## *.14

### Shared changes

#### Features

- Remove module `radp-tomcat-spring-boot-starter`.
- Rename `radp-swagger3-spring-boot-starter` to `radp-springdoc-webmvc-spring-boot-starter`.
- Add module `radp-springdoc-webflux-spring-boot-starter`.
- Disable auto-configuration:
  - `BootstrapLogAutoConfiguration`
  - `AccessLogAutoConfiguration`
  - `WebAPIAutoConfiguration`
- Enable `AsyncTaskExecutionAutoConfiguration`.

#### Fixes

- Resolve `AsyncTaskExecutionAutoConfiguration` deprecation warning involving `TaskExecutorBuilder`.

#### Dependencies

- Optimize plugin management:
  - Use `radp-dependencies` to manage Maven plugin versions.
  - Use `radp-parent` to manage plugin configuration.

#### Parent

- Add properties:
  - `java.version`
  - `maven.compiler.source`
  - `maven.compiler.target`
  - `project.build.sourceEncoding`
  - and related defaults.

#### Documentation

- Update Writerside `about.md`.

### 3.14

- Use Knife4j Jakarta starter: `knife4j-openapi3-jakarta-spring-boot-starter:4.1.0`.

### 2.14

- Use `springdoc-openapi` BOM and 2.x-compatible `springdoc` / Knife4j coordinates.

---

## *.13

### Shared changes

#### Scaffold

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

#### Dependencies

- Replace `pl.project13.maven:git-commit-id-plugin` with `io.github.git-commit-id:git-commit-id-maven-plugin`.
- Upgrade `commons-io:commons-io` to `2.13.0`.
- Upgrade `springdoc-openapi` to `2.4.0`.
- Upgrade MyBatis-Plus and `mybatis-spring-boot`, and use the MyBatis-Plus BOM.
- Upgrade Spring Cloud to a newer compatible release.

#### Features

- Add `MultiResult` to `radp-spring-framework`.
- In `radp-logging-spring-boot-starter` logback template, add `NopStatusListener`.
- In `ResponseBuilder`, add `Result buildFailure(ErrorCode errorCode)`.
- In `SingleResult`, add `build()` method.
- Optimize profiles `auto-layered` and `auto-assembly`.

#### Fixes

- Fix missing `PaginationInnerInterceptor` by explicitly declaring `mybatis-plus-jsqlparser`.
- Fix CI/CD pipelines after upgrading the stack.
- Fix `ResponseBuilder` bug in `radp-spring-framework`.
- Fix internal SPI registration under `META-INF`.
- Fix `ApplicationContextHelper#getBean`.
- Fix `RestExceptionHandler`.

### 3.13

- Spring Boot 3.x line (JDK 17 baseline).
- Dockerfile base image: `eclipse-temurin:17-jdk`.

### 2.13

- Uses `spring-boot-parent` `2.7.18`.
- Uses 2.x compatible versions of Spring Cloud, SpringDoc, and MyBatis.
- Dockerfile base image and scaffold details are tuned for 2.x / JDK 8–11 setups.

---

## *.12

### Shared changes

- Introduce a dual-line strategy:
  - `future` branch for **JDK 17 + Spring Boot 3.x**.
  - Existing 2.x line maintained for stability.

### 3.12

- Start of the Spring Boot 3.x line:
  - JDK 17 baseline.
  - Add `arm64` support for `netty-resolver-dns-native-macos`.
  - Switch from `javax.validation:validation-api` to `jakarta.validation:jakarta.validation-api`.
  - Switch from `javax.servlet.*` to `jakarta.servlet.*`.
  - Allow the `future` branch to trigger GitHub Actions.

### 2.12

- Last Spring Boot 2.x minor before the 3.x transition.
- Introduces the `future` branch while keeping core functionality on the 2.x stack.

---

## 0.11

- Fix archetype `auto-release` profile not working.
- Fix archetype import errors.

## 0.10

- Fix GitHub Actions Maven cache not working.
- Change default JDK from `java 8.0.432+6-tem` to `java 8.0.442+6-amzn` to support arm64.
- Update archetype `application-dev.yaml`.

## 0.9

- Optimize GitHub Actions Maven cache configuration.
- Optimize `auto-assembly` profile.

## 0.8

- Update archetype structure.
- Optimize GitHub Actions workflows.
- Add `rapd-design-pattern-framework`:
  - Provide abstractions for common design patterns to reduce usage complexity and standardize style.
  - Include a decision-tree pattern abstraction.
- Archetype:
  - Optimize assembly.
  - Optimize layered build.

## 0.7

- Add support for publishing GitLab Pages via GitLab CI/CD.
- Optimize the GitLab CI pipeline by using remote templates.
- Update archetype, `.mvn`, and `.github` Actions.

## 0.6

- Add GitLab CI/CD support.
- Provide an `ubuntu/24.04` Vagrantfile for macOS arm64.

## 0.5

- Add dependency management for Shiro and JWT.
- Add utility classes for Base64 and digest operations.
- Add CI/CD support to publish artifacts to GitHub Packages and Maven Central.

## 0.3

- Add utility classes:
  - `RegexUtils` for regular expressions.
  - `NanoIdGenerator` and `SnowflakeGenerator` for ID generation.

## 0.2

### Features

- Improve pagination support.

### Fixes

- Refine archetype includes and remove redundant entries.

## 0.1.3

- Support publishing artifacts to Maven Central, GitHub Packages, and a self-hosted Artifactory.
- Use GitHub Actions to build and publish GitHub Pages.
- Add a DDD scaffold.
- Add core common components.
