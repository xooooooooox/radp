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
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import org.springframework.http.HttpHeaders;

/**
 * @author x9x
 * @since 2025-11-22 22:50
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class JwtConfig {

	@Builder.Default
	private String header = HttpHeaders.AUTHORIZATION;

	private boolean enabled;

	private String base64Secret;

	/**
	 * The secret key used for JWT token signing and verification.
	 * <ul>
	 * Requirements
	 * <li>Minimum length: 32 characters</li>
	 * <li>Should contain a mix of uppercase, lowercase, numbers and * special
	 * characters</li>
	 * <li>should not contain common dictionary * words</li>
	 * <li>Must be kept secure and not exposed in logs or configurations</li>
	 * </ul>
	 * You can use {@code openssl } to generate a random key of at least 32 bytes. For
	 * example: {@code openssl rand -base64 64}
	 */
	private String secret;

	@Builder.Default
	private long tokenValidityInSeconds = 1000;

	@Builder.Default
	private long tokenValidityInSecondsForRememberMe = 2592000;

	@Builder.Default
	private List<String> anonymousUrls = Collections.emptyList();

	@Builder.Default
	private List<String> permitAllUrls = Collections.emptyList();

	@Builder.Default
	private List<String> authenticatedUrls = Collections.emptyList();

}
