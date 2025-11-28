/*
 * Copyright 2012-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package space.x9x.radp.spring.security.jwt.config;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.annotation.security.PermitAll;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import space.x9x.radp.commons.json.JacksonUtils;
import space.x9x.radp.spring.security.jwt.filter.JwtAuthorizationFilter;
import space.x9x.radp.spring.security.web.handler.ForbiddenAccessDeniedHandler;
import space.x9x.radp.spring.security.web.handler.UnauthorizedEntryPoint;

/**
 * Centralizes default HttpSecurity configuration for JWT-based authentication and
 * authorization.
 * <p>
 * This configurer provides a comprehensive security setup for JWT-based authentication:
 * <ul>
 * <li>Disables CSRF protection as it's unnecessary for token-based authentication</li>
 * <li>Configures stateless sessions to align with JWT principles</li>
 * <li>Registers authentication entry point and access-denied handler for proper error
 * handling</li>
 * <li>Applies URL authorization rules from {@link JwtConfig}</li>
 * <li>Automatically detects and processes @PermitAll annotations for endpoint access</li>
 * <li>Registers {@link JwtAuthorizationFilter} in the security filter chain</li>
 * </ul>
 *
 * <p>
 * Key features:
 * <ul>
 * <li>Flexible configuration through {@link JwtConfig}</li>
 * <li>Support for both authenticated and anonymous access paths</li>
 * <li>Integration with Spring MVC handler mappings</li>
 * <li>Customizable authorization rules through
 * {@link JwtAuthorizeHttpRequestsCustomizer}</li>
 * </ul>
 *
 * <p>
 * Basic usage in application code: <pre>
 * jwtSecurityConfigurer.configure(httpSecurity);
 * </pre>
 *
 * <p>
 * For custom authorization rules: <pre>
 * jwtSecurityConfigurer.configure(httpSecurity, customizers);
 * </pre>
 *
 * @author x9x
 * @since 2025-11-23 13:31
 * @see JwtConfig
 * @see JwtAuthorizationFilter
 * @see JwtAuthorizeHttpRequestsCustomizer
 */
@RequiredArgsConstructor
@Slf4j
public class JwtSecurityConfigurer {

	/**
	 * 可选的 JWT 认证过滤器.
	 * <p>
	 * 当为 {@code null} 时, 当前配置仅负责:
	 * <ul>
	 * <li>禁用 CSRF;</li>
	 * <li>配置无状态会话;</li>
	 * <li>注册认证入口点和访问拒绝处理器;</li>
	 * <li>应用 URL 授权规则;</li>
	 * <li>不再从请求中解析 JWT.</li>
	 * </ul>
	 */
	private final JwtAuthorizationFilter jwtAuthorizationFilter;

	private final JwtConfig jwtConfig;

	private final UnauthorizedEntryPoint unauthorizedEntryPoint;

	private final ForbiddenAccessDeniedHandler forbiddenAccessDeniedHandler;

	private final PathMatcher pathMatcher;

	private final List<RequestMappingHandlerMapping> requestMappingHandlerMappings;

	private final List<JwtAuthorizeHttpRequestsCustomizer> authorizeHttpRequestsCustomizers;

	/**
	 * Creates a new {@code JwtSecurityConfigurer} instance with a default path matcher
	 * and the specified components for configuring JWT-based security.
	 * @param jwtAuthorizationFilter the {@link JwtAuthorizationFilter} used for JWT
	 * authentication and authorization.
	 * @param jwtConfig the {@link JwtConfig} containing configurations such as header
	 * name and token validity.
	 * @param unauthorizedEntryPoint the {@link UnauthorizedEntryPoint} to handle
	 * unauthorized requests.
	 * @param forbiddenAccessDeniedHandler the {@link ForbiddenAccessDeniedHandler} to
	 * handle access denial responses.
	 * @param handlerMappings the list of {@link RequestMappingHandlerMapping} instances
	 * for mapping HTTP requests.
	 * @param authorizeHttpRequestsCustomizers the list of
	 * {@link JwtAuthorizeHttpRequestsCustomizer} for customizing authorization rules.
	 * @return a new instance of {@code JwtSecurityConfigurer} configured with the
	 * provided arguments.
	 */
	public static JwtSecurityConfigurer withDefaultPathMatcher(JwtAuthorizationFilter jwtAuthorizationFilter,
			JwtConfig jwtConfig, UnauthorizedEntryPoint unauthorizedEntryPoint,
			ForbiddenAccessDeniedHandler forbiddenAccessDeniedHandler,
			List<RequestMappingHandlerMapping> handlerMappings,
			List<JwtAuthorizeHttpRequestsCustomizer> authorizeHttpRequestsCustomizers) {

		return new JwtSecurityConfigurer(jwtAuthorizationFilter, jwtConfig, unauthorizedEntryPoint,
				forbiddenAccessDeniedHandler, new AntPathMatcher(), handlerMappings, authorizeHttpRequestsCustomizers);
	}

	/**
	 * Apply the default JWT-based security configuration to the given
	 * {@link HttpSecurity}. Delegates to {@link #configure(HttpSecurity, List)} with no
	 * extra customizers.
	 * @param httpSecurity the {@link HttpSecurity} to configure
	 * @throws Exception if an error occurs while applying configuration
	 */
	public void configure(HttpSecurity httpSecurity) throws Exception {
		configure(httpSecurity, Collections.emptyList());
	}

