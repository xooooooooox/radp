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

import java.util.List;

import lombok.RequiredArgsConstructor;

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
 * @author x9x
 * @since 2025-11-23 02:15
 */
@AutoConfiguration
@EnableConfigurationProperties(JwtProperties.class)
@ConditionalOnProperty(prefix = JwtProperties.CONFIG_PREFIX, name = Conditions.ENABLED, havingValue = Conditions.TRUE)
@RequiredArgsConstructor
@ConditionalOnClass(HttpSecurity.class)
public class JwtAutoConfiguration {

	private final JwtProperties jwtProperties;

	@ConditionalOnMissingBean
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

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

	@ConditionalOnMissingBean
	@Bean
	public JwtTokenService jwtTokenService(AuthenticationManager authenticationManager,
			JwtTokenProvider jwtTokenProvider) {
		return new JwtTokenService(authenticationManager, jwtTokenProvider);
	}

	// ========== Web-related beans (no SecurityFilterChain) ==========

	@ConditionalOnMissingBean
	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@ConditionalOnMissingBean
	@Bean
	public UnauthorizedEntryPoint unauthorizedEntryPoint() {
		return new UnauthorizedEntryPoint();
	}

	@ConditionalOnMissingBean
	@Bean
	public ForbiddenAccessDeniedHandler forbiddenAccessDeniedHandler() {
		return new ForbiddenAccessDeniedHandler();
	}

	@ConditionalOnMissingBean(PathMatcher.class)
	@Bean
	public PathMatcher pathMatcher() {
		return new AntPathMatcher();
	}

	@ConditionalOnMissingBean
	@Bean
	public JwtAuthorizationFilter jwtAuthorizationFilter(AuthenticationManager authenticationManager,
			JwtTokenProvider jwtTokenProvider, PathMatcher pathMatcher) {
		return new JwtAuthorizationFilter(authenticationManager, jwtTokenProvider, pathMatcher);
	}

	/**
	 * Central HttpSecurity configurer for JWT. Applications typically inject and apply
	 * this in their own SecurityConfig.
	 */
	@ConditionalOnMissingBean
	@Bean
	public JwtSecurityConfigurer jwtSecurityConfigurer(JwtAuthorizationFilter jwtAuthorizationFilter,
			JwtTokenProvider jwtTokenProvider, UnauthorizedEntryPoint unauthorizedEntryPoint,
			ForbiddenAccessDeniedHandler forbiddenAccessDeniedHandler,
			List<RequestMappingHandlerMapping> handlerMappings) {

		JwtConfig jwtConfig = jwtTokenProvider.getJwtConfig();
		return JwtSecurityConfigurer.withDefaultPathMatcher(jwtAuthorizationFilter, jwtConfig, unauthorizedEntryPoint,
				forbiddenAccessDeniedHandler, handlerMappings);
	}

}
