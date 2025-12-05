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

package space.x9x.radp.redis.spring.boot.support;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RBitSet;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RBucket;
import org.redisson.api.RCountDownLatch;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RList;
import org.redisson.api.RLock;
import org.redisson.api.RMap;
import org.redisson.api.RPermitExpirableSemaphore;
import org.redisson.api.RQueue;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RSemaphore;
import org.redisson.api.RSet;
import org.redisson.api.RSortedSet;
import org.redisson.api.RedissonClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

/**
 * Unit tests for {@link RedissonService}.
 *
 * @author RADP x9x
 * @since 2024-10-28 15:41
 */
class RedissonServiceTests {

	@Mock
	private RedissonClient redissonClient;

	@Mock
	private RBucket<String> stringBucket;

	@Mock
	private RAtomicLong atomicLong;

	@Mock
	private RQueue<String> queue;

	@Mock
	private RBlockingQueue<String> blockingQueue;

	@Mock
	private RDelayedQueue<String> delayedQueue;

	@Mock
	private RSet<String> set;

	@Mock
	private RList<String> list;

	@Mock
	private RMap<String, String> map;

	@Mock
	private RSortedSet<String> sortedSet;

	@Mock
	private RLock lock;

	@Mock
	private RReadWriteLock readWriteLock;

	@Mock
	private RSemaphore semaphore;

	@Mock
	private RPermitExpirableSemaphore permitExpirableSemaphore;

	@Mock
	private RCountDownLatch countDownLatch;

	@Mock
	private RBloomFilter<String> bloomFilter;

	@Mock
	private RBitSet bitSet;

	private RedissonService redissonService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		redissonService = new RedissonService(redissonClient);

