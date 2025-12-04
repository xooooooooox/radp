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

package space.x9x.radp.spring.data.redis.core;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.redis.core.StringRedisTemplate;

import space.x9x.radp.commons.collections.CollectionUtils;
import space.x9x.radp.commons.lang.ObjUtils;
import space.x9x.radp.commons.lang.StrUtils;
import space.x9x.radp.spring.framework.json.support.JSONHelper;

/**
 * Implementation of the CustomRedisTemplate interface. This class provides concrete
 * implementations of Redis operations using Spring's StringRedisTemplate as the
 * underlying mechanism for Redis communication. It handles serialization and
 * deserialization of objects to/from JSON for storage in Redis.
 *
 * @author x9x
 * @since 2024-10-19 16:33
 */
@RequiredArgsConstructor
@Slf4j
@Getter
public class CustomRedisTemplateImpl implements CustomRedisTemplate {

	/**
	 * The Spring StringRedisTemplate used for Redis operations.
	 */
	private final StringRedisTemplate redisTemplate;

	// ---------------------------------- RedisObject 操作
	// ----------------------------------

	@Override
	public Boolean hasKey(String key) {
		return this.redisTemplate.hasKey(key);
	}

	@Override
	public Boolean delete(String key) {
		return this.redisTemplate.delete(key);
	}

	@Override
	public Boolean expire(String key, long timeout) {
		return this.redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
	}

	// ---------------------------------- String 操作 ----------------------------------

	@Override
	public <T> Optional<T> get(String key, Class<T> clazz) {
		String value = this.redisTemplate.opsForValue().get(key);
		return toObject(value, clazz);
	}

	@Override
	public Optional<String> get(String key) {
		return Optional.ofNullable(this.redisTemplate.opsForValue().get(key));
	}

	@Override
	public <T> Optional<List<T>> getForList(String key, Class<T> clazz) {
		String value = this.redisTemplate.opsForValue().get(key);
		if (StrUtils.isBlank(value)) {
			return Optional.empty();
		}
		return Optional.of(JSONHelper.json().parseList(value, clazz));
	}

	@Override
	public <T> void set(String key, T value) {
		this.redisTemplate.opsForValue().set(key, toString(value));
	}

	@Override
	public <T> void set(String key, T data, long timeout) {
		this.set(key, data, timeout, TimeUnit.SECONDS);
	}

	@Override
	public <T> void set(String key, T data, long timeout, TimeUnit unit) {
		String value = toString(data);
		this.redisTemplate.opsForValue().set(key, value, timeout, unit);
	}

	// ---------------------------------- Hash 操作 ----------------------------------

	@Override
	public <T> Optional<T> hget(String key, String hashKey, Class<T> clazz) {
		Object hashValue = this.redisTemplate.opsForHash().get(key, hashKey);
		return toObject(ObjUtils.trimToString(hashValue), clazz);
	}

	@Override
	public Optional<Map<Object, Object>> hgetAll(String key) {
		return Optional.of(this.redisTemplate.opsForHash().entries(key));
	}

	@Override
	public <T> void hset(String key, String hashKey, T data) {
		this.redisTemplate.opsForHash().put(key, hashKey, toString(data));
	}

	@Override
	public void hset(String key, String hashKey, String value) {
		this.redisTemplate.opsForHash().put(key, hashKey, value);
	}

	@Override
	public void hset(String key, Map<?, ?> map) {
		this.redisTemplate.opsForHash().putAll(key, map);
	}

	@Override
	public void hDelete(String key, Object... hashKeys) {
		this.redisTemplate.opsForHash().delete(key, hashKeys);
	}

	// ---------------------------------- List 操作 ----------------------------------

	@Override
	public <T> Optional<List<T>> range(String key, long start, long end, Class<T> clazz) {
		List<String> list = this.redisTemplate.opsForList().range(key, start, end);
		if (CollectionUtils.isEmpty(list)) {
			return Optional.empty();
		}
		return Optional
			.of(list.stream().map(str -> JSONHelper.json().parseObject(str, clazz)).collect(Collectors.toList()));
	}

	@Override
	public <T> void rightPushAll(String key, List<T> data) {
		String[] values = new String[data.size()];
		for (int i = 0; i < values.length; i++) {
			values[i] = toString(data.get(i));
		}
		this.redisTemplate.opsForList().rightPushAll(key, values);
	}

	// ---------------------------------- Set 操作 ----------------------------------

	@Override
	public Optional<Set<String>> members(String key) {
		return Optional.ofNullable(this.redisTemplate.opsForSet().members(key));
	}

	@Override
	public <T> void add(String key, List<T> data) {
		String[] values = new String[data.size()];
		for (int i = 0; i < values.length; i++) {
			values[i] = toString(data.get(i));
		}
		this.redisTemplate.opsForSet().add(key, values);
	}

	// ------------------------------------------------------------------------------

	/**
	 * Converts a JSON string to an object of the specified class.
	 * @param <T> the type of the object to be returned
	 * @param value the JSON string to be converted
	 * @param clazz the class of the object to be returned
	 * @return an Optional containing the converted object if the JSON string is not
	 * blank, otherwise an empty Optional
	 */
	private <T> Optional<T> toObject(String value, Class<T> clazz) {
		return Optional.ofNullable(value)
			.filter(StrUtils::isNoneBlank)
			.map(s -> JSONHelper.json().parseObject(s, clazz));
	}

	/**
	 * Converts the given data object to its String representation. If the data is a
	 * character or a CharSequence, it returns the data's toString() value. For other
	 * types of objects, it converts the data to a JSON string.
	 * @param <T> the type of the data object to be converted
	 * @param data the data object to be converted to a String; this can be of any type
	 * @return the String representation of the data object; returns null if the input
	 * data is null
	 */
	private <T> String toString(T data) {
		if (data == null) {
			return null;
		}
		String value;
		if (data instanceof CharSequence || data instanceof Character) {
			value = data.toString();
		}
		else {
			value = JSONHelper.json().toJSONString(data);
		}
		return value;
	}

}
