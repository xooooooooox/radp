package space.x9x.radp.redis.spring.boot.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
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
import space.x9x.radp.redis.spring.boot.env.ExtendedCacheProperties;
import space.x9x.radp.redis.spring.boot.support.IRedissonService;
import space.x9x.radp.redis.spring.boot.support.RedissonService;
import space.x9x.radp.redis.spring.boot.support.TimeoutRedisCacheManager;

import java.util.Objects;

/**
 * @author x9x
 * @since 2024-10-21 11:37
 */
@EnableConfigurationProperties({CacheProperties.class, ExtendedCacheProperties.class})
@EnableCaching
@AutoConfiguration
@Slf4j
public class RadpRedisCacheAutoConfiguration {

    private static final String AUTOWIRED_REDIS_CACHE_CONFIGURATION = "Autowired redisCacheConfiguration";
    private static final String AUTOWIRED_REDIS_CACHE_MANAGER = "Autowired redisCacheManager";
    private static final String AUTOWIRED_REDISSON_SERVICE = "Autowired redissonService";

    /**
     * Creates and configures a RedisCacheConfiguration bean.
     * This method sets up the default cache configuration with appropriate serialization
     * and applies settings from the provided CacheProperties.
     *
     * @param cacheProperties the cache properties to apply to the configuration
     * @return a configured RedisCacheConfiguration instance
     */
    @Bean
    @Primary
    public RedisCacheConfiguration redisCacheConfiguration(CacheProperties cacheProperties) {
        log.debug(AUTOWIRED_REDIS_CACHE_CONFIGURATION);
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();
        config = config.serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(RadpRedisTemplateAutoConfiguration.buildRedisSerializer()));

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
     * Creates and configures a RedisCacheManager bean.
     * This method sets up a cache manager with non-locking Redis cache writer
     * and batch scanning strategy based on the provided properties.
     *
     * @param redisTemplate           the Redis template to use for cache operations
     * @param redisCacheConfiguration the cache configuration to apply
     * @param extendedCacheProperties additional cache properties for extended functionality
     * @return a configured RedisCacheManager instance
     */
    @Bean
    public RedisCacheManager redisCacheManager(RedisTemplate<String, Object> redisTemplate,
                                               RedisCacheConfiguration redisCacheConfiguration,
                                               ExtendedCacheProperties extendedCacheProperties) {
        log.debug(AUTOWIRED_REDIS_CACHE_MANAGER);
        RedisConnectionFactory connectionFactory = Objects.requireNonNull(redisTemplate.getConnectionFactory());
        RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(connectionFactory,
                BatchStrategies.scan(extendedCacheProperties.getRedisScanBatchSize()));
        return new TimeoutRedisCacheManager(redisCacheWriter, redisCacheConfiguration);

    }

    /**
     * Creates and configures an IRedissonService bean.
     * This method provides a service implementation for interacting with Redis
     * using the Redisson client.
     *
     * @param redissonClient the Redisson client to use for Redis operations
     * @return a configured IRedissonService implementation
     */
    @Bean(name = "redissonService")
    public IRedissonService redissonService(RedissonClient redissonClient) {
        log.debug(AUTOWIRED_REDISSON_SERVICE);
        return new RedissonService(redissonClient);
    }
}
