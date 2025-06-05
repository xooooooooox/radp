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

import java.util.concurrent.TimeUnit;

import org.redisson.api.RBitSet;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RCountDownLatch;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RLock;
import org.redisson.api.RMap;
import org.redisson.api.RPermitExpirableSemaphore;
import org.redisson.api.RQueue;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RSemaphore;

/**
 * Interface for Redisson service operations. This interface provides a simplified API for
 * working with Redis through Redisson, offering methods for various Redis data structures
 * and distributed objects including:
 * <ul>
 * <li>key-value operations</li>
 * <li>atomic counters</li>
 * <li>collections (lists, sets, maps)</li>
 * <li>distributed locks and synchronization primitives</li>
 * <li>bloom filters and bit operations</li>
 * </ul>
 *
 * @author IO x9x
 * @since 2024-10-28 15:21
 */
public interface IRedissonService {

	/**
	 * sets a value for the specified key.
	 * @param <T> the type of the value
	 * @param key the key
	 * @param value the value
	 */
	<T> void setValue(String key, T value);

	/**
	 * Sets a value for the specified key with an expiration time.
	 * @param <T> the type of the value
	 * @param key the key
	 * @param value the value
	 * @param expired the expiration time in seconds
	 */
	<T> void setValue(String key, T value, long expired);

	/**
	 * Gets the value for the specified key.
	 * @param <T> the return type
	 * @param key the key
	 * @return the value
	 */
	<T> T getValue(String key);

	/**
	 * Gets a Redis queue for the specified key.
	 * @param key the key
	 * @param <T> the type of elements in the queue
	 * @return the Redis queue
	 */
	<T> RQueue<T> getQueue(String key);

	/**
	 * Gets a Redis blocking queue for the specified key.
	 * @param key the key
	 * @param <T> the type of elements in the queue
	 * @return the Redis blocking queue
	 */
	<T> RBlockingQueue<T> getBlockingQueue(String key);

	/**
	 * Gets a Redis delayed queue based on a blocking queue.
	 * @param rBlockingQueue the blocking queue
	 * @param <T> the type of elements in the queue
	 * @return the Redis delayed queue
	 */
	<T> RDelayedQueue<T> getDelayedQueue(RBlockingQueue<T> rBlockingQueue);

	/**
	 * Sets an atomic long value for the specified key.
	 * @param key the key
	 * @param value the value
	 */
	void setAtomicLong(String key, long value);

	/**
	 * Gets the atomic long value for the specified key.
	 * @param key the key
	 * @return the current value of the atomic counter
	 */
	Long getAtomicLong(String key);

	/**
	 * Increments the value of the specified key by 1.
	 * @param key the key
	 * @return the value after increment
	 */
	long incr(String key);

	/**
	 * Increments the value of the specified key by the given delta.
	 * @param key the key
	 * @param delta the value to add
	 * @return the value after increment
	 */
	long incrBy(String key, long delta);

	/**
	 * Decrements the value of the specified key by 1.
	 * @param key the key
	 * @return the value after decrement
	 */
	long decr(String key);

	/**
	 * Decrements the value of the specified key by the given delta.
	 * @param key the key
	 * @param delta the value to subtract
	 * @return the value after decrement
	 */
	long decrBy(String key, long delta);

	/**
	 * Removes the value for the specified key.
	 * @param key the key
	 */
	void remove(String key);

	/**
	 * Checks if the value for the specified key exists.
	 * @param key the key
	 * @return true if the key exists, false otherwise
	 */
	boolean isExists(String key);

	/**
	 * adds the specified value to a set.
	 * @param key the key
	 * @param value the value
	 */
	void addToSet(String key, String value);

	/**
	 * checks if the specified value is a member of the set.
	 * @param key the key
	 * @param value the value
	 * @return true if the value is a member of the set, false otherwise
	 */
	boolean isSetMember(String key, String value);

	/**
	 * adds the specified value to a list.
	 * @param key the key
	 * @param value the value
	 */
	void addToList(String key, String value);

	/**
	 * gets the value at the specified index in the list.
	 * @param key the key
	 * @param index the index
	 * @return the value
	 */
	String getFromList(String key, int index);

	/**
	 * gets a Redis map for the specified key.
	 * @param <K> the type of keys in the map
	 * @param <V> the type of values in the map
	 * @param key the key
	 * @return the Redis map
	 */
	<K, V> RMap<K, V> getMap(String key);

	/**
	 * adds the specified key-value pair to a hash map.
	 * @param key the key
	 * @param field the field
	 * @param value the value
	 */
	void addToMap(String key, String field, String value);

	/**
	 * gets the value for the specified field from a hash map.
	 * @param key the key
	 * @param field the field
	 * @return the value
	 */
	String getFromMap(String key, String field);

	/**
	 * gets the value for the specified field from a hash map.
	 * @param <K> the field type
	 * @param <V> the value type
	 * @param key the key
	 * @param field the field
	 * @return the value
	 */
	<K, V> V getFromMap(String key, K field);

	/**
	 * adds the specified value to a sorted set.
	 * @param key the key
	 * @param value the value
	 */
	void addToSortedSet(String key, String value);

	/**
	 * gets a Redis reentrant lock for the specified key.
	 * @param key the key
	 * @return the lock
	 */
	RLock getLock(String key);

	/**
	 * gets a Redis fair lock for the specified key.
	 * @param key the key
	 * @return the lock
	 */
	RLock getFairLock(String key);

	/**
	 * gets a Redis read-write lock for the specified key.
	 * @param key the key
	 * @return the read-write lock
	 */
	RReadWriteLock getReadWriteLock(String key);

	/**
	 * gets Redis semaphore for the specified key.
	 * @param key the key
	 * @return the semaphore
	 */
	RSemaphore getSemaphore(String key);

	/**
	 * gets Redis permit expirable semaphore for the specified key.
	 * <p>
	 * this distributed semaphore has an interface similar to
	 * java.util.concurrent.Semaphore and also provides async, reactive, and RxJava2
	 * interfaces.
	 * @param key the key
	 * @return the permit expirable semaphore
	 */
	RPermitExpirableSemaphore getPermitExpirableSemaphore(String key);

	/**
	 * gets a Redis count down latch for the specified key.
	 * @param key the key
	 * @return the count-down latch
	 */
	RCountDownLatch getCountDownLatch(String key);

	/**
	 * gets a Redis bloom filter for the specified key.
	 * @param key the key
	 * @param <T> the type of elements in the bloom filter
	 * @return the bloom filter
	 */
	<T> RBloomFilter<T> getBloomFilter(String key);

	/**
	 * sets the value for the specified key only if the key does not exist.
	 * @param key the key
	 * @return true if the value was set, false otherwise
	 */
	Boolean setNx(String key);

	/**
	 * sets the value for the specified key only if the key does not exist, with an
	 * expiration time.
	 * @param key the key
	 * @param expired the expiration time
	 * @param timeUnit the time unit
	 * @return true if the value was set, false otherwise
	 */
	Boolean setNx(String key, long expired, TimeUnit timeUnit);

	/**
	 * gets a Redis bit set for the specified key.
	 * @param key the key
	 * @return the bit set
	 */
	RBitSet getBitSet(String key);

}
