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

/**
 * Interface for Redis key providers that standardizes key management across different modules.
 * <p>
 * Implementing this interface allows different modules to define their own Redis key formats
 * while maintaining a consistent approach to key generation throughout the application.
 *
 * @author x9x
 * @since 2024-10-30
 */
public interface RedisKeyProvider {

    /**
     * Gets the prefix for all keys managed by this provider.
     * <p>
     * The prefix typically includes the application and module name,
     * for example: "radp:module-name"
     *
     * @return the key prefix
     */
    String getPrefix();

    /**
     * Builds a complete Redis key using this provider's prefix and the specified parts.
     *
     * @param parts the parts of the key to append after the prefix
     * @return the complete Redis key
     */
    default String buildKey(String... parts) {
        return RedisKeyConstants.buildRedisKeyWithPrefix(getPrefix(), parts);
    }
}