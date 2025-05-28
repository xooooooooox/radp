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

package space.x9x.radp.redis.spring.boot.autoconfigure;

import java.util.Objects;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.BatchStrategies;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;

import space.x9x.radp.redis.spring.boot.env.ExtendedCacheProperties;
import space.x9x.radp.redis.spring.boot.support.IRedissonService;
import space.x9x.radp.redis.spring.boot.support.RedissonService;
import space.x9x.radp.redis.spring.boot.support.TimeoutRedisCacheManager;

/**
 * Autoconfiguration for Redis caching. This class automatically configures Spring's cache
 * abstraction to use Redis as the backing store, with enhanced features including:
 * <ul>
 * <li>Custom serialization for cache values</li>
 * <li>Enhanced timeout handling with TimeoutRedisCacheManager</li>
 * <li>Non-locking cache operations for better performance</li>
 * <li>Integration with Redisson for advanced Redis operations</li>
 * </ul>
 * This configuration enables the @Cacheable, @CachePut, and @CacheEvict annotations to
 * work with Redis, providing a distributed caching solution for the application.
 *
 * @author IO x9x
 * @since 2024-10-21 11:37
 */
@EnableConfigurationProperties({ CacheProperties.class, ExtendedCacheProperties.class })
@EnableCaching
@AutoConfiguration
@Slf4j
public class RadpRedisCacheAutoConfiguration {

	/**
	 * Log message used when the Redis cache configuration is autowired. This message is
	 * logged at debug level when the configuration is initialized.
	 */
	private static final String AUTOWIRED_REDIS_CACHE_CONFIGURATION = "Autowired redisCacheConfiguration";

	/**
	 * Log message used when the Redis cache manager is autowired. This message is logged
	 * at debug level when the manager is initialized.
	 */
	private static final String AUTOWIRED_REDIS_CACHE_MANAGER = "Autowired redisCacheManager";

	/**
	 * Log message used when the Redisson service is autowired. This message is logged at
	 * debug level when the service is initialized.
	 */
	private static final String AUTOWIRED_REDISSON_SERVICE = "Autowired redissonService";

	/**
	 * Creates a Redis cache configuration with customized serialization and settings.
	 * This bean configures how Spring's cache abstraction interacts with Redis,
	 * including: - Custom value serialization using the application's standard Redis
	 * serializer - Time-to-live settings from cache properties - Null value caching
	 * behavior - Key prefix usage
	 * @param cacheProperties the Spring cache properties to apply
	 * @return a configured RedisCacheConfiguration instance
	 */
	@Bean
	@Primary
	public RedisCacheConfiguration redisCacheConfiguration(CacheProperties cacheProperties) {
		log.debug(AUTOWIRED_REDIS_CACHE_CONFIGURATION);
		RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();
		config = config.serializeValuesWith(RedisSerializationContext.SerializationPair
			.fromSerializer(RadpRedisTemplateAutoConfiguration.buildRedisSerializer()));

		CacheProperties.Redis redisProperties = cacheProperties.getRedis();
		if (redisProperties.getTimeToLive() != null) {
			config = config.entryTtl(redisProperties.getTimeToLive());
		}
		if (!redisProperties.isCacheNullValues()) {
			config = config.disableCachingNullValues();
		}
		if (!redisProperties.isUseKeyPrefix()) {
			config = config.disableKeyPrefix();
		}

		return config;
	}

	/**
	 * Creates a Redis cache manager with enhanced timeout capabilities. This bean
	 * provides a cache manager that uses the application's Redis connection and cache
	 * configuration, with additional features: - Non-locking cache operations for better
	 * performance - Batch scanning strategy with configurable batch size - Custom
	 * TimeoutRedisCacheManager implementation for enhanced timeout handling
	 * @param redisTemplate the Redis template to use for cache operations
	 * @param redisCacheConfiguration the Redis cache configuration to apply
	 * @param extendedCacheProperties additional cache properties for extended features
	 * @return a configured RedisCacheManager instance
	 */
	@Bean
	public RedisCacheManager redisCacheManager(RedisTemplate<String, Object> redisTemplate,
			RedisCacheConfiguration redisCacheConfiguration, ExtendedCacheProperties extendedCacheProperties) {
		log.debug(AUTOWIRED_REDIS_CACHE_MANAGER);
		RedisConnectionFactory connectionFactory = Objects.requireNonNull(redisTemplate.getConnectionFactory());
		RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(connectionFactory,
				BatchStrategies.scan(extendedCacheProperties.getRedisScanBatchSize()));
		return new TimeoutRedisCacheManager(redisCacheWriter, redisCacheConfiguration);
	}

	/**
	 * Creates a Redisson service that provides enhanced Redis operations. This bean wraps
	 * a RedissonClient to provide a simplified and consistent API for working with
	 * various Redis data structures and features, including: - Basic key-value operations
	 * - Collections (lists, sets, maps) - Distributed locks and synchronization
	 * primitives - Atomic operations
	 * @param redissonClient the Redisson client to use for Redis operations
	 * @return an implementation of IRedissonService
	 */
	@Bean(name = "redissonService")
	public IRedissonService redissonService(RedissonClient redissonClient) {
		log.debug(AUTOWIRED_REDISSON_SERVICE);
		return new RedissonService(redissonClient);
	}

}
