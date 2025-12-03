# v27

## Features

- Add starter `radp-jwt-spring-boot-starter`, implementing JWT-based authentication and authorization.
- Add starter `radp-oauth2-spring-boot-starter`
- Optimize `BasePOAutoFillStrategy` to use `LoginUserResolver` to obtain the current logged-in user from context.
- Add utility methods in `ServletUtils` to wrap HTTP responses with JSON content.
- Add `#addOrder()` utility method to `MybatisUtils`.
- Add the `#deleteBatch()` utility method to `BaseMapperX`.
- Optimize `ErrorCodeLoader`
  - Implemented resource merging for `META-INF/error/message*.properties` with override priority (libraries < app
    classes).
  - Added support for Spring `MessageSource` to enable application-specific i18n error messages.
  - Introduced fallback mechanism ensuring error resolution robustness: app messages > internal bundle > error code
    itself.
  - Improved thread-safety in message resource initialization.

## Fix

- Fix `RestExceptionHandler` leaking sensitive exception details to the frontend.

## Refactor

- Rename `SingleResult` and `PageResult` methods:
  - `#build` → `#ok`
  - `#buildFailure` → `#failed`
- Optimize custom `AbstractAssert`, removing inheritance from Spring’s `Assert`.
- Optimize `BaseConvertor`.
- Adjust built-in error codes so that codes below `1000` are reserved for framework-level internal use.
- Rename the `PageParam` constant `PAGE_SIZE_NONE` to `NO_PAGINATION`.

## Scaffold

- Add `@Contract` annotations to `ClientAssert`, `ServerAssert`, and `ThirdServiceAssert`, and refactor null/nonnull
  handling.
- Add module `xxx-case` to `scaffold-std`.
- Replace hardcoded empty string with `PREFIX` in `RedisKeyProvider`.
- Restructure package organization in the `xxx-api` layer.
- Add `checkstyle-idea.xml`.
- Fix Writerside setup.
- Update default resource bundle `message.properties`.

## Documentation

- Fix Javadoc error: `MobileConvert`, `Sm4StringEncryptor`, `JasyptUtils`, `JwtAutoConfiguration`,
- Migrate CHANGELOG to writerside.

## Tests

- Add tests for `MessageFormatter`, `MessageFormatUtils`, and `ResponseBuilder`.
- Fix tests for `RedissonServiceSmokeTest`, `ElasticsearchKibanaTest`
