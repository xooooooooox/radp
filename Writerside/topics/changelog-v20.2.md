# v3.20.2

## Fixes

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

## Build

- Optimize profile `auto-update-local-catalog` by disabling default excludes.
- Fix profiles `code-review`, `unit-test`, and `integration-test`.
- Add profile `aggregate-reports`:
  - Integrate SonarQube.
  - Add `maven-jxr-plugin` for source cross-reference.
  - Configure `maven-site-plugin` and `project-info-reports-plugin`.

## Dependencies / Plugins

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

## Scaffold

- Optimize `.mvn`.
- Add `.gitattributes`.
- Add default `ApplicationTests`.
- Add GitHub issue templates.

## Documentation

- Add Javadoc and resolve Javadoc warnings.
