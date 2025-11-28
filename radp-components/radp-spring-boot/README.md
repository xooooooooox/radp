# radp-spring-boot

Spring Boot integration for RADP platform, providing auto-configuration, common beans and conventions for RADP-based
applications.

This document focuses on **error message resolution & i18n** integration.

---

## Error messages & i18n

### What you get

When your application depends on:

- `radp-spring-framework`
- `radp-spring-boot` (directly or via a starter)

then:

- `radp-spring-boot` auto-configures a dedicated `MessageSource` for error codes.
- `ErrorCodeLoader` in `radp-spring-framework` is automatically wired to that `MessageSource`.
- All RADP components that use `ErrorCodeLoader` (asserts, exceptions, REST responses, security, etc.) transparently
  support:
  - **Per-key merge** across modules (libs + app).
  - **App overrides library** behavior.
  - **i18n** based on `LocaleContextHolder` / `Accept-Language`.

You do **not** need to change existing callers such as:

- `new ErrorCode("TEST_0001", ...)`
- `SingleResult.failed("TEST_0001", ...)`
- `ExceptionUtils.serverException("TEST_0001", ...)`
- `ServerAssert.notNull(obj, "TEST_0001", ...)`
- etc.

---

### 1. Where to put your error message files

In your application A, create:

```text
src/main/resources/META-INF/error/
message.properties
message_zh_CN.properties
message_en_US.properties   # optional, more locales
```

#### Example: `message.properties` (default locale, e.g. English)

```properties
TEST_0001=hello {}
SEC_0400=Token is required
SEC_0401=Token is invalid
```

#### Example: `message_zh_CN.properties` (Chinese)

```properties
TEST_0001=你好 {}
SEC_0400=令牌不能为空
SEC_0401=令牌不合法
```

Other RADP modules (e.g. `radp-spring-security`) can also define:

```text
src/main/resourcesMETA-INF/error/
message.properties
message_zh_CN.properties
```

Those will be merged with your application’s messages.

---

### 2. Precedence & merge rules

The auto-configured `MessageSource` uses the following basenames:

```text
classpath:/META-INF/error/message
classpath*:/META-INF/error/message
classpath:/META-INF/error/internal
```

Resolution order:

1. **Application messages (non-wildcard)**  
   `classpath:/META-INF/error/message*` (your app module only)

2. **All messages on classpath**  
   `classpath*:/META-INF/error/message*` (all jars / modules)

3. **Internal framework defaults**  
   `classpath:/META-INF/error/internal*`

Spring will try these basenames **in the declared order** and return the first match.  
Therefore, for the same key:

```text
Application > Libraries > Internal
```

This means:

- You can **add new keys** in your app that libs don’t have.
- You can **override lib-defined keys** in your app.
- You never lose library defaults for codes you don’t override.
- If no key is found anywhere, the code itself is used as the message.

---

### 3. How to use it in code

Anywhere in your code (app or library):

```java
import space.x9x.radp.spring.framework.error.ErrorCodeLoader;

String msg = ErrorCodeLoader.getErrMessage("TEST_0001", 123);
// For zh-CN: "你好 123"
// For default/en: "hello 123"
```

You usually don’t need to call `ErrorCodeLoader` directly because it is already used in:

- `ErrorCode`
- `SingleResult.failed(...)`
- `ExceptionUtils.*Exception(...)`
- `ServerAssert` / `ClientAssert` / `ThirdServiceAssert`

- Other RADP framework components

Those will automatically resolve messages via the configured `MessageSource`.

---

### 4. i18n behavior

#### 4.1. Web applications

In Spring Boot Web MVC:

- Locale is resolved from `Accept-Language` headers (by default).
- `ErrorCodeLoader` uses `LocaleContextHolder.getLocale()` when a `MessageSource` is present.

So, for a REST endpoint that uses RADP’s result/exception model, the response message will depend on the request’s
locale.

Example controller:

```java
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import space.x9x.radp.spring.framework.dto.SingleResult;
import space.x9x.radp.spring.framework.error.ErrorCodeLoader;

@RestController
class DemoController {

	@GetMapping("/api/demo")
	public SingleResult<String> demo() {
		String message = ErrorCodeLoader.getErrMessage("TEST_0001", 123);
		return SingleResult.ok("payload", message);
	}

}
```

Requests:

- `GET /api/demo` with `Accept-Language: zh-CN`  
  → message `"你好 123"`

- `GET /api/demo` with `Accept-Language: en`  
  → message `"hello 123"`

#### 4.2. Non-web or tests: manual locale

You can set the locale explicitly:

```java
import java.util.Locale;

import org.springframework.context.i18n.LocaleContextHolder;

import space.x9x.radp.spring.framework.error.ErrorCodeLoader;

LocaleContextHolder.setLocale(Locale.SIMPLIFIED_CHINESE);

String zh = ErrorCodeLoader.getErrMessage("TEST_0001", 123);

LocaleContextHolder.

setLocale(Locale.US);

String en = ErrorCodeLoader.getErrMessage("TEST_0001", 123);
```

---

### 5. Overriding configuration

If you need to customize the `MessageSource` used for error codes:

1. Define your own bean named `radpErrorMessageSource` in the application:
   ```java
   @Bean(name = "radpErrorMessageSource")
   public MessageSource customRadpErrorMessageSource() { ... }
   ```
2. In that bean, call:
   ```java
   ErrorCodeLoader.setMessageSource(customMessageSource);
   ```
3. `radp-spring-boot` auto-configuration will back off (`@ConditionalOnMissingBean(name = "radpErrorMessageSource")`).

This allows full control over:

- Which basenames are loaded.
- Cache times / reload behavior.
- Default locale and fallback strategies.
