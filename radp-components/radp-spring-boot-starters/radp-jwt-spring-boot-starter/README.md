# radp-jwt-spring-boot-starter

基于 Spring Security 的 JWT 认证/授权一站式 Starter。通过少量配置即可让应用具备无会话（Stateless）的 Token
认证能力，并提供默认的安全链路、异常处理、路径放行规则以及可扩展的授权自定义接口。

本 Starter 以 radp-spring-security 为核心实现，自动装配 JwtTokenProvider、JwtTokenService、JwtAuthorizationFilter
等组件，同时提供默认的 SecurityFilterChain（可关闭），帮助你快速落地“登录获取 Token + 持 Token 访问受保护接口”的模式。

• 适用场景：BFF/后端 API 服务、前后端分离项目、网关后端服务的鉴权等。

## 特性

- 开箱即用
  - 自动装配 JWT
    核心组件：JwtTokenProvider、JwtTokenService、PasswordEncoder、UnauthorizedEntryPoint、ForbiddenAccessDeniedHandler
  - 可选装配 JwtAuthorizationFilter（默认开启）与默认 SecurityFilterChain（默认开启）

- 路径授权规则（HttpSecurity 统一配置）
  - 支持 authenticated-urls、permit-all-urls、anonymous-urls 三类路径集合
  - 自动发现并放行标注了 @PermitAll 的接口
  - 默认放行 GET 方式下的 /*.css、/*.js、/*.html 静态资源

- 过滤器与放行的关系（重要）
  - anonymous-urls：JWT 过滤器将完全跳过这些路径，不要求携带 Token
  - permit-all-urls：在授权阶段放行，但如果未加入 anonymous-urls，JWT 过滤器仍会先要求 Token
  - authenticated-urls：必须认证后方可访问

- SPI 扩展
  - 通过 JwtAuthorizeHttpRequestsCustomizer 可在默认授权规则之后继续追加自定义的授权匹配

- 可插拔 Token 存储
  - 提供 JwtTokenStore 接口，默认内存实现为“无操作、永远有效”（用于开发测试）
  - 你可以提供自己的 JwtTokenStore 实现以支持黑名单/踢出登录等能力

## 快速开始

### 1. 引入依赖

```xml

<dependency>
	<groupId>space.x9x.radp</groupId>
	<artifactId>radp-jwt-spring-boot-starter</artifactId>
</dependency>
```

### 2. 基本配置（application.yml）

```yaml
radp:
  security:
    jwt:
      config:
        enabled: true                        # 启用 JWT Starter

        # 密钥配置（二选一）
        # 方式 A：直接使用原始字符串（>=32 字符，包含大小写/数字/特殊字符）
        secret: "change-me-to-a-strong-secret-at-least-32-chars"
        # 方式 B（可选）：若你希望以 BASE64 形式提供 secret，设置 base64-secret 任意非空值以启用 BASE64 解码模式
        # base64-secret: "any-non-empty-value"   # 启用后，secret 将被按 BASE64 解码

        header: Authorization                 # 读取 Token 的请求头，默认 Authorization
        token-validity-in-seconds: 3600       # Token 有效期，默认 1000 秒（示例中改为 1 小时）
        token-validity-in-seconds-for-remember-me: 2592000 # 记住我有效期，默认 30 天

        # 注意：
        # - 若某接口需要“完全不携带 Token 即可访问”，请务必放入 anonymous-urls
        # - permit-all-urls 仅在授权阶段放行，但若未在 anonymous-urls 中，JWT 过滤器仍会先校验 Token
        anonymous-urls:
          - /auth/**
          - /public/**
        permit-all-urls:
          - /swagger-ui/**
          - /v3/api-docs/**
        authenticated-urls:
          - /api/**

        # 开关
        use-default-authentication-filter: true   # 是否使用默认 JwtAuthorizationFilter（默认 true）
        use-default-security-filter-chain: true   # 是否注入默认 SecurityFilterChain（默认 true）
```

### 3. 登录发放 Token（示例）

```java

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

	private final JwtTokenService jwtTokenService;

	@PostMapping("/login")
	@PermitAll // 也可通过 anonymous-urls 放行；为避免被过滤器拦截，推荐两者至少其一生效
	public AccessToken login(@RequestBody LoginRequest req) {
		// LoginUserDetails 是 RADP 公共模型，封装登录所需信息
		LoginUserDetails user = new LoginUserDetails(req.getUsername(), req.getPassword(), req.isRememberMe());
		Map<String, Object> claims = Map.of("tenant", "t1");
		return jwtTokenService.authenticate(user, claims);
	}
}

@Data
class LoginRequest {
	private String username;

	private String password;

	private boolean rememberMe;
}
```

发起登录请求后会得到 AccessToken：

```json
{
  "tokenType": "Bearer",
  "value": "eyJhbGciOiJIUzUxMiIs...",
  "expiration": "2025-12-31T12:00:00Z"
}
```

### 4. 携带 Token 访问受保护接口

```http
GET /api/user/profile HTTP/1.1
Host: example.com
Authorization: Bearer <your-jwt-token>
```

## 配置项说明

配置前缀：`radp.security.jwt.config`

- enabled：是否启用 Starter，默认 false（需显式开启）
- header：读取 Token 的请求头名称，默认 `Authorization`
- secret：签名密钥（>=32 字符），生产环境务必妥善保密，不要输出到日志
- base64-secret：可选，非空即表示以 BASE64 方式解析 secret
- token-validity-in-seconds：Token 有效期（秒），默认 1000
- token-validity-in-seconds-for-remember-me：记住我有效期（秒），默认 2592000（30 天）
- anonymous-urls：完全匿名访问（过滤器跳过 + 授权放行），典型用在登录、注册、开放接口
- permit-all-urls：授权放行但过滤器不跳过（若未在 anonymous-urls 中，仍需携带 Token 才能不被过滤器拦截）
- authenticated-urls：强制认证访问
- use-default-authentication-filter：是否注入默认 JwtAuthorizationFilter，默认 true
- use-default-security-filter-chain：是否注入默认 SecurityFilterChain，默认 true

路径匹配说明：

- 使用 Ant 风格（如 `/api/**`），与 Spring 的 AntPathMatcher 行为一致
- 过滤器使用 servletPath 进行路径匹配，通常更贴近应用实际处理路径

## 自动装配与默认行为

启用后（enabled=true）：

- Bean
  - AuthenticationManager（缺省则装配）
  - JwtTokenProvider、JwtTokenService
  - PasswordEncoder（DelegatingPasswordEncoder）
  - UnauthorizedEntryPoint、ForbiddenAccessDeniedHandler
  - PathMatcher（AntPathMatcher）
  - JwtAuthorizationFilter（在 use-default-authentication-filter=true 时装配）

- 默认 SecurityFilterChain（可关闭）
  - 在 use-default-security-filter-chain=true 且应用自身未定义 SecurityFilterChain Bean 时，自动创建
  - 自动应用 JwtSecurityConfigurer，注册默认授权规则与（可选的）JWT 过滤器

## 自定义与扩展

### 1) 自定义授权规则（SPI）

实现并注册 `JwtAuthorizeHttpRequestsCustomizer` Bean，在默认规则基础上追加授权配置：

```java

@Bean
public JwtAuthorizeHttpRequestsCustomizer myCustomizer() {
	return registry -> registry.antMatchers(HttpMethod.POST, "/orders/**").hasRole("ADMIN");
}
```

### 2) 自定义 SecurityFilterChain

关闭默认的 SecurityFilterChain：

```yaml
radp:
  security:
    jwt:
      config:
        use-default-security-filter-chain: false
```

并在你的配置类中直接使用 `JwtSecurityConfigurer`：

```java

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
	private final JwtSecurityConfigurer jwtSecurityConfigurer;

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		jwtSecurityConfigurer.configure(http); // 或者传入自定义 customizers 列表
		return http.build();
	}
}
```

### 3) 替换/关闭默认 JwtAuthorizationFilter

- 关闭默认过滤器：`use-default-authentication-filter: false`
- 自定义 Bean：声明你自己的 `JwtAuthorizationFilter`（或扩展实现）作为 Bean 即可被 `JwtSecurityConfigurer` 使用

### 4) 自定义 Token 存储

实现 `JwtTokenStore` 以支持黑名单、踢出登录、单端登录等业务：

```java

@Component
public class RedisJwtTokenStore implements JwtTokenStore {
	public void storeAccessToken(AccessToken token) { /* ... */ }

	public boolean validateAccessToken(AccessToken token) { /* ... */
		return true;
	}

	public void removeAccessToken(AccessToken token) { /* ... */ }
}
```

当容器中存在你自己的 `JwtTokenStore` Bean 时，将自动替换默认内存实现。

## 常见问题（FAQ）

1) 登录接口需要匿名访问，应该配置到哪里？

- 推荐配置到 `anonymous-urls`。这样 JWT 过滤器会直接跳过，不要求携带 Token；同时授权阶段也会放行。
- 仅配置到 `permit-all-urls` 并不足以避免过滤器校验，仍可能被过滤器拦截。

2) 我已经定义了自己的 SecurityFilterChain，还会加载 Starter 的默认链吗？

- 不会。默认链通过 `@ConditionalOnMissingBean(SecurityFilterChain.class)` 条件装配，你的自定义链会覆盖默认行为。

3) 如何改变读取 Token 的 Header 名称？

- 配置 `radp.security.jwt.config.header`，例如设置为 `X-Auth-Token`。

4) 如何自定义未认证/未授权的响应格式？

- 提供自定义的 `UnauthorizedEntryPoint` 与 `ForbiddenAccessDeniedHandler` Bean，即可覆盖默认实现。

5) 密钥如何生成与管理？

- 请使用长度不小于 32 字符的强随机密钥，生产环境切勿写死在代码仓库与日志。
- 可使用 `openssl rand -base64 64` 生成随机密钥；如使用 BASE64 形式，参考上文配置项说明。

## 相关类（来自 radp-spring-security）

- `JwtConfig`：JWT 配置载体（header、secret、有效期、路径集合等）
- `JwtTokenProvider`：创建、校验 Token，并从 Token 解析 Authentication
- `JwtTokenService`：基于 AuthenticationManager 执行登录认证并签发 Token
- `JwtAuthorizationFilter`：从请求头提取/校验 Token，并将认证信息放入 SecurityContext
- `JwtSecurityConfigurer`：集中化的 HttpSecurity 配置器，应用路径授权规则、注册过滤器与异常处理等
- `JwtAuthorizeHttpRequestsCustomizer`：授权规则扩展 SPI

## 许可证

本项目使用 Apache License 2.0，详见仓库根目录的 LICENSE 文件。

