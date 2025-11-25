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
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import space.x9x.radp.jwt.spring.boot.env.JwtProperties;
import space.x9x.radp.spring.boot.bootstrap.constants.Conditions;
import space.x9x.radp.spring.security.jwt.config.JwtAuthorizeHttpRequestsCustomizer;
import space.x9x.radp.spring.security.jwt.config.JwtSecurityConfigurer;

/**
 * Autoconfiguration that wires JWT into Spring Security's HttpSecurity by creating a
 * default SecurityFilterChain.
 * <p>
 * If the application defines its own SecurityFilterChain, this auto-config backs off.
 *
 * @author x9x
 * @since 2025-11-25 12:54
 * @see JwtSecurityConfigurer
 */
// 避免由于 SecurityAutoConfiguration 以及 actuator 的
// ManagementWebSecurityAutoConfiguration 自动注入了 SecurityFilterChain 以至于
// @ConditionalOnMissingBean(SecurityFilterChain.class) 条件不满足导致注入失败
@AutoConfiguration(before = SecurityAutoConfiguration.class, beforeName = {
		"org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration" })
@AutoConfigureAfter(JwtAutoConfiguration.class)
@ConditionalOnClass({ HttpSecurity.class, SecurityFilterChain.class })
@ConditionalOnBean(JwtSecurityConfigurer.class)
@ConditionalOnProperty(prefix = JwtProperties.WEB_PREFIX, name = Conditions.ENABLED, havingValue = Conditions.TRUE,
		matchIfMissing = true)
@Slf4j
public class JwtWebSecurityAutoConfiguration {

	/**
	 * Default SecurityFilterChain that simply applies the JwtSecurityConfigurer. Users
	 * can override this by defining their own SecurityFilterChain bean.
	 */
	@ConditionalOnMissingBean(SecurityFilterChain.class)
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity,
			JwtSecurityConfigurer jwtSecurityConfigurer,
			ObjectProvider<JwtAuthorizeHttpRequestsCustomizer> customizerProvider) throws Exception {

		List<JwtAuthorizeHttpRequestsCustomizer> customizers = customizerProvider.orderedStream()
			.collect(Collectors.toList());

		log.debug("Creating radp SecurityFilterChain, found {} JwtAuthorizeHttpRequestsCustomizer bean(s)",
				customizers.size());
		jwtSecurityConfigurer.configure(httpSecurity, customizers);
		return httpSecurity.build();
	}

}