	/**
	 * Apply JWT-based security configuration with optional authorization customizers.
	 * @param httpSecurity the {@link HttpSecurity} to configure
	 * @param authorizeHttpRequestsCustomizers additional customizers to contribute
	 * request authorization rules
	 * @throws Exception if an error occurs while applying configuration
	 */
	public void configure(HttpSecurity httpSecurity,
			List<JwtAuthorizeHttpRequestsCustomizer> authorizeHttpRequestsCustomizers) throws Exception {
		String[] authenticatedUrls = Optional.ofNullable(this.jwtConfig.getAuthenticatedUrls())
			.orElse(Collections.emptyList())
			.toArray(new String[0]);
		String[] permittedUrls = Optional.ofNullable(this.jwtConfig.getPermitAllUrls())
			.orElse(Collections.emptyList())
			.toArray(new String[0]);
		String[] anonymousUrls = Optional.ofNullable(this.jwtConfig.getAnonymousUrls())
			.orElse(Collections.emptyList())
			.toArray(new String[0]);
		String[] permitAllUrlsFromAnnotations = getPermitAllUrlsFromAnnotations();

		// ===== 1. 仅在启用 JWT 过滤器时, 才接管 CSRF/Session/ExceptionHandling =====
		if (this.jwtAuthorizationFilter != null) {
			httpSecurity
				// 基于 TOKEN 机制, 可屏蔽 CSRF 防护
				.csrf(AbstractHttpConfigurer::disable)
				// 基于 TOKEN 机制, 不需要 Session
				.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				// 添加自定义 未登录和未授权 结果返回
				.exceptionHandling(eh -> eh.authenticationEntryPoint(this.unauthorizedEntryPoint)
					.accessDeniedHandler(this.forbiddenAccessDeniedHandler));

			log.debug(
					"JWT authorization filter is enabled: CSRF disabled, session stateless, custom entry point active.");
		}
		else {
			// 未启用 JWT 过滤器:
			// - 使用 Spring Security 默认策略
			// - 显式启用表单登录, 让未认证访问受保护资源时跳转到 /login
			httpSecurity.formLogin() // 使用默认 login 页面和 loginProcessingUrl("/login")
				.and()
				.httpBasic(); // 可选: 同时支持 HTTP Basic

			log.debug("No JwtAuthorizationFilter found. Enable formLogin/httpBasic with default entry point.");
		}

		// ===== 2. 仅在启用 JWT 过滤器时, 才把它加入过滤器链 =====
		if (this.jwtAuthorizationFilter != null) {
			httpSecurity.addFilterBefore(this.jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);
			log.debug("Registered JwtAuthorizationFilter in SecurityFilterChain.");
		}
		else {
			log.debug(
					"No JwtAuthorizationFilter registered. Starter will not parse JWT, but will keep stateless session and authorization rules.");
		}

		// ===== 3. URL 授权规则: 无论是否启用 JWT 过滤器, 都统一生效 =====
		httpSecurity
			// 设置每个请求的权限 (Spring Security 5.x 使用 antMatchers, 6.x 使用 requestMatchers)
			.authorizeRequests(registry -> {
				// 1. 全局共享规则
				// 1.1 认证: 对于显式配置需要认证的 url, 必须认证
				registry.antMatchers(authenticatedUrls).authenticated();
				// 1.2 放行: 允许对网站 静态资源 的无授权访问
				registry.antMatchers(HttpMethod.GET, "/*.css", "/*.js", "/*.html").permitAll();
				// 1.3 放行: 对配置文件中指定的 permit-all-urls 接口允许未授权访问(比如登录/注册等)
				registry.antMatchers(permittedUrls).permitAll();
				// 1.4 放行: 对 @PermitAll 标注的接口允许未授权访问
				registry.antMatchers(permitAllUrlsFromAnnotations).permitAll();
				// 1.5 放行: 对配置文件中指定的 anonymous-urls 接口允许未授权访问
				registry.antMatchers(anonymousUrls).permitAll();

				// 2. 引入该 starter 的项目的自定义规则
				authorizeHttpRequestsCustomizers.forEach(customizer -> customizer.customize(registry));

				// 3. 兜底规则
				// 除了上述之外的其他请求, 都必须认证
				registry.anyRequest().authenticated();
			});

		log.debug("Authenticated urls: {}", JacksonUtils.toJSONString(authenticatedUrls));
		log.debug("PermitAll urls (from config): {}", JacksonUtils.toJSONString(permittedUrls));
		log.debug("PermitAll urls (from annotations): {}", JacksonUtils.toJSONString(permitAllUrlsFromAnnotations));
		log.debug("Anonymous urls: {}", JacksonUtils.toJSONString(anonymousUrls));
	}

	private String[] getPermitAllUrlsFromAnnotations() {
		Set<String> urls = new LinkedHashSet<>();

		this.requestMappingHandlerMappings
			.forEach(mapping -> mapping.getHandlerMethods().forEach((requestMappingInfo, handlerMethod) -> {
				boolean hasPermitAll = AnnotatedElementUtils.hasAnnotation(handlerMethod.getBeanType(), PermitAll.class)
						|| AnnotatedElementUtils.hasAnnotation(handlerMethod.getMethod(), PermitAll.class);

				// Skip if the method/class is not annotated with @PermitAll
				// or if there are no URL patterns defined
				if (!hasPermitAll || requestMappingInfo.getPatternsCondition() == null) {
					return;
				}

				requestMappingInfo.getPatternsCondition().getPatterns().forEach(pattern -> {
					log.debug("Detected @PermitAll mapping: {}", pattern);
					urls.add(pattern);
				});
			}));

		return urls.toArray(new String[0]);
	}

}
