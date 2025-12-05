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

package space.x9x.radp.jwt.spring.boot.autoconfigure;

import java.util.Collections;
import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import space.x9x.radp.jwt.spring.boot.env.JwtProperties;
import space.x9x.radp.spring.boot.bootstrap.constants.Conditions;
import space.x9x.radp.spring.security.jwt.config.JwtConfig;
import space.x9x.radp.spring.security.jwt.config.JwtSecurityConfigurer;
import space.x9x.radp.spring.security.jwt.filter.JwtAuthorizationFilter;
import space.x9x.radp.spring.security.jwt.token.JwtTokenProvider;
import space.x9x.radp.spring.security.jwt.token.JwtTokenService;
import space.x9x.radp.spring.security.jwt.token.JwtTokenStore;
import space.x9x.radp.spring.security.web.handler.ForbiddenAccessDeniedHandler;
import space.x9x.radp.spring.security.web.handler.UnauthorizedEntryPoint;

/**
 * @author RADP x9x
 * @since 2025-11-23 02:15
 */
@AutoConfiguration
@EnableConfigurationProperties(JwtProperties.class)
@ConditionalOnProperty(prefix = JwtProperties.CONFIG_PREFIX, name = Conditions.ENABLED, havingValue = Conditions.TRUE)
@RequiredArgsConstructor
@ConditionalOnClass(HttpSecurity.class)
public class JwtAutoConfiguration {

	private final JwtProperties jwtProperties;

	/**
	 * Exposes the application {@link AuthenticationManager}.
	 * @param authenticationConfiguration spring Security authentication configuration
	 * @return the {@link AuthenticationManager} bean
	 * @throws Exception if the authentication manager cannot be obtained
	 */
	@ConditionalOnMissingBean
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	/**
	 * Creates a {@link JwtTokenProvider} configured from {@link JwtProperties}.
	 * @param jwtTokenStore token store used to persist/validate JWTs (optional
	 * implementation)
	 * @return configured {@link JwtTokenProvider}
	 */
	@ConditionalOnMissingBean
	@Bean
	public JwtTokenProvider jwtTokenProvider(JwtTokenStore jwtTokenStore) {
		JwtConfig jwtConfig = JwtConfig.builder()
			.header(this.jwtProperties.getConfig().getHeader())
			.secret(this.jwtProperties.getConfig().getSecret())
			.base64Secret(this.jwtProperties.getConfig().getBase64Secret())
			.tokenValidityInSeconds(this.jwtProperties.getConfig().getTokenValidityInSeconds())
			.tokenValidityInSecondsForRememberMe(
					this.jwtProperties.getConfig().getTokenValidityInSecondsForRememberMe())
			.anonymousUrls(this.jwtProperties.getConfig().getAnonymousUrls())
			.authenticatedUrls(this.jwtProperties.getConfig().getAuthenticatedUrls())
			.permitAllUrls(this.jwtProperties.getConfig().getPermitAllUrls())
			.build();
		return new JwtTokenProvider(jwtConfig, jwtTokenStore);
	}

	/**
	 * Creates the {@link JwtTokenService} for issuing and validating tokens.
	 * @param authenticationManager authentication manager used during authentication
	 * @param jwtTokenProvider provider for creating and parsing JWTs
	 * @return {@link JwtTokenService} bean
	 */
	@ConditionalOnMissingBean
	@Bean
	public JwtTokenService jwtTokenService(AuthenticationManager authenticationManager,
			JwtTokenProvider jwtTokenProvider) {
		return new JwtTokenService(authenticationManager, jwtTokenProvider);
	}

	// ========== Web-related beans (no SecurityFilterChain) ==========

	/**
	 * Provides a delegating {@link PasswordEncoder} supporting multiple encoders.
	 * @return delegating password encoder
	 */
	@ConditionalOnMissingBean
	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	/**
	 * Handler for 401 Unauthorized responses.
	 * @return {@link UnauthorizedEntryPoint} bean
	 */
	@ConditionalOnMissingBean
	@Bean
	public UnauthorizedEntryPoint unauthorizedEntryPoint() {
		return new UnauthorizedEntryPoint();
	}

	/**
	 * Handler for 403 Forbidden responses.
	 * @return {@link ForbiddenAccessDeniedHandler} bean
	 */
	@ConditionalOnMissingBean
	@Bean
	public ForbiddenAccessDeniedHandler forbiddenAccessDeniedHandler() {
		return new ForbiddenAccessDeniedHandler();
	}

	/**
	 * Provides a default {@link PathMatcher} based on Ant-style patterns.
	 * @return path matcher bean
	 */
	@ConditionalOnMissingBean(PathMatcher.class)
	@Bean
	public PathMatcher pathMatcher() {
		return new AntPathMatcher();
	}

	/**
	 * Registers the default {@link JwtAuthorizationFilter}.
	 * @param authenticationManager authentication manager used by the filter
	 * @param jwtTokenProvider provider to validate and parse JWT tokens
	 * @param pathMatcher matcher for anonymous URL patterns
	 * @return {@link JwtAuthorizationFilter} bean
	 */
	@ConditionalOnMissingBean(JwtAuthorizationFilter.class)
	@ConditionalOnProperty(prefix = JwtProperties.CONFIG_PREFIX, name = "use-default-authentication-filter",
			havingValue = Conditions.TRUE, matchIfMissing = true)
	@Bean
	public JwtAuthorizationFilter jwtAuthorizationFilter(AuthenticationManager authenticationManager,
			JwtTokenProvider jwtTokenProvider, PathMatcher pathMatcher) {
		return new JwtAuthorizationFilter(authenticationManager, jwtTokenProvider, pathMatcher);
	}

	/**
	 * Central HttpSecurity configurer for JWT. Applications typically inject and apply
	 * this in their own SecurityConfig.
	 * @param jwtAuthorizationFilterProvider the provider for the optional
	 * {@link JwtAuthorizationFilter}
	 * @param jwtTokenProvider the provider for resolving JWT tokens
	 * @param unauthorizedEntryPoint the handler for unauthorized request entry points
	 * @param forbiddenAccessDeniedHandler the handler for forbidden access cases
	 * @param handlerMappings the request mapping handler mappings used for path
	 * configurations
	 * @return a configured {@link JwtSecurityConfigurer} instance
	 */
	@ConditionalOnMissingBean
	@Bean
	public JwtSecurityConfigurer jwtSecurityConfigurer(
			ObjectProvider<JwtAuthorizationFilter> jwtAuthorizationFilterProvider, JwtTokenProvider jwtTokenProvider,
			UnauthorizedEntryPoint unauthorizedEntryPoint, ForbiddenAccessDeniedHandler forbiddenAccessDeniedHandler,
			List<RequestMappingHandlerMapping> handlerMappings) {

		JwtConfig jwtConfig = jwtTokenProvider.getJwtConfig();
		JwtAuthorizationFilter jwtAuthorizationFilter = jwtAuthorizationFilterProvider.getIfAvailable();

		return JwtSecurityConfigurer.withDefaultPathMatcher(jwtAuthorizationFilter, jwtConfig, unauthorizedEntryPoint,
				forbiddenAccessDeniedHandler, handlerMappings, Collections.emptyList());
	}

}