		// Setup of common mocks
		given(redissonClient.<String>getBucket(anyString())).willReturn(stringBucket);
		given(redissonClient.getAtomicLong(anyString())).willReturn(atomicLong);
		given(redissonClient.<String>getQueue(anyString())).willReturn(queue);
		given(redissonClient.<String>getBlockingQueue(anyString())).willReturn(blockingQueue);
		given(redissonClient.<String>getDelayedQueue(any())).willReturn(delayedQueue);
		given(redissonClient.<String>getSet(anyString())).willReturn(set);
		given(redissonClient.<String>getList(anyString())).willReturn(list);
		given(redissonClient.<String, String>getMap(anyString())).willReturn(map);
		given(redissonClient.<String>getSortedSet(anyString())).willReturn(sortedSet);
		given(redissonClient.getLock(anyString())).willReturn(lock);
		given(redissonClient.getFairLock(anyString())).willReturn(lock);
		given(redissonClient.getReadWriteLock(anyString())).willReturn(readWriteLock);
		given(redissonClient.getSemaphore(anyString())).willReturn(semaphore);
		given(redissonClient.getPermitExpirableSemaphore(anyString())).willReturn(permitExpirableSemaphore);
		given(redissonClient.getCountDownLatch(anyString())).willReturn(countDownLatch);
		given(redissonClient.<String>getBloomFilter(anyString())).willReturn(bloomFilter);
		given(redissonClient.getBitSet(anyString())).willReturn(bitSet);
	}

	@Test
	void testSetValue() {
		// Test setValue(String key, T value)
		redissonService.setValue("testKey", "testValue");
		then(redissonClient).should().getBucket("testKey");
		then(stringBucket).should().set("testValue");
	}

	@Test
	void testSetValueWithExpiration() {
		// Test setValue(String key, T value, long expired)
		long expiration = 1000L;
		redissonService.setValue("testKey", "testValue", expiration);
		then(redissonClient).should().getBucket("testKey");
		then(stringBucket).should().set("testValue", Duration.ofMillis(expiration));
	}

	@Test
	void testGetValue() {
		// Setup
		given(stringBucket.get()).willReturn("testValue");

		// Test getValue(String key)
		String result = redissonService.getValue("testKey");

		// Verify
		then(redissonClient).should().getBucket("testKey");
		then(stringBucket).should().get();
		assertThat(result).isEqualTo("testValue");
	}

	@Test
	void testGetQueue() {
		// Test getQueue(String key)
		RQueue<String> result = redissonService.getQueue("testKey");

		// Verify
		then(redissonClient).should().getQueue("testKey");
		assertThat(result).isEqualTo(queue);
	}

	@Test
	void testGetBlockingQueue() {
		// Test getBlockingQueue(String key)
		RBlockingQueue<String> result = redissonService.getBlockingQueue("testKey");

		// Verify
		then(redissonClient).should().getBlockingQueue("testKey");
		assertThat(result).isEqualTo(blockingQueue);
	}

	@Test
	void testGetDelayedQueue() {
		// Test getDelayedQueue(RBlockingQueue<T> rBlockingQueue)
		RDelayedQueue<String> result = redissonService.getDelayedQueue(blockingQueue);

		// Verify
		then(redissonClient).should().getDelayedQueue(blockingQueue);
		assertThat(result).isEqualTo(delayedQueue);
	}

	@Test
	void testSetAndGetAtomicLong() {
		// Setup
		given(atomicLong.get()).willReturn(100L);

		// Test setAtomicLong(String key, long value)
		redissonService.setAtomicLong("testKey", 100L);

		// Verify
		then(redissonClient).should().getAtomicLong("testKey");
		then(atomicLong).should().set(100L);

		// Test getAtomicLong(String key)
		Long result = redissonService.getAtomicLong("testKey");

		// Verify
		then(redissonClient).should(times(2)).getAtomicLong("testKey");
		then(atomicLong).should().get();
		assertThat(result).isEqualTo(100L);
	}

	@Test
	void testIncrAndDecr() {
		// Setup
		given(atomicLong.incrementAndGet()).willReturn(101L);
		given(atomicLong.decrementAndGet()).willReturn(99L);
		given(atomicLong.addAndGet(10L)).willReturn(110L);
		given(atomicLong.addAndGet(-10L)).willReturn(90L);

		// Test incr(String key)
		long incrResult = redissonService.incr("testKey");

		// Verify
		then(redissonClient).should().getAtomicLong("testKey");
		then(atomicLong).should().incrementAndGet();
		assertThat(incrResult).isEqualTo(101L);

		// Test incrBy(String key, long delta)
		long incrByResult = redissonService.incrBy("testKey", 10L);

		// Verify
		then(redissonClient).should(times(2)).getAtomicLong("testKey");
		then(atomicLong).should().addAndGet(10L);
		assertThat(incrByResult).isEqualTo(110L);

		// Test decr(String key)
		long decrResult = redissonService.decr("testKey");

		// Verify
		then(redissonClient).should(times(3)).getAtomicLong("testKey");
		then(atomicLong).should().decrementAndGet();
		assertThat(decrResult).isEqualTo(99L);

		// Test decrBy(String key, long delta)
		long decrByResult = redissonService.decrBy("testKey", 10L);

		// Verify
		then(redissonClient).should(times(4)).getAtomicLong("testKey");
		then(atomicLong).should().addAndGet(-10L);
		assertThat(decrByResult).isEqualTo(90L);
	}

	@Test
	void testRemoveAndExists() {
		// Setup
		given(stringBucket.isExists()).willReturn(true, false);

		// Test isExists(String key) - should return true
		boolean existsResult = redissonService.isExists("testKey");

		// Verify
		then(redissonClient).should().getBucket("testKey");
		then(stringBucket).should().isExists();
		assertThat(existsResult).isTrue();

		// Test remove(String key)
		redissonService.remove("testKey");

		// Verify
		then(redissonClient).should(times(2)).getBucket("testKey");
		then(stringBucket).should().delete();

		// Test isExists(String key) after removal - should return false
		existsResult = redissonService.isExists("testKey");

		// Verify
		then(redissonClient).should(times(3)).getBucket("testKey");
		then(stringBucket).should(times(2)).isExists();
		assertThat(existsResult).isFalse();
	}

	@Test
	void testSetOperations() {
		// Setup
		given(set.contains("testValue")).willReturn(true);

		// Test addToSet(String key, String value)
		redissonService.addToSet("testKey", "testValue");

		// Verify
		then(redissonClient).should().getSet("testKey");
		then(set).should().add("testValue");

		// Test isSetMember(String key, String value)
		boolean isMember = redissonService.isSetMember("testKey", "testValue");

		// Verify
		then(redissonClient).should(times(2)).getSet("testKey");
		then(set).should().contains("testValue");
		assertThat(isMember).isTrue();
	}

	@Test
	void testListOperations() {
		// Setup
		given(list.get(0)).willReturn("testValue");

		// Test addToList(String key, String value)
		redissonService.addToList("testKey", "testValue");

		// Verify
		then(redissonClient).should().getList("testKey");
		then(list).should().add("testValue");

		// Test getFromList(String key, int index)
		String value = redissonService.getFromList("testKey", 0);

		// Verify
		then(redissonClient).should(times(2)).getList("testKey");
		then(list).should().get(0);
		assertThat(value).isEqualTo("testValue");
	}

	@Test
	void testMapOperations() {
		// Setup
		given(map.get("field")).willReturn("testValue");

		// Test addToMap(String key, String field, String value)
		redissonService.addToMap("testKey", "field", "testValue");

		// Verify
		then(redissonClient).should().getMap("testKey");
		then(map).should().put("field", "testValue");

		// Test getFromMap(String key, String field)
		String value = redissonService.getFromMap("testKey", "field");

		// Verify
		then(redissonClient).should(times(2)).getMap("testKey");
		then(map).should().get("field");
		assertThat(value).isEqualTo("testValue");
	}

	@Test
	void testSortedSetOperations() {
		// Test addToSortedSet(String key, String value)
		redissonService.addToSortedSet("testKey", "testValue");

		// Verify
		then(redissonClient).should().getSortedSet("testKey");
		then(sortedSet).should().add("testValue");
	}

	@Test
	void testLockOperations() {
		// Test getLock(String key)
		RLock lockResult = redissonService.getLock("testKey");

		// Verify
		then(redissonClient).should().getLock("testKey");
		assertThat(lockResult).isEqualTo(lock);

		// Test getFairLock(String key)
		RLock fairLockResult = redissonService.getFairLock("testKey");

		// Verify
		then(redissonClient).should().getFairLock("testKey");
		assertThat(fairLockResult).isEqualTo(lock);

		// Test getReadWriteLock(String key)
		RReadWriteLock rwLockResult = redissonService.getReadWriteLock("testKey");

		// Verify
		then(redissonClient).should().getReadWriteLock("testKey");
		assertThat(rwLockResult).isEqualTo(readWriteLock);
	}

	@Test
	void testSemaphoreOperations() {
		// Test getSemaphore(String key)
		RSemaphore semaphoreResult = redissonService.getSemaphore("testKey");

		// Verify
		then(redissonClient).should().getSemaphore("testKey");
		assertThat(semaphoreResult).isEqualTo(semaphore);

		// Test getPermitExpirableSemaphore(String key)
		RPermitExpirableSemaphore permitSemaphoreResult = redissonService.getPermitExpirableSemaphore("testKey");

		// Verify
		then(redissonClient).should().getPermitExpirableSemaphore("testKey");
		assertThat(permitSemaphoreResult).isEqualTo(permitExpirableSemaphore);
	}

	@Test
	void testCountDownLatchOperations() {
		// Test getCountDownLatch(String key)
		RCountDownLatch latchResult = redissonService.getCountDownLatch("testKey");

		// Verify
		then(redissonClient).should().getCountDownLatch("testKey");
		assertThat(latchResult).isEqualTo(countDownLatch);
	}

	@Test
	void testBloomFilterOperations() {
		// Test getBloomFilter(String key)
		RBloomFilter<String> filterResult = redissonService.getBloomFilter("testKey");

		// Verify
		then(redissonClient).should().getBloomFilter("testKey");
		assertThat(filterResult).isEqualTo(bloomFilter);
	}

	@Test
	void testSetNx() {
		// Setup
		given(stringBucket.setIfAbsent(eq("lock"))).willReturn(true);

		// Test setNx(String key)
		Boolean result = redissonService.setNx("testKey");

		// Verify
		then(redissonClient).should().getBucket("testKey");
		then(stringBucket).should().setIfAbsent("lock");
		assertThat(result).isTrue();
	}

	@Test
	void testSetNxWithExpiration() {
		// Setup
		given(stringBucket.setIfAbsent(eq("lock"), any(Duration.class))).willReturn(true);

		// Test setNx(String key, long expired, TimeUnit timeUnit)
		Boolean result = redissonService.setNx("testKey", 1000L, TimeUnit.MILLISECONDS);

		// Verify
		then(redissonClient).should().getBucket("testKey");
		then(stringBucket).should().setIfAbsent(eq("lock"), any(Duration.class));
		assertThat(result).isTrue();
	}

	@Test
	void testGetBitSet() {
		// Test getBitSet(String key)
		RBitSet bitSetResult = redissonService.getBitSet("testKey");

		// Verify
		then(redissonClient).should().getBitSet("testKey");
		assertThat(bitSetResult).isEqualTo(bitSet);
	}

}
