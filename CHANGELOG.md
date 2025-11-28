# ChangeLog

## *.27

### 3.27

#### Features

- Add starter `radp-jwt-spring-boot-starter`, implementing JWT-based authentication and authorization.
- Optimize `BasePOAutoFillStrategy`, using `LoginUserResolver` to obtain the current logged-in user from context.
- Add utility methods in `ServletUtils` to wrap HTTP responses with JSON content.
- Add `#addOrder` utility method to `MybatisUtils`.
- Add `deleteBatch` utility method to `BaseMapperX`.

#### Tests

- Add tests for `MessageFormatter`, `MessageFormatUtils`, and `ResponseBuilder`.

#### Dependencies

- In `DependencyManagement`, add `io.swagger.core.v3:swagger-annotations-jakarta:2.2.29` and set
  `swagger-api.version=2.2.29`.
- Upgrade `retrofit.version` from `2.9` to `3.0`.

#### Refactor

- Rename `SingleResult` and `PageResult` methods:
  - `#build` → `#ok`
  - `#buildFailure` → `#failed`
- Optimize custom `AbstractAssert`, removing inheritance from Spring’s `Assert`.
- Optimize `BaseConvertor`.
- Adjust the system’s built-in error codes, reserving codes below `1000` for framework-level internal use.
- Rename the `PageParam` constant `PAGE_SIZE_NONE` to `NO_PAGINATION`.

#### Chore

##### Scaffold

- Update scaffold default `radpVersion` to `3.27`.
- Add `swagger-annotations-jakarta` dependency to the `xxx-type` layer.
- Add `@Contract` annotations to `ClientAssert`, `ServerAssert`, and `ThirdServiceAssert` and refactor null/nonnull handling.
- Add module `xxx-case` to `scaffold-std`.
- Replace hardcoded empty string with `PREFIX` in `RedisKeyProvider`.
- Restructure package organization in the `xxx-api` layer.
- Add `checkstyle-idea.xml`.
- Fix Writerside setup.

#### Documentation

- Improve Javadoc for `MobileConvert`, `Sm4StringEncryptor`, `JasyptUtils`, and `JwtAutoConfiguration`.

### 2.27

Same as **3.27** unless otherwise noted.

#### Differences

- **Dependencies**
  - Use `io.swagger.core.v3:swagger-annotations:2.2.8` (non-Jakarta) and set `swagger-api.version=2.2.8`.
  - Retrofit remains on the 2.x line’s version (no `2.9 → 3.0` upgrade here).
- **Scaffold**
  - Scaffold default `radpVersion` is `2.27`.
  - The `xxx-type` layer adds `swagger-annotations` instead of `swagger-annotations-jakarta`.

---

## *.26

### 3.26

#### Features

- Add modules:
  - `radp-solution-excel`
  - `radp-solution-dict`
- Optimize the Checkstyle configuration to ignore comments when checking trailing whitespace.
- Optimize `radp-commons`.
- Optimize `radp-jasypt-spring-boot-starter`: add SM4 encryption/decryption support with auto-configuration.
- Update Checkstyle configurations and remove unnecessary or redundant checks.
- Optimize `JacksonUtils`: add methods for parsing JSON from `File` and `URL`.
- Add Junie guidelines.
- Adjust Jacoco branch coverage threshold from `0.60` to `0.50`.

#### Dependencies

- Remove `commons-io` to resolve a transitive conflict with `fastexcel`.
- Upgrade `io.spring.javaformat:spring-javaformat-maven-plugin` from `0.0.46` to `0.0.47`.
- Upgrade `com.puppycrawl.tools:checkstyle` from `9.3` to `11.0.1`.

#### Fixes

- Fix `RestExceptionHandler`.
- Update Checkstyle configuration paths to use `${maven.multiModuleProjectDirectory}` for improved flexibility.

#### Chore

##### Scaffold

- Update scaffold default `radpVersion` to `3.26`.

### 2.26.4

#### Features

- Add `LoginUserResolver` interface to resolve the current logged-in user context.

#### Fixes

- Fix `BasePOAutoFillStrategy`.

### 2.26.3

#### Fixes

- Fix `BasePO` and `TenantBasePO`.

