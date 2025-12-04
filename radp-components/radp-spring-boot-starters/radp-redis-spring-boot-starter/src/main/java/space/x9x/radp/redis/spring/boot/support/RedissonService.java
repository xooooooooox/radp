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

/**
 * implementation of the {@link IRedissonService} interface that provides Redis operations
 * through Redisson. this service simplifies working with Redis data structures and
 * distributed objects by providing a consistent API for common operations.
 *
 * <p>
 * this implementation uses a {@link RedissonClient} to interact with Redis and supports
 * all operations defined in the interface including key-value operations, collections,
 * locks, and other distributed primitives.
 *
 * @author RADP x9x
 * @since 2024-10-28 15:41
 */
public class RedissonService implements IRedissonService {

	/**
	 * the Redisson client used for Redis operations.
	 */
	private final RedissonClient redissonClient;

	/**
	 * Constructs a new RedissonService with the specified RedissonClient. This
	 * constructor initializes the service with a client that will be used for all Redis
	 * operations.
	 * @param redissonClient the Redisson client to use for Redis operations
	 */
	public RedissonService(RedissonClient redissonClient) {
		this.redissonClient = redissonClient;
	}

	@Override
	public <T> void setValue(String key, T value) {
		this.redissonClient.<T>getBucket(key).set(value);
	}

	@Override
	public <T> void setValue(String key, T value, long expired) {
		RBucket<T> bucket = this.redissonClient.getBucket(key);
		bucket.set(value, Duration.ofMillis(expired));
	}

	@Override
	public <T> T getValue(String key) {
		return this.redissonClient.<T>getBucket(key).get();
	}

	@Override
	public <T> RQueue<T> getQueue(String key) {
		return this.redissonClient.getQueue(key);
	}

	@Override
	public <T> RBlockingQueue<T> getBlockingQueue(String key) {
		return this.redissonClient.getBlockingQueue(key);
	}

	@Override
	public <T> RDelayedQueue<T> getDelayedQueue(RBlockingQueue<T> rBlockingQueue) {
		return this.redissonClient.getDelayedQueue(rBlockingQueue);
	}

	@Override
	public void setAtomicLong(String key, long value) {
		this.redissonClient.getAtomicLong(key).set(value);
	}

	@Override
	public Long getAtomicLong(String key) {
		return this.redissonClient.getAtomicLong(key).get();
	}

	@Override
	public long incr(String key) {
		return this.redissonClient.getAtomicLong(key).incrementAndGet();
	}

	@Override
	public long incrBy(String key, long delta) {
		return this.redissonClient.getAtomicLong(key).addAndGet(delta);
	}

	@Override
	public long decr(String key) {
		return this.redissonClient.getAtomicLong(key).decrementAndGet();
	}

	@Override
	public long decrBy(String key, long delta) {
		return this.redissonClient.getAtomicLong(key).addAndGet(-delta);
	}

	@Override
	public void remove(String key) {
		this.redissonClient.getBucket(key).delete();
	}

	@Override
	public boolean isExists(String key) {
		return this.redissonClient.getBucket(key).isExists();
	}

	@Override
	public void addToSet(String key, String value) {
		RSet<String> set = this.redissonClient.getSet(key);
		set.add(value);
	}

	@Override
	public boolean isSetMember(String key, String value) {
		RSet<String> set = this.redissonClient.getSet(key);
		return set.contains(value);
	}

	@Override
	public void addToList(String key, String value) {
		RList<String> list = this.redissonClient.getList(key);
		list.add(value);
	}

	@Override
	public String getFromList(String key, int index) {
		RList<String> list = this.redissonClient.getList(key);
		return list.get(index);
	}

	@Override
	public <K, V> RMap<K, V> getMap(String key) {
		return this.redissonClient.getMap(key);
	}

	@Override
	public void addToMap(String key, String field, String value) {
		RMap<String, String> map = this.redissonClient.getMap(key);
		map.put(field, value);
	}

	@Override
	public String getFromMap(String key, String field) {
		RMap<String, String> map = this.redissonClient.getMap(key);
		return map.get(field);
	}

	@Override
	public <K, V> V getFromMap(String key, K field) {
		return this.redissonClient.<K, V>getMap(key).get(field);
	}

	@Override
	public void addToSortedSet(String key, String value) {
		RSortedSet<String> sortedSet = this.redissonClient.getSortedSet(key);
		sortedSet.add(value);
	}

	@Override
	public RLock getLock(String key) {
		return this.redissonClient.getLock(key);
	}

	@Override
	public RLock getFairLock(String key) {
		return this.redissonClient.getFairLock(key);
	}

	@Override
	public RReadWriteLock getReadWriteLock(String key) {
		return this.redissonClient.getReadWriteLock(key);
	}

	@Override
	public RSemaphore getSemaphore(String key) {
		return this.redissonClient.getSemaphore(key);
	}

	@Override
	public RPermitExpirableSemaphore getPermitExpirableSemaphore(String key) {
		return this.redissonClient.getPermitExpirableSemaphore(key);
	}

	@Override
	public RCountDownLatch getCountDownLatch(String key) {
		return this.redissonClient.getCountDownLatch(key);
	}

	@Override
	public <T> RBloomFilter<T> getBloomFilter(String key) {
		return this.redissonClient.getBloomFilter(key);
	}

	/**
	 * 设置键的值，只有在键不存在时才会设置成功 使用setIfAbsent替代已废弃的trySet方法.
	 * @param key 键
	 * @return 设置成功返回true，否则返回false
	 */
	@Override
	public Boolean setNx(String key) {
		return this.redissonClient.getBucket(key).setIfAbsent("lock");
	}

	/**
	 * 设置键的值，只有在键不存在时才会设置成功，并设置过期时间 使用setIfAbsent替代已废弃的trySet方法.
	 * @param key 键
	 * @param expired 过期时间
	 * @param timeUnit 时间单位
	 * @return 设置成功返回true，否则返回false
	 */
	@Override
	public Boolean setNx(String key, long expired, TimeUnit timeUnit) {
		Duration duration = Duration.ofMillis(timeUnit.toMillis(expired));
		return this.redissonClient.getBucket(key).setIfAbsent("lock", duration);
	}

	@Override
	public RBitSet getBitSet(String key) {
		return this.redissonClient.getBitSet(key);
	}

}
