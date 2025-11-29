# v26

## Features

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

## Dependencies

- Remove `commons-io` to resolve a transitive conflict with `fastexcel`.

## Fixes

- Fix `RestExceptionHandler`.
- Update Checkstyle configuration paths to use `${maven.multiModuleProjectDirectory}` for improved flexibility.

## Scaffold

- Update scaffold default `radpVersion` to `*.26` (actual value differs per major line).