#### Chore

##### Scaffold

- Update scaffold default `radpVersion` to `2.26.3`.

### 2.26.2

#### Features

- Allow multiple MyBatis autofill strategies to run for the same entity so `BasePO` audit fields can coexist with custom
  logic (for example, tenant-specific fields).
- Add `TenantContextHolder` and `TenantAutoFillStrategy` (via `radp-solution-tenant`) to populate `tenantId`
  automatically for `TenantBasePO`.

#### Chore

##### Scaffold

- Update scaffold default `radpVersion` to `2.26.2`.

### 2.26.1

#### Refactor

- Refactor the MyBatis autofill implementation.

#### Chore

##### Scaffold

- Update scaffold default `radpVersion` to `2.26.1`.

### 2.26

Same as **3.26** in terms of features (Excel/Dict modules, Jacoco threshold adjustments, Jasypt SM4 support, etc.), unless otherwise noted.

#### Differences

- **Dependencies**
  - Also removes `commons-io` for the `fastexcel` conflict, but Checkstyle and spring-javaformat plugin versions follow
    the Spring Boot 2.x line (see the 2.x POMs for exact versions).
- **Scaffold**
  - Scaffold default `radpVersion` is `2.26`.

---

## *.25

### 3.25

#### Features

- Add modules:
  - `radp-solutions-tenant`
  - `radp-dynamic-datasource-spring-boot-starter`
- Optimize `devcontainer`.
- Optimize `radp-mybatis-spring-boot-starter`.
- Optimize `radp-spring-data`.
- Optimize module `radp-spring-boot`:
  - Enable conditional configuration for `WebAPIAutoConfiguration` based on properties.
- Optimize module `radp-commons`:
  - Optimize `SnowflakeGenerator`.
  - Add `PasswordGeneratorUtils` for password generation and validation.
  - Enhance `RandomStringUtils`:
    - Add methods to generate N-digit numbers, Mainland China mobile numbers, valid usernames, valid emails, etc.
    - Allow username validation and random username generation to use custom rules (regex-based validation, rule-based generation).
  - Add helper `isValidMobile()` for Mainland China mobile numbers.

#### Bug Fixes

- Fix `AutofillMetaObjectHandler#updateFill` not being invoked.

#### Dependencies

- In `DependencyManagement`, add:
  - `org.bouncycastle:bcprov-jdk15to18:1.81`
  - `org.passay:passay:1.6.6`

#### Chore

##### Scaffold

- Update scaffold default `radpVersion` to `3.25`.
- Optimize `maven.config` default active profiles.
- Standardize log file paths using `LOG_HOME` and `LOG_FILE_BASENAME` environment variables.
- Optimize `.idea` project code styles.
- Optimize `checkstyle-suppressions.xml` and suppress the `HideUtilityClassConstructor` rule globally.
- Optimize `application-local.yaml`.

#### Documentation

- Add comments to `application-logback.yaml` explaining how to adjust the log file name using `LOG_FILE_BASENAME`.

### 2.25

Same as **3.25** unless otherwise noted.

#### Differences

- Scaffold default `radpVersion` is `2.25`.
- All dependency versions are aligned with the Spring Boot 2.x line (see 2.x POMs for exact versions).

---

## *.24

### 3.24

#### Features

- Add devcontainer.
- Optimize `radp-logging-spring-boot-starter`:
  - Support overriding the log file basename via an environment variable.

#### Fixes

- Fix `.mvn/settings.xml`.

#### Chore

##### Build

- Update license.
- Optimize `jib-maven-plugin` configuration properties.

##### Scaffold

- Update scaffold default `radpVersion` to `3.24`.
- Optimize `Dockerfile` and `.dockerignore`.
- Optimize helper script `helper`.
- Fix archetype generation failures.
- Optimize `layers.xml`.
- Optimize `.devcontainer`.

### 2.24

Same as **3.24** unless otherwise noted.

#### Differences

- Scaffold default `radpVersion` is `2.24`.
- Dependencies remain aligned to the Spring Boot 2.x line.

---

## *.23.1

### 3.23.1

#### Chore

##### Build

- Add support for custom container runtime user.

##### Scaffold

