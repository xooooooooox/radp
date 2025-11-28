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

package space.x9x.radp.spring.security;

import java.util.Optional;

import lombok.experimental.UtilityClass;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author x9x
 * @since 2025-11-11 23:28
 */
@UtilityClass
public class SpringSecurityUtils {

	/**
	 * Get the current authenticated principal name from the {@link SecurityContext}, if
	 * available.
	 * @return an {@link Optional} containing the principal name, or empty if no
	 * authentication is present
	 */
	public static Optional<String> getPrincipal() {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		return Optional.ofNullable(extractPrincipal(securityContext.getAuthentication()));
	}

	/**
	 * Extracts the principal's username from the given {@code Authentication} object. The
	 * method checks if the principal within the {@code Authentication} object is an
	 * instance of {@code UserDetails} or a {@code String}, and returns the appropriate
	 * value. If the input is {@code null} or the principal does not meet these
	 * conditions, it returns {@code null}.
	 * @param authentication the {@code Authentication} object from which to extract the
	 * principal. It might be {@code null}.
	 * @return the username as a {@code String} if the principal is of type
	 * {@code UserDetails}, or the {@code String} value of the principal if it is a
	 * {@code String}. Returns {@code null} if the input is {@code null} or the principal
	 * is not of the above types.
	 */
	private static String extractPrincipal(Authentication authentication) {
		if (authentication == null) {
			return null;
		}

		if (authentication.getPrincipal() instanceof UserDetails) {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			return userDetails.getUsername();
		}
		if (authentication.getPrincipal() instanceof String) {
			return ((String) authentication.getPrincipal());
		}
		return null;
	}

}
