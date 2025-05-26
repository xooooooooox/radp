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

package space.x9x.radp.smoke.tests.redis;

import org.junit.jupiter.api.Test;
import space.x9x.radp.redis.spring.boot.constants.IRedisKeyProvider;
import space.x9x.radp.redis.spring.boot.constants.RedisKeyConstants;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the enhanced Redis key management system.
 * <p>
 * These tests verify that the Redis key management system works correctly
 * with the new validation, normalization, and utility methods.
 *
 * @author IO x9x
 * @since 2024-10-30
 */
class RedisKeyManagementTest {

    @Test
    void testKeyValidation() {
        // Valid keys
        assertTrue(RedisKeyConstants.isValidKey("radp:test:user:123"));
        assertTrue(RedisKeyConstants.isValidKey("app:module:entity"));

        // Invalid keys
        assertFalse(RedisKeyConstants.isValidKey(null));
        assertFalse(RedisKeyConstants.isValidKey(""));
        assertFalse(RedisKeyConstants.isValidKey("single-part"));
        assertFalse(RedisKeyConstants.isValidKey(":starts:with:delimiter"));
        assertFalse(RedisKeyConstants.isValidKey("ends:with:delimiter:"));
        assertFalse(RedisKeyConstants.isValidKey("has::consecutive:delimiters"));
    }


    @Test
    void testPrefixExtraction() {
        assertEquals("radp", RedisKeyConstants.extractPrefix("radp:test:user:123"));
        assertEquals("app", RedisKeyConstants.extractPrefix("app:module:entity"));
        assertEquals("single", RedisKeyConstants.extractPrefix("single"));
        assertEquals("", RedisKeyConstants.extractPrefix(null));
        assertEquals("", RedisKeyConstants.extractPrefix(""));
    }

    @Test
    void testKeyBuilding() {
        // Test building keys with various inputs
        assertEquals("radp:test:user:123", RedisKeyConstants.buildRedisKeyWithPrefix("radp", "test", "user", "123"));
        assertEquals("radp", RedisKeyConstants.buildRedisKeyWithPrefix("radp"));
        assertEquals("radp:test", RedisKeyConstants.buildRedisKeyWithPrefix("radp", "test"));
        assertEquals("radp:test:user", RedisKeyConstants.buildRedisKeyWithPrefix("radp", "test", "user"));

        // Test handling of null and empty parts
        assertEquals("radp:test:user", RedisKeyConstants.buildRedisKeyWithPrefix("radp", "test", null, "user"));
        assertEquals("radp:test:user", RedisKeyConstants.buildRedisKeyWithPrefix("radp", "test", "", "user"));

        // Test exception for null or empty prefix
        assertThrows(IllegalArgumentException.class, () -> RedisKeyConstants.buildRedisKeyWithPrefix(null));
        assertThrows(IllegalArgumentException.class, () -> RedisKeyConstants.buildRedisKeyWithPrefix(""));
    }

    @Test
    void testRedisKeyProvider() {
        IRedisKeyProvider provider = TestRedisKeys.INSTANCE;

        // Test building keys with the provider
        assertEquals("radp:test:user:123", provider.buildKey("user", "123"));
        assertEquals("radp:test:user:123", provider.buildEntityKey("user", "123"));
        assertEquals("radp:test:feature", provider.buildFeatureKey("feature"));

        // Test namespace validation
        assertTrue(provider.isKeyInNamespace("radp:test:user:123"));
        assertFalse(provider.isKeyInNamespace("other:test:user:123"));
        assertFalse(provider.isKeyInNamespace(null));

        // Test entity and ID extraction
        // The key format is "radp:test:user:123" where "test" is the module, "user" is the entity, and "123" is the ID
        // Since the TestRedisKeys provider has a prefix of "radp:test", the entity is at index 1 after the prefix
        assertEquals("user", provider.extractEntity("radp:test:user:123"));
        assertEquals("123", provider.extractId("radp:test:user:123"));
        assertNull(provider.extractEntity("other:test:user:123"));
        assertNull(provider.extractId("other:test:user:123"));

        // Test key validation
        assertTrue(provider.isValidNamespacedKey("radp:test:user:123"));
        assertFalse(provider.isValidNamespacedKey("radp:test::user:123"));
        assertFalse(provider.isValidNamespacedKey("other:test:user:123"));
    }
}