- Update scaffold default `radpVersion` to `3.23.1`.
- Update `.gitlab-ci.yml`.
- Optimize `Dockerfile`.
- Add `.dockerignore`.
- Add `imageRegistry` property and update fileSet includes.
- Refactor Dockerfile build script `build.sh`.

### 2.23.1

Same as **3.23.1** unless otherwise noted.

#### Differences

- Scaffold default `radpVersion` is `2.23.1`.
- CI and image-related configuration are kept compatible with the Spring Boot 2.x line.

---

## *.23

### 3.23

#### Chore

##### Build

- Optimize `.mvn`:
  - Add `jvm.config`.
  - Add private registry server configuration to Maven settings.
- Optimize profiles `o-release` and `o-tar`.
- Optimize assembly:
  - Change dist filename from `${project.build.finalName}-assembly.tar.gz`
    to `${project.build.finalName}-assembly-${project.version}.tar.gz`.
  - Remove `zip` format from assembly configuration.
- Optimize `maven-release-plugin`:
  - Add `maven.release.extraPreparationProfiles` and `maven.release.extraReleaseProfiles`.
- Add profile `o-catalog`.
- Remove redundant `auto-release` profile.
- Optimize `jib-maven-plugin` configuration to support building images without the Docker daemon.

##### Dependencies

- Upgrade `org.springframework.boot:spring-boot-starter-parent` from `3.4.5` to `3.5.4`.
- Upgrade `com.google.cloud.tools:jib-maven-plugin` from `3.4.5` to `3.4.6`.
- Remove `archetype-packaging` pluginManagement to resolve:
  `Failed to retrieve plugin descriptor … No plugin descriptor found at META-INF/maven/plugin.xml`.

##### Scaffold

- Update scaffold default `radpVersion` to `3.23`.

### 2.23

Same as **3.23** in terms of build and scaffold structure unless otherwise noted.

#### Differences

- Continues to use a Spring Boot 2.x parent.
- Scaffold default `radpVersion` is `2.23`.

---

## *.22

### 3.22

#### Chore

##### Dependencies

- Upgrade `io.gatling.highcharts:gatling-charts-highcharts` from `3.2.1` to `3.10.0`.
- Upgrade `io.github.git-commit-id:git-commit-id-maven-plugin` from `9.0.1` to `9.0.2`.
- Remove unused MongoDB dependencies.

##### Build

- Upgrade `io.gatling:gatling-maven-plugin` from `3.0.3` to `4.6.0`.
- Remove unused `disableCompiler` configuration in the Gatling Maven Plugin.
- Update Checkstyle config location.
- Add default `pluginGroup` to `.mvn/settings.xml`.
- Optimize profile `code-review`:
  - Add `sonar.login`.
  - Add `sonar.qualitygate.wait`.
- Optimize profile `unit-test`.
- Rename profile `aggregate-reports` to `site-aggregate`.
- Remove unused `argLine` configuration from the Surefire plugin to fix Jacoco integration.
- Add profile `o-wrapper`.
- Remove `sonar.login` to avoid deprecation warnings.
- Change `maven-release-plugin` `tagNameFormat` from `x.y.z` to `vx.y.z`.
- Add `skipViaCommandLine` option for overriding the `git-commit-id-maven-plugin` skip flag.

##### Scaffold

- Avoid generating incorrect `.gitlab-ci.yml`.
- Update scaffold default `radpVersion` to `3.22`.
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
  - Add suppression for `target/generated-sources`.
- Optimize `assembly.xml` to include `jib-maven.tar`.
- Fix scaffold Writerside configuration.

##### Style

- Update code style and Checkstyle configuration.
- Optimize import order.

### 2.22

Same as **3.22** in terms of build, style, and scaffold changes unless otherwise noted.

#### Differences

- Scaffold default `radpVersion` is `2.22`.
- Dependency and plugin versions are aligned with the Spring Boot 2.x line.

---

## *.21

### 3.21

#### Breaking Changes

- Refactor `radp-logging-spring-boot-starter` for clearer and more extensible logging.

#### Features

- Add Redis key management utilities to standardize key creation and validation.
- Add a comprehensive testing support module in `radp-spring-test`.

#### Bug Fixes

