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
 * Centralizes default HttpSecurity configuration for JWT.
 * <p>
 * This configurer:
 * <ul>
 * <li>Disables CSRF</li>
 * <li>Configures stateless sessions</li>
 * <li>Registers authentication entry point and access denied handler</li>
 * <li>Applies URL authorization from {@link JwtConfig}</li>
 * <li>Detects @PermitAll mappings</li>
 * <li>Registers {@link JwtAuthorizationFilter}</li>
 * </ul>
 *
 * Application code usually only needs to call:
 *
 * <pre>
 * jwtSecurityConfigurer.configure(httpSecurity);
 * </pre>
 *
 * @author x9x
 * @since 2025-11-23 13:31
 */
@RequiredArgsConstructor
@Slf4j
public class JwtSecurityConfigurer {

	private final JwtAuthorizationFilter jwtAuthorizationFilter;

	private final JwtConfig jwtConfig;

	private final UnauthorizedEntryPoint unauthorizedEntryPoint;

	private final ForbiddenAccessDeniedHandler forbiddenAccessDeniedHandler;

	private final PathMatcher pathMatcher;

	private final List<RequestMappingHandlerMapping> requestMappingHandlerMappings;

	private final List<JwtAuthorizeHttpRequestsCustomizer> authorizeHttpRequestsCustomizers;

	/**
	 * Convenience factory when user code doesn't care about a custom PathMatcher.
	 */
	public static JwtSecurityConfigurer withDefaultPathMatcher(JwtAuthorizationFilter jwtAuthorizationFilter,
			JwtConfig jwtConfig, UnauthorizedEntryPoint unauthorizedEntryPoint,
			ForbiddenAccessDeniedHandler forbiddenAccessDeniedHandler,
			List<RequestMappingHandlerMapping> handlerMappings,
			List<JwtAuthorizeHttpRequestsCustomizer> authorizeHttpRequestsCustomizers) {

		return new JwtSecurityConfigurer(jwtAuthorizationFilter, jwtConfig, unauthorizedEntryPoint,
				forbiddenAccessDeniedHandler, new AntPathMatcher(), handlerMappings, authorizeHttpRequestsCustomizers);
	}

	public void configure(HttpSecurity httpSecurity) throws Exception {
		configure(httpSecurity, Collections.emptyList());
	}

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

		httpSecurity
			// 基于 TOKEN 机制, 可屏蔽 CSRF 防护
			.csrf(AbstractHttpConfigurer::disable)
			// 基于 TOKEN 机制, 不需要 Session
			.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			// 添加自定义 未登录和未授权 结果返回
			.exceptionHandling(eh -> eh.authenticationEntryPoint(this.unauthorizedEntryPoint)
				.accessDeniedHandler(this.forbiddenAccessDeniedHandler))

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

		// register JWT filter
		httpSecurity.addFilterBefore(this.jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);

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
