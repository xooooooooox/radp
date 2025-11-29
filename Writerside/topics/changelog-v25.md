# v25

## Features

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

## Bug fixes

- Fix `AutofillMetaObjectHandler#updateFill` not being invoked.

## Dependencies

- In `DependencyManagement`, add:
  - `org.bouncycastle:bcprov-jdk15to18:1.81`
  - `org.passay:passay:1.6.6`

## Chore — Scaffold

- Standardize log file paths using `LOG_HOME` and `LOG_FILE_BASENAME`.
- Optimize `.idea` code styles.
- Optimize `checkstyle-suppressions.xml` and suppress `HideUtilityClassConstructor` globally.
- Optimize `application-local.yaml`.

## Documentation

- Add comments in `application-logback.yaml` explaining how to adjust the log file name using `LOG_FILE_BASENAME`.