- Fix `class file for edu.umd.cs.findbugs.annotations.SuppressFBWarnings not found`.
- Fix and optimize `TtlThreadPoolTaskExecutor` and `ExceptionHandlingAsyncTaskExecutor`.
- Fix error handling in embedded servers.
- Fix `Unable to find a URL to the parent project. The parent menu will NOT be added.`.
- Resolve various transitive dependency issues.

#### Chore

##### Dependencies

- Upgrade `org.springframework.boot:spring-boot-starter-parent` from `3.4.4` to `3.4.5`.
- Upgrade `testcontainers.version` from `1.17.6` to `1.21.0`.
- Upgrade `com.github.codemonstur:embedded-redis` from `0.11.0` to `1.4.3`.
- Add `com.redis:testcontainers-redis:2.2.2` to `DependencyManagement`.
- Override `kafka.version` from `3.8.1` to `3.9.0` to consolidate `junit-platform.properties`.
- Simplify and clean up dependencies to remove module cycles.
- Remove `mongodb.version` and `maven-surefire-plugin.version` properties.
- Exclude `spring-boot-starter-logging` in `radp-spring-boot-test`.
- Remove unused `radp-spring-test` dependency from `radp-spring-framework`.
- Remove redundant dependencies from `radp-integration-test`.

##### Build

- In `PluginManagement`, add `io.spring.javaformat:spring-javaformat-maven-plugin:0.0.45`.
- Remove the redundant `code-review` profile from the parent.
- Move `git-commit-id-maven-plugin` and `versions-maven-plugin` from `radp-parent` to the root POM.
- Optimize profile `code-review`:
  - Explicitly set `maven.test.skip=false`.
  - Add `spring-javaformat-maven-plugin` and `maven-checkstyle-plugin`.
- Optimize profile `unit-test`:
  - Move it to the root POM.
  - Explicitly set `maven.test.skip=false`.
- Optimize profile `aggregate-reports`:
  - Use `src/checkstyle/checkstyle.xml` (Spring Checks) for Checkstyle.
- Add missing `relativePath` in POM parent configuration.
- Optimize `radp-smoke-tests-archetype`:
  - Remove `maven.install.skip` from submodules.
  - Add `maven.test.skip` to `radp-smoke-tests-archetype`.

##### Scaffold

- Update scaffold default `radpVersion` to `3.21`.
- Update `application-logback.yaml` and `logback-test.xml`.
- Add `RedisKeyProvider` enum.
- Relocate assert classes to a new package.
- Optimize `.gitignore`, `.gitattributes`, `.gitlab-ci.yml`.
- Add `.idea` settings for copyright and scope.

##### Other

- Change license from GNU GPLv3 to Apache 2.0.
- Optimize CheckStyle-IDEA plugin configuration and integrate Spring Checks.
- Optimize IDEA CodeStyle configuration.

#### Refactor

- Relocate `ResponseBuilder` to the DTO package.
- Refactor `LocalCallFirstCluster` and `DubboExceptionFilter` to reduce complexity and improve readability.
- Optimize `RedissonService`:
  - Use non-deprecated `setNx` methods.

#### Tests

- Add modules:
  - `radp-smoke-tests-redis`
  - `radp-smoke-tests-logging`
  - `radp-smoke-tests-test`
- Add tests for:
  - Testcontainers
  - Embedded servers
  - `RedissonService` in `radp-redis-spring-boot-starter`

### 2.21

Same as **3.21** in terms of logging refactor, Redis key utilities, testing support, and most chore work unless otherwise noted.

#### Differences

- **Dependencies**
  - Override `kafka.version` from `3.1.2` to `3.9.0`.
  - Remove `maven-antrun-plugin.version`.
- **Build / Scaffold**
  - Uses a Spring Boot 2.x parent.
  - Scaffold default `radpVersion` is `2.21`.

---

## *.20.2

### 3.20.2

#### Fixes

- Fix `BaseException`:
  - Properly set the cause when a `Throwable` is passed as the last vararg parameter.
  - Handle placeholder mismatches when a `Throwable` is passed as a parameter.
