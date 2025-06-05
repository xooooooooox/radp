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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests for the enhanced Redis key management system.
 * <p>
 * These tests verify that the Redis key management system works correctly with the new
 * validation, normalization, and utility methods.
 *
 * @author IO x9x
 * @since 2024-10-30
 */
class RedisKeyManagementTest {

	@Test
	void testKeyValidation() {
		// Valid keys
		assertThat(RedisKeyConstants.isValidKey("radp:test:user:123")).isTrue();
		assertThat(RedisKeyConstants.isValidKey("app:module:entity")).isTrue();

		// Invalid keys
		assertThat(RedisKeyConstants.isValidKey(null)).isFalse();
		assertThat(RedisKeyConstants.isValidKey("")).isFalse();
		assertThat(RedisKeyConstants.isValidKey("single-part")).isFalse();
		assertThat(RedisKeyConstants.isValidKey(":starts:with:delimiter")).isFalse();
		assertThat(RedisKeyConstants.isValidKey("ends:with:delimiter:")).isFalse();
		assertThat(RedisKeyConstants.isValidKey("has::consecutive:delimiters")).isFalse();
	}

	@Test
	void testPrefixExtraction() {
		assertThat(RedisKeyConstants.extractPrefix("radp:test:user:123")).isEqualTo("radp");
		assertThat(RedisKeyConstants.extractPrefix("app:module:entity")).isEqualTo("app");
		assertThat(RedisKeyConstants.extractPrefix("single")).isEqualTo("single");
		assertThat(RedisKeyConstants.extractPrefix(null)).isEqualTo("");
		assertThat(RedisKeyConstants.extractPrefix("")).isEqualTo("");
	}

	@Test
	void testKeyBuilding() {
		// Test building keys with various inputs
		assertThat(RedisKeyConstants.buildRedisKeyWithPrefix("radp", "test", "user", "123"))
			.isEqualTo("radp:test:user:123");
		assertThat(RedisKeyConstants.buildRedisKeyWithPrefix("radp")).isEqualTo("radp");
		assertThat(RedisKeyConstants.buildRedisKeyWithPrefix("radp", "test")).isEqualTo("radp:test");
		assertThat(RedisKeyConstants.buildRedisKeyWithPrefix("radp", "test", "user")).isEqualTo("radp:test:user");

		// Test handling of null and empty parts
		assertThat(RedisKeyConstants.buildRedisKeyWithPrefix("radp", "test", null, "user")).isEqualTo("radp:test:user");
		assertThat(RedisKeyConstants.buildRedisKeyWithPrefix("radp", "test", "", "user")).isEqualTo("radp:test:user");

		// Test exception for null or empty prefix
		assertThatThrownBy(() -> RedisKeyConstants.buildRedisKeyWithPrefix(null))
			.isInstanceOf(IllegalArgumentException.class);
		assertThatThrownBy(() -> RedisKeyConstants.buildRedisKeyWithPrefix(""))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void testRedisKeyProvider() {
		IRedisKeyProvider provider = TestRedisKeys.INSTANCE;

		// Test building keys with the provider
		assertThat(provider.buildKey("user", "123")).isEqualTo("radp:test:user:123");
		assertThat(provider.buildEntityKey("user", "123")).isEqualTo("radp:test:user:123");
		assertThat(provider.buildFeatureKey("feature")).isEqualTo("radp:test:feature");

		// Test namespace validation
		assertThat(provider.isKeyInNamespace("radp:test:user:123")).isTrue();
		assertThat(provider.isKeyInNamespace("other:test:user:123")).isFalse();
		assertThat(provider.isKeyInNamespace(null)).isFalse();

		// Test entity and ID extraction
		// The key format is "radp:test:user:123" where "test" is the module, "user" is
		// the entity, and "123" is the ID
		// Since the TestRedisKeys provider has a prefix of "radp:test", the entity is at
		// index 1 after the prefix
		assertThat(provider.extractEntity("radp:test:user:123")).isEqualTo("user");
		assertThat(provider.extractId("radp:test:user:123")).isEqualTo("123");
		assertThat(provider.extractEntity("other:test:user:123")).isNull();
		assertThat(provider.extractId("other:test:user:123")).isNull();

		// Test key validation
		assertThat(provider.isValidNamespacedKey("radp:test:user:123")).isTrue();
		assertThat(provider.isValidNamespacedKey("radp:test::user:123")).isFalse();
		assertThat(provider.isValidNamespacedKey("other:test:user:123")).isFalse();
	}

}
