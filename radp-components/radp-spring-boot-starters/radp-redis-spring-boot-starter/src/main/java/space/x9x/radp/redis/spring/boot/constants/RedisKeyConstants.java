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
import space.x9x.radp.commons.lang.Strings;

/**
 * Redis key constants and utilities for standardized key management.
 * <p>
 * This class provides a centralized location for defining Redis keys used throughout the application.
 * Keys follow the format: application:module:entity:identifier
 * <p>
 * Example: "radp:user:profile:123"
 *
 * @author x9x
 * @since 2024-10-20 18:45
 */
@UtilityClass
public class RedisKeyConstants {
    // Common prefixes
    public static final String RADP_PREFIX = "radp";

    public static final String DEMO_KEY = "radp:demo";

    /**
     * Default implementation of RedisKeyProvider for core RADP functionality.
     * This can be used directly or as a reference for module-specific implementations.
     */
    public static final RedisKeyProvider DEFAULT_PROVIDER = () -> RADP_PREFIX;


    /**
     * Core method: Builds a Redis key with multiple parts joined by colons.
     *
     * @param parts the parts of the key to join
     * @return the complete Redis key
     */
    public static String buildRedisKey(String... parts) {
        return String.join(Strings.COLON, parts);
    }

    /**
     * Builds a Redis key with a specific prefix and additional parts.
     *
     * @param prefix the prefix to use
     * @param parts  the parts to append after the prefix
     * @return the complete Redis key
     */
    public static String buildRedisKeyWithPrefix(String prefix, String... parts) {
        String[] allParts = new String[parts.length + 1];
        allParts[0] = prefix;
        System.arraycopy(parts, 0, allParts, 1, parts.length);
        return buildRedisKey(allParts);
    }
}