- Add `ErrorCodeLoader.getErrMessage(String errCode)` to retrieve the raw message template without placeholder replacement.
- Remove deprecated `ListenableFuture` methods in `TtlThreadPoolTaskExecutor`.
- Add `serialVersionUID` for better serialization consistency.
- Use a more appropriate constructor in adaptive extension instantiation.
- Simplify instance checks in `ExceptionHandlingAsyncTaskExecutor`.
- Fix `ExtensionLoader` by replacing deprecated `newInstance` usage.
- Fix `AccessLogConfiguration` NPE.

#### Chore

##### Parent / Build

- Optimize profile `auto-update-local-catalog` by disabling default excludes.
- Fix profiles `code-review`, `unit-test`, and `integration-test`.
- Add profile `aggregate-reports`:
  - Integrate SonarQube.
  - Add `maven-jxr-plugin` for source cross-references.
  - Configure `maven-site-plugin` and `project-info-reports-plugin`.

##### Dependencies / Plugins

- Upgrade:
  - `maven-wrapper-plugin.version` from `3.2.0` to `3.3.2`.
  - `maven-archetype-plugin.version` from `3.2.0` to `3.3.1`.
  - `sonar-maven-plugin.version` from `3.9.1.2184` to `5.1.0.4751`.
  - `maven-surefire-plugin` from `3.0.0-M7` to `3.1.2`.
  - `maven-failsafe-plugin` from `3.0.0-M3` to `3.1.2`.
  - `org.jacoco:jacoco-maven-plugin` from `0.8.7` to `0.8.9`.
- In `DependencyManagement`, add `com.google.code.findbugs:annotations:3.0.1`.
- In `PluginManagement`, add:
  - `maven-jxr-plugin:3.3.0`
  - `maven-project-info-reports-plugin:3.6.2`
  - `maven-site-plugin:3.12.1`
  - `maven-checkstyle-plugin:3.3.1`
  - `archetype-packaging:3.2.0`

##### Scaffold

- Update scaffold default `radpVersion` to `3.20.2`.
- Optimize `.mvn`.
- Add `.gitattributes`.
- Add default `ApplicationTests`.
- Add GitHub issue templates.

#### Documentation

- Add Javadoc and resolve Javadoc warnings.

### 2.20.2

Same as **3.20.2** in terms of `BaseException`, `ErrorCodeLoader`, and most build/tooling improvements unless otherwise noted.

#### Differences

- Dependencies also add or override:
  - `maven-resources-plugin.version` from `3.2.0` to `3.3.1`.
- Scaffold default `radpVersion` is `2.20.2`.
- All plugin and dependency versions remain compatible with Spring Boot 2.x.

---

## *.19.1

### 3.19.1

#### Fixes

- Fix `ErrorCodeLoader`.
- Fix `BaseException` message formatting with parameters.
- Fix `ExceptionUtils.clientException`, `ExceptionUtils.serverException`, and
  `ExceptionUtils.thirdServiceException` placeholder handling.
- Fix default value for `PageResult#total`.

#### Documentation

- Add GitHub issue templates.

#### Scaffold

- Update scaffold default `radpVersion` to `3.19.1`.
- Fix placeholder handling in `XxxAssert` messages.

### 2.19.1

Same as **3.19.1** unless otherwise noted.

#### Differences

- Scaffold default `radpVersion` is `2.19.1`.
- Dependency versions align with the Spring Boot 2.x line.

---

## *.19

### 3.19

#### Features

- Optimize custom assertion utilities.

#### Chore

##### Parent

- Add profiles `env-sit` and `o-all-env`.

##### Dependencies

- Upgrade `com.google.cloud.tools:jib-maven-plugin` from `3.4.4` to `3.4.5`.

##### Scaffold

- Update scaffold default `radpVersion` to `3.19`.
- Optimize `build.sh`.
- Optimize `pom.xml` for `scaffold-xx` `xx-types` and `xx-app` modules.
- Fix `entrypoint.sh` (`Unrecognized option: --spring.config.additional-location=`).
- Optimize `application-local.yaml` for exposing env and loggers endpoints.
- Optimize `dev-ops/docker-compose-app.yaml`.

### 2.19

Same as **3.19** in terms of assert optimization and scaffold changes unless otherwise noted.

#### Differences

