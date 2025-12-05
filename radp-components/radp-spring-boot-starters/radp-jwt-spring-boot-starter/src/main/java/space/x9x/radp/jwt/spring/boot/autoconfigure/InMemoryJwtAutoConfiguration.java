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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import space.x9x.radp.jwt.spring.boot.env.JwtProperties;
import space.x9x.radp.spring.boot.bootstrap.constants.Conditions;
import space.x9x.radp.spring.security.common.token.AccessToken;
import space.x9x.radp.spring.security.jwt.token.JwtTokenStore;

/**
 * Autoconfiguration class for in-memory JWT token storage. This configuration is
 * activated when the JWT properties are enabled (radp.jwt.enabled=true).
 * <p>
 * Provides a basic in-memory implementation of {@link JwtTokenStore} that performs no
 * actual storage operations, suitable for testing or scenarios where token persistence is
 * not required.
 *
 * @author RADP x9x
 * @since 2025-11-24 22:44
 */
@AutoConfiguration
@EnableConfigurationProperties(JwtProperties.class)
@ConditionalOnProperty(prefix = JwtProperties.CONFIG_PREFIX, name = Conditions.ENABLED, havingValue = Conditions.TRUE)
@RequiredArgsConstructor
@Configuration
@Slf4j
public class InMemoryJwtAutoConfiguration {

	/**
	 * Creates a default in-memory implementation of {@link JwtTokenStore}. This
	 * implementation does not store tokens but always validates them as true, making it
	 * suitable for testing or development environments.
	 * <p>
	 * This bean is only created if no other {@link JwtTokenStore} bean is present in the
	 * application context.
	 * @return a new instance of {@link JwtTokenStore} that provides no-op token storage
	 * operations
	 */
	@ConditionalOnMissingBean
	@Bean
	public JwtTokenStore jwtTokenStore() {
		log.debug("Autowired InMemoryJwtTokenStore");
		return new JwtTokenStore() {
			@Override
			public void storeAccessToken(AccessToken accessToken) {
				// do nothing
			}

			@Override
			public boolean validateAccessToken(AccessToken accessToken) {
				return true;
			}

			@Override
			public void removeAccessToken(AccessToken accessToken) {
				// do nothing
			}
		};
	}

}
