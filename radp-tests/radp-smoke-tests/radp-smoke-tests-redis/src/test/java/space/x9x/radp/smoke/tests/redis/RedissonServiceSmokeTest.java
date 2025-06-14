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

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import com.redis.testcontainers.RedisContainer;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import space.x9x.radp.redis.spring.boot.constants.RedisKeyConstants;
import space.x9x.radp.redis.spring.boot.support.RedissonService;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Smoke tests for {@link RedissonService} using Testcontainers. These tests verify that
 * RedissonService works correctly with a real Redis server.
 */
@Testcontainers
@Slf4j
class RedissonServiceSmokeTest {

	private static final String TEST_VALUE = "test-value";

	private static final RedisContainer redisContainer = new RedisContainer(DockerImageName.parse("redis:6.2.6"));

	private RedissonClient redissonClient;

	private RedissonService redissonService;

	/**
	 * Generate a unique key for each test to avoid conflicts. Uses the RedisKeyProvider
	 * to create standardized keys with the test name and a UUID.
	 * @param testName the name of the test or operation
	 * @return a unique Redis key for the test
	 */
	private String getUniqueKey(String testName) {
		// Use UUID instead of timestamp for more deterministic and collision-resistant
		// keys
		String uniqueId = UUID.randomUUID().toString().substring(0, 8); // Use the first 8
																		// chars of UUID
																		// for brevity
		return RedisKeyConstants.buildRedisKeyWithDefPrefix("test", testName, uniqueId);
	}

	@BeforeEach
	void setUp() {
		// Configure Redisson client to connect to the Redis container
		Config config = new Config();
		config.useSingleServer()
			.setAddress(redisContainer.getRedisURI())
			.setConnectionMinimumIdleSize(1)
			.setConnectionPoolSize(2)
			.setConnectTimeout(10000)
			.setTimeout(3000);

		redissonClient = Redisson.create(config);
		redissonService = new RedissonService(redissonClient);

		// Clear the Redis database before each test
		redissonClient.getKeys().flushall();
	}

	@AfterEach
	void tearDown() {
		// Cleanup resources
		if (redissonClient != null) {
			redissonClient.shutdown();
		}
	}

	@Test
	void testSetAndGetValue() {
		// Generate a unique key for this test
		String testKey = getUniqueKey("setValue");

		// Set a value
		redissonService.setValue(testKey, TEST_VALUE);

		// Get the value and verify it matches what was set
		String retrievedValue = redissonService.getValue(testKey);
		assertThat(retrievedValue).isEqualTo(TEST_VALUE);
	}

	@Test
	void testSetValueWithExpiration() {
		// Generate a unique key for this test
		String testKey = getUniqueKey("setValueWithExpiration");

		// Set a value with a short expiration time
		redissonService.setValue(testKey, TEST_VALUE, 100);

		// Verify the value exists immediately after setting
		assertThat(redissonService.isExists(testKey)).isTrue();

		// Wait for the value to expire
		try {
			Thread.sleep(200);
		}
		catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}

		// Verify the value no longer exists after expiration
		assertThat(redissonService.isExists(testKey)).isFalse();
	}

	@Test
	void testRemoveValue() {
		// Generate a unique key for this test
		String testKey = getUniqueKey("removeValue");

		// Set a value
		redissonService.setValue(testKey, TEST_VALUE);

		// Verify the value exists
		assertThat(redissonService.isExists(testKey)).isTrue();

		// Remove the value
		redissonService.remove(testKey);

		// Verify the value no longer exists
		assertThat(redissonService.isExists(testKey)).isFalse();
	}

	@Test
	void testAtomicOperations() {
		// Generate a unique key for this test
		String testKey = getUniqueKey("atomicOperations");

		// Set an atomic long value
		redissonService.setAtomicLong(testKey, 100L);

		// Get the value and verify it matches what was set
		Long value = redissonService.getAtomicLong(testKey);
		assertThat(value).isEqualTo(100L);

		// Increment the value
		long incrResult = redissonService.incr(testKey);
		assertThat(incrResult).isEqualTo(101L);

		// Increment by a specific amount
		long incrByResult = redissonService.incrBy(testKey, 10L);
		assertThat(incrByResult).isEqualTo(111L);

		// Decrement the value
		long decrResult = redissonService.decr(testKey);
		assertThat(decrResult).isEqualTo(110L);

		// Decrement by a specific amount
		long decrByResult = redissonService.decrBy(testKey, 10L);
		assertThat(decrByResult).isEqualTo(100L);
	}

	@Test
	void testSetOperations() {
		// Generate a unique key for this test
		String testKey = getUniqueKey("setOperations");

		// Add a value to a set
		redissonService.addToSet(testKey, TEST_VALUE);

		// Verify the value is a member of the set
		boolean isMember = redissonService.isSetMember(testKey, TEST_VALUE);
		assertThat(isMember).isTrue();

		// Verify a different value is not a member of the set
		boolean isNotMember = redissonService.isSetMember(testKey, "non-existent-value");
		assertThat(isNotMember).isFalse();
	}

	@Test
	void testListOperations() {
		// Generate a unique key for this test
		String testKey = getUniqueKey("listOperations");

		// Add a value to a list
		redissonService.addToList(testKey, TEST_VALUE);

		// Get the value from the list and verify it matches what was added
		String retrievedValue = redissonService.getFromList(testKey, 0);
		assertThat(retrievedValue).isEqualTo(TEST_VALUE);
	}

	@Test
	void testMapOperations() {
		// Generate a unique key for this test
		String testKey = getUniqueKey("mapOperations");

		// Add a value to a map
		String field = "field";
		redissonService.addToMap(testKey, field, TEST_VALUE);

		// Get the value from the map and verify it matches what was added
		String retrievedValue = redissonService.getFromMap(testKey, field);
		assertThat(retrievedValue).isEqualTo(TEST_VALUE);
	}

	@Test
	void testSetNx() {
		// Generate a unique key for this test
		String testKey = getUniqueKey("setNx");

		// Set a value using setNx (only if not exists)
		Boolean result = redissonService.setNx(testKey);
		assertThat(result).isTrue();

		// Try to set the value again
		Boolean secondResult = redissonService.setNx(testKey);
		assertThat(secondResult).isFalse();
	}

	@Test
	void testSetNxWithExpiration() {
		// Generate a unique key for this test
		String testKey = getUniqueKey("setNxWithExpiration");

		// Set a value using setNx with expiration
		Boolean result = redissonService.setNx(testKey, 100, TimeUnit.MILLISECONDS);
		assertThat(result).isTrue();

		// Wait for the value to expire
		try {
			Thread.sleep(200);
		}
		catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}

		// Try to set the value again after expiration
		Boolean secondResult = redissonService.setNx(testKey);
		assertThat(secondResult).isTrue();
	}

}