- Scaffold default `radpVersion` is `2.19`.
- Dependencies remain compatible with Spring Boot 2.x.

---

## *.18.1

### 3.18.1

#### Chore

##### Dependencies

- Override `liquibase.version` to `4.31.1`.

##### Scaffold

- Update scaffold default `radpVersion` to `3.18.1`.
- Optimize `entrypoint.sh`.
- Improve Liquibase support:
  - Fix duplicate initialization caused by filename inconsistencies recognized in changesets.
  - Improve the `changelog-init.yaml` example.
  - Improve `migration/20241018` directory structure and multi-environment support.

### 2.18.1

Same as **3.18.1** for Liquibase behavior unless otherwise noted.

#### Differences

- Also override `commons-lang3.version` to `3.17.0`.
- Scaffold default `radpVersion` is `2.18.1`.

---

## *.18

### 3.18

#### Chore

##### Dependencies

- Upgrade `org.sonatype.central:central-publishing-maven-plugin` from `0.6.0` to `0.7.0`.

##### Parent

- Add support for publishing snapshots to the Central portal.
- Add profile `env.uat`.
- Optimize profile `repo-central`:
  - Add properties `auto.layered.enabled` and `auto.assembly.enabled`.
- In profile `coding`, add property `user.docker.build.namespace`.

##### Scaffold

- Update scaffold default `radpVersion` to `3.18`.
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

### 2.18

Same as **3.18** unless otherwise noted.

#### Differences

- Scaffold default `radpVersion` is `2.18`.
- Dependency versions are aligned with Spring Boot 2.x.

---

## *.17

### 3.17

#### Chore

##### Parent

- Optimize profile `auto-jib`:
  - Split into `auto-jib-buildTar` and `auto-jib-dockerBuild` to work around `jib:buildTar` not supporting multi-platform builds.
- Add profiles:
  - `o-release`
  - `o-tar`
  - `publish-harbor`
  - `publish-artifactory`

##### Scaffold

- Update scaffold default `radpVersion` to `3.17`.
- Fix `.github/trigger-releases.yml`.
- Change generated project version from `1.0.0-SNAPSHOT` to `1.0-SNAPSHOT`.
- Optimize dev-ops `app` configuration.
- Optimize `.mvn/settings.xml`.

### 2.17

Same as **3.17** unless otherwise noted.

#### Differences

- Scaffold default `radpVersion` is `2.17`.
- All build and deployment configurations are tuned for the Spring Boot 2.x line.

---

## *.16.1

### 3.16.1

#### Fixes

- Fix deployment to Artifactory error:
  - `the parameters 'url' for wagon-maven-plugin are missing or invalid`.
- Fix GitHub Actions error:
  - `Unable to decrypt gpg passphrase`.

#### Chore

##### Parent

- Rename profile `auto-archetype-xx` to `auto-upload-catalog-xx`.

##### Scaffold

- Update `.mvn/settings.xml`.
- Update scaffold default `radpVersion` to `3.16.1`.

### 2.16.1

Same as **3.16.1** unless otherwise noted.

#### Differences

- Scaffold default `radpVersion` is `2.16.1`.

---

## *.16

### 3.16

#### Features

- Add tests for `radp-jasypt-spring-boot-starter`.

#### Fixes

- Fix `logging.pattern.console` not taking effect.

#### Chore

##### Parent

- Optimize profile `auto-jib`.
- Optimize `archetype-catalog.xml` deployment to a self-hosted Artifactory.

##### Dependencies

- In `PluginManagement`, add:
  - `maven-resources-plugin`
  - `maven-enforcer-plugin`
  - `wagon-maven-plugin`

##### Scaffold

- Remove `JasyptTest` and add a blank JUnit 5 `ApiTest`.
- Optimize `application.yaml` and add `application-jasypt.yaml`.
- Update scaffold default `radpVersion` to `3.15.1` (legacy value retained).
- Update `application-logback.yaml`.
- Optimize `application.yaml` and add `application-webmvc.yaml`.
- In `.mvn/settings.xml`, add property `auto.archetype.catalog.artifactory`.

##### Writerside

- Update `1.1.1-use_archetype_create_project.md`.

### 2.16

