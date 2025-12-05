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

package space.x9x.radp.redis.spring.boot.constants;

import lombok.experimental.UtilityClass;

import space.x9x.radp.commons.lang.StringConstants;

/**
 * Redis key constants and utilities for standardized key management.
 * <p>
 * This class provides a centralized location for defining Redis keys used throughout the
 * application. Keys follow the format: application:module:entity:identifier
 * <p>
 * Example: "radp:user:profile:123"
 *
 * @author RADP x9x
 * @since 2024-10-20 18:45
 */
@UtilityClass
public class RedisKeyConstants {

	/**
	 * The default prefix for all RADP Redis keys.
	 */
	private static final String RADP_PREFIX = "radp";

	/**
	 * The standard delimiter used to separate parts of Redis keys.
	 */
	public static final String DELIMITER = StringConstants.COLON;

	/**
	 * Default implementation of RedisKeyProvider for core RADP functionality. This can be
	 * used directly or as a reference for module-specific implementations.
	 */
	public static final IRedisKeyProvider DEFAULT_PROVIDER = () -> RADP_PREFIX;

	/**
	 * Core method: Builds a Redis key with multiple parts joined by the standard
	 * delimiter. This method filters out null or empty parts to ensure clean keys.
	 * @param parts the parts of the key to join
	 * @return the complete Redis key
	 */
	private static String buildRedisKey(String... parts) {
		if (parts == null || parts.length == 0) {
			return "";
		}

		// Filter out null or empty parts
		StringBuilder sb = new StringBuilder();
		boolean first = true;

		for (String part : parts) {
			if (part != null && !part.isEmpty()) {
				if (!first) {
					sb.append(DELIMITER);
				}
				sb.append(part);
				first = false;
			}
		}

		return sb.toString();
	}

	/**
	 * Builds a Redis key with a specific prefix and additional parts. This method ensures
	 * that the prefix is always included, even if it's the only part.
	 * @param prefix the prefix to use
	 * @param parts the parts to append after the prefix
	 * @return the complete Redis key
	 */
	public static String buildRedisKeyWithPrefix(String prefix, String... parts) {
		if (prefix == null || prefix.isEmpty()) {
			throw new IllegalArgumentException("Prefix cannot be null or empty");
		}

		if (parts == null || parts.length == 0) {
			return prefix;
		}

		String[] allParts = new String[parts.length + 1];
		allParts[0] = prefix;
		System.arraycopy(parts, 0, allParts, 1, parts.length);
		return buildRedisKey(allParts);
	}

	/**
	 * Creates a namespaced Redis key using the DEFAULT_PROVIDER's prefix. This is a
	 * convenience method for creating keys in the default RADP namespace.
	 * @param parts the parts of the key to append after the default prefix
	 * @return the complete Redis key
	 */
	public static String buildRedisKeyWithDefPrefix(String... parts) {
		return DEFAULT_PROVIDER.buildKey(parts);
	}

	/**
	 * Validates that a Redis key follows the standard format. A valid key should: - Not
	 * be null or empty - Contain at least one delimiter - Not start or end with a
	 * delimiter - Not contain consecutive delimiters
	 * @param key the Redis key to validate
	 * @return true if the key is valid, false otherwise
	 */
	public static boolean isValidKey(String key) {
		if (key == null || key.isEmpty()) {
			return false;
		}

		if (!key.contains(DELIMITER)) {
			return false;
		}

		if (key.startsWith(DELIMITER) || key.endsWith(DELIMITER)) {
			return false;
		}

		return !key.contains(DELIMITER + DELIMITER);
	}

	/**
	 * Extracts the prefix (first part) from a Redis key.
	 * @param key the Redis key
	 * @return the prefix, or the entire key if no delimiter is found
	 */
	public static String extractPrefix(String key) {
		if (key == null || key.isEmpty()) {
			return "";
		}

		int delimiterIndex = key.indexOf(DELIMITER);
		return (delimiterIndex > 0) ? key.substring(0, delimiterIndex) : key;
	}

}
