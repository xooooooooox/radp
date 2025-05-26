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
 * @author IO x9x
 * @since 2024-10-30
 */
public interface IRedisKeyProvider {

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

    /**
     * Builds a complete Redis key for a specific feature or operation.
     * <p>
     * This is a convenience method for creating keys in the format:
     * prefix:feature
     *
     * @param feature the feature or operation name
     * @return the complete Redis key
     */
    default String buildFeatureKey(String feature) {
        return buildKey(feature);
    }

    /**
     * Builds a complete Redis key for an entity with an identifier.
     * <p>
     * This is a convenience method for creating keys in the format:
     * prefix:entity:id
     *
     * @param entity the entity name
     * @param id     the entity identifier
     * @return the complete Redis key
     */
    default String buildEntityKey(String entity, String id) {
        return buildKey(entity, id);
    }

    /**
     * Checks if a Redis key belongs to this provider's namespace.
     * <p>
     * This method can be used to validate that a key follows the expected format
     * and belongs to the correct namespace.
     *
     * @param key the Redis key to check
     * @return true if the key belongs to this provider's namespace, false otherwise
     */
    default boolean isKeyInNamespace(String key) {
        return key != null && key.startsWith(getPrefix() + RedisKeyConstants.DELIMITER);
    }

    /**
     * Extracts the entity part from a key in this provider's namespace.
     * <p>
     * This method extracts the part after the prefix. For example, if the prefix is "app:module"
     * and the key is "app:module:entity:id", this method returns "entity".
     * <p>
     * If the key doesn't have enough parts or doesn't belong to this namespace,
     * returns null.
     *
     * @param key the Redis key
     * @return the entity part, or null if not found
     */
    default String extractEntity(String key) {
        if (!isKeyInNamespace(key)) {
            return null;
        }

        // Count the number of delimiters in the prefix
        String prefix = getPrefix();
        int prefixDelimiters = prefix.split(RedisKeyConstants.DELIMITER, -1).length - 1;

        // The entity is the part after the prefix
        String[] parts = key.split(RedisKeyConstants.DELIMITER);
        int entityIndex = prefixDelimiters + 1;

        return parts.length > entityIndex ? parts[entityIndex] : null;
    }

    /**
     * Extracts the identifier part from a key in this provider's namespace.
     * <p>
     * This method extracts the part after the entity. For example, if the prefix is "app:module"
     * and the key is "app:module:entity:id", this method returns "id".
     * <p>
     * If the key doesn't have enough parts or doesn't belong to this namespace,
     * returns null.
     *
     * @param key the Redis key
     * @return the identifier part, or null if not found
     */
    default String extractId(String key) {
        if (!isKeyInNamespace(key)) {
            return null;
        }

        // Count the number of delimiters in the prefix
        String prefix = getPrefix();
        int prefixDelimiters = prefix.split(RedisKeyConstants.DELIMITER, -1).length - 1;

        // The ID is the part after the entity
        String[] parts = key.split(RedisKeyConstants.DELIMITER);
        int idIndex = prefixDelimiters + 2;

        return parts.length > idIndex ? parts[idIndex] : null;
    }

    /**
     * Validates that a Redis key belongs to this provider's namespace and follows
     * the standard format.
     *
     * @param key the Redis key to validate
     * @return true if the key is valid and belongs to this namespace, false otherwise
     */
    default boolean isValidNamespacedKey(String key) {
        return RedisKeyConstants.isValidKey(key) && isKeyInNamespace(key);
    }
}