Same as **3.16** in terms of features and fixes unless otherwise noted.

#### Differences

- In Parent / PluginManagement, this line only adds `maven-resources-plugin` and `maven-enforcer-plugin` (no `wagon-maven-plugin`).
- Scaffold default `radpVersion` is `2.16`.

---

## *.15.1

### 3.15.1

#### Dependencies

- Upgrade `commons-io:commons-io` from `2.13.0` to `2.17.0` to ensure compatibility with `org.apache.tika:tika-core`.

#### Scaffold

- Add dev-ops files:
  - `docker-compose-pgadmin.yaml`
  - `docker-compose-redis-commander.yaml`
- Update scaffold default `radpVersion` to `3.15.1`.
- Fix dev-ops configuration in the scaffold.

### 2.15.1

Same as **3.15.1** for dev-ops changes unless otherwise noted.

#### Differences

- Scaffold default `radpVersion` is `2.15.1`.
- Exact dependency versions follow the 2.x POM set.

---

## *.15

### 3.15

#### Features

- Add `FileUtils` to `radp-common`.

#### Dependencies

- Upgrade Spring Boot from `3.2.3` to `3.4.4`.
- Upgrade Spring Cloud from `2023.0.0` to `2024.0.0`.
- In `DependencyManagement`, add:
  - `central-publishing-maven-plugin:0.6.0`
  - `maven-javadoc-plugin:3.5.0`
- Keep `space.x9x.radp:radp` using `maven-deploy-plugin` as defined in `radp-dependencies` rather than the one from `spring-boot-dependencies`.
- Fix `org.apache.dubbo:dubbo-dependencies-zookeeper` in `DependencyManagement`.
- Change Knife4j starter group from `com.github.xingfudeshi` to `com.github.xiaoming`.
- Remove redundant plugin version configuration in `radp-parent`.
- Remove duplicate plugin definitions in `radp-dependencies`.
- Fix an issue where transitive dependencies caused the Spring Framework version to be downgraded.

#### Parent

- Add properties:
  - `app.build.base_image.jdk8`
  - `app.build.base_image.jdk11`
  - `app.build.base_image.jdk17`

#### Scaffold

- Optimize dev-ops.
- Fix `application-dev.yaml`.
- Optimize Docker base image configuration.
- Update scaffold default `radpVersion` to `3.15`.

### 2.15

Same as **3.15** in terms of features and general dependency management patterns unless otherwise noted.

#### Differences

- Does not upgrade to Spring Boot 3.x / Spring Cloud 2024.x; remains on the Spring Boot 2.x line.
- Scaffold default `radpVersion` is `2.15`.

---

## *.14

### 3.14

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

- Resolve `AsyncTaskExecutionAutoConfiguration` deprecation issue related to `TaskExecutorBuilder`.

#### Dependencies

- Optimize plugin management:
  - Use `radp-dependencies` to manage Maven plugin versions.
  - Use `radp-parent` to manage plugin configuration.
- Switch Knife4j starter to `com.github.xingfudeshi:knife4j-openapi3-jakarta-spring-boot-starter:4.1.0`.

#### Parent

- Add properties:
  - `java.version`
  - `maven.compiler.source`
  - `maven.compiler.target`
  - `project.build.sourceEncoding`
  - and related defaults.

#### Documentation

- Update Writerside `about.md`.

### 2.14

Same as **3.14** with respect to modules and auto-configuration flags unless otherwise noted.

#### Differences

- Dependencies are tuned to Spring Boot 2.x and use the `springdoc-openapi` BOM.
- Scaffold entries for the 2.x line remain specific to Spring Boot 2.x.

---

## *.13

### 3.13

#### Scaffold

- Add PostgreSQL template `application.yaml`.
- Add properties `docker.build.base_image` and `docker.build.image_tag`.
- Improve `.gitignore`.
- Fix `maven-release-plugin` SCM configuration.
- Change Dockerfile base image from `eclipse-temurin:11-jdk` to `eclipse-temurin:17-jdk`.
- Optimize `docker-compose-app.yaml`.
- Adjust Hikari logging configuration in `application-local.yaml` and `application-dev.yaml`.
- Remove `application-homelab.yaml`.
- Optimize actuator configuration.
- Add `project.name` property.
- Fix issues where assembly JAR profiles activated by CLI did not work.
- Include `bin/catalina.sh`, `bin/catalina.bat`, `bin/startup.sh`, and `bin/shutdown.sh` in assembly.

