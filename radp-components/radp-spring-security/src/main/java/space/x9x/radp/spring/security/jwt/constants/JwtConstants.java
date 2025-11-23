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

package space.x9x.radp.spring.security.jwt.constants;

import lombok.experimental.UtilityClass;

/**
 * @author x9x
 * @since 2025-11-22 23:52
 */
@UtilityClass
public class JwtConstants {

	/**
	 * Bearer token type.
	 * @see <a href="https://datatracker.ietf.org/doc/html/rfc6750#autoid-1">RFC 6750</a>
	 */
	public static final String BEARER_TYPE = "Bearer";

	/**
	 * {@code Authorization: <scheme> <credentials>}.
	 */
	public static final String BEARER_PREFIX = String.format("%s ", BEARER_TYPE);

	/**
	 * Represents the key used to store and retrieve authorities or roles associated with
	 * a user in a security or token-based authentication context.
	 */
	public static final String AUTHORITIES_KEY = "authorities";

}
