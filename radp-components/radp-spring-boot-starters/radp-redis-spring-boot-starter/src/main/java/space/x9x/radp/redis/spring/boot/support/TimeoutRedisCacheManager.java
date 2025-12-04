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

import org.jetbrains.annotations.NotNull;

import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;

import space.x9x.radp.commons.lang.StringConstants;
import space.x9x.radp.commons.lang.StringUtil;
import space.x9x.radp.commons.lang.math.NumberUtil;

/**
 * an extension of {@link RedisCacheManager} that supports dynamic cache timeout
 * configuration through cache names. this manager allows specifying custom TTL
 * (time-to-live) values directly in the cache name using a special syntax.
 *
 * <p>
 * cache names can include timeout specifications using the format:
 * {@code cacheName#timeoutValue} where timeoutValue can include time unit suffixes (s for
 * seconds, m for minutes, h for hours, d for days). for example: {@code users#30m}
 * creates a cache named "users" with a 30-minute timeout.
 *
 * @author x9x
 * @since 2024-10-21 11:53
 */
public class TimeoutRedisCacheManager extends RedisCacheManager {

	/**
	 * Delimiter used to separate the cache name from the timeout specification. When a
	 * cache name contains this delimiter, the part after it is parsed as a custom timeout
	 * value.
	 */
	private static final String SPLIT = StringConstants.HASH;

	/**
	 * Constructs a new TimeoutRedisCacheManager with the specified cache writer and
	 * configuration. This cache manager extends the standard RedisCacheManager to support
	 * custom timeout specifications in cache names, allowing for dynamic TTL
	 * configuration per cache.
	 * @param cacheWriter the Redis cache writer to use for cache operations
	 * @param redisCacheConfiguration the default Redis cache configuration
	 */
	public TimeoutRedisCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration redisCacheConfiguration) {
		super(cacheWriter, redisCacheConfiguration);
	}

	@Override
	protected @NotNull RedisCache createRedisCache(@NotNull String name, RedisCacheConfiguration cacheConfig) {
		if (StringUtil.isEmpty(name)) {
			return super.createRedisCache(name, cacheConfig);
		}
		// 如果使用 # 分隔符, 大小不为 2, 则说明不使用自定义火气时间
		String[] names = StringUtil.split(name, SPLIT);
		if (names.length != 2) {
			super.createRedisCache(name, cacheConfig);
		}
		// 核心: 通过修改 cacheConfig 的过期时间, 实现自定义过期时间
		if (cacheConfig != null) {
			// 移除 # 后面的 : 以及后见面的内容, 避免影响解析
			String ttlStr = StringUtil.substringBefore(names[1], StringConstants.COLON); // 获取
																					// ttlStr
																					// 时间部分
			names[1] = StringUtil.substringAfter(names[1], ttlStr); // 移除掉 ttlStr 时间部分
			// 解析时间
			Duration duration = parseDuration(ttlStr);
			cacheConfig = cacheConfig.entryTtl(duration);
		}

		// 创建 RedisCache 对象, 需要忽略掉 ttlStr
		return super.createRedisCache(names[0] + names[1], cacheConfig);
	}

	/**
	 * parses the expiration time string into a Duration object.
	 * @param ttlStr the expiration time string
	 * @return the parsed Duration
	 */
	private Duration parseDuration(String ttlStr) {
		String timeUnit = StringUtil.right(ttlStr, 1);
		switch (timeUnit) {
			case "s":
				return Duration.ofSeconds(removeDurationSuffix(ttlStr));
			case "m":
				return Duration.ofMinutes(removeDurationSuffix(ttlStr));
			case "h":
				return Duration.ofHours(removeDurationSuffix(ttlStr));
			case "d":
				return Duration.ofDays(removeDurationSuffix(ttlStr));
			default:
				return Duration.ofSeconds(Long.parseLong(ttlStr));
		}
	}

	/**
	 * removes the time unit suffix from the expiration time string and returns the
	 * numeric value.
	 * @param ttlStr the expiration time string
	 * @return the numeric time value
	 */
	private Long removeDurationSuffix(String ttlStr) {
		return NumberUtil.toLong(StringUtil.substring(ttlStr, 0, ttlStr.length() - 1));
	}

}