#### Dependencies

- Replace `pl.project13.maven:git-commit-id-plugin:4.9.10` with `io.github.git-commit-id:git-commit-id-maven-plugin:6.0.0`.
- Upgrade `commons-io:commons-io` from `2.7` to `2.13.0`.
- Upgrade `springdoc-openapi` from `1.6.15` to `2.4.0`.
- Upgrade `com.baomidou:mybatis-plus-boot-starter:2.5.7` to `com.baomidou:mybatis-plus-spring-boot3-starter:2.5.7`.
- Use the `springdoc-openapi` BOM.
- Upgrade Spring Cloud from `2021.0.5` to `2023.0.0`.
- Upgrade `mybatis-spring-boot` from `2.1.4` to `3.0.4`.
- Upgrade MyBatis-Plus from `3.5.7` to `3.5.9` and use the MyBatis-Plus BOM.

#### Features

- Add `MultiResult` to `radp-spring-framework`.
- In `radp-logging-spring-boot-starter` logback template, add `NopStatusListener`.
- In `ResponseBuilder`, add `Result buildFailure(ErrorCode errorCode)`.
- In `SingleResult`, add `build()` method.
- Optimize profiles `auto-layered` and `auto-assembly`.

#### Fixes

- Fix missing `PaginationInnerInterceptor` by explicitly declaring `mybatis-plus-jsqlparser`.
- Fix GitLab CI/CD after upgrading to Spring Boot 3 (JDK 17).
- Fix GitHub Actions after upgrading to Spring Boot 3 (JDK 17).
- Fix `ResponseBuilder` bug in `radp-spring-framework`.
- Fix internal SPI registration under `META-INF`.
- Fix `ApplicationContextHelper#getBean`.
- Fix `RestExceptionHandler`.

### 2.13

Same as **3.13** conceptually (e.g. `MultiResult`, logging template, `RestExceptionHandler`, etc.) but implemented on Spring Boot 2.x.

#### Differences

- Uses `spring-boot-parent` `2.7.18`.
- Spring Cloud, SpringDoc, and MyBatis versions follow the Spring Boot 2.x compatibility matrix.
- Docker base image and other scaffold details are tuned for JDK 8/11-era 2.x projects.

---

## *.12

### 3.12

- Move mainline development to **JDK 17 + Spring Boot 3.x**:
  - Add `arm64` support for `netty-resolver-dns-native-macos`.
  - Switch from `javax.validation:validation-api` to `jakarta.validation:jakarta.validation-api`.
  - Switch from `javax.servlet.*` to `jakarta.servlet.*`.
  - Allow the `future` branch to trigger GitHub Actions.

### 2.12

- Introduce the `future` branch for JDK 17 + Spring Boot 3.x development, while 2.12 itself remains on the Spring Boot 2.x line and serves as a transition point.

---

## 0.11

- Fix archetype `auto-release` profile not working.
- Fix archetype import errors.

## 0.10

- Fix GitHub Action Maven cache not working.
- Change default JDK from `java 8.0.432+6-tem` to `java 8.0.442+6-amzn` to support arm64.
- Update archetype `application-dev.yaml`.

## 0.9

- Optimize GitHub Action Maven cache configuration.
- Optimize `auto-assembly` profile.

## 0.8

- Update archetype structure.
- Optimize GitHub Actions workflows.
- Add `rapd-design-pattern-framework`:
  - Provide abstractions for design pattern usage to reduce complexity and standardize style.
  - Include a decision-tree pattern abstraction.
- Archetype:
  - Optimize assembly.
  - Optimize layered build.

## 0.7

- Add support for publishing GitLab Pages via GitLab CI/CD.
- Optimize GitLab CI pipeline by using remote templates.
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

- Support publishing artifacts to Central, GitHub Packages, and a self-hosted Artifactory.
- Use GitHub Actions to build and publish GitHub Pages.
- Add a DDD scaffold.
- Add core common components.
