package space.x9x.radp.redis.spring.boot.support;

import java.time.Duration;

import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;

import org.jetbrains.annotations.NotNull;

import space.x9x.radp.commons.lang.StringUtils;
import space.x9x.radp.commons.lang.Strings;
import space.x9x.radp.commons.lang.math.NumberUtils;

/**
 * @author IO x9x
 * @since 2024-10-21 11:53
 */
public class TimeoutRedisCacheManager extends RedisCacheManager {

    private static final String SPLIT = Strings.HASH;

    /**
     * Constructs a new TimeoutRedisCacheManager with the specified cache writer and configuration.
     * This cache manager extends the standard RedisCacheManager to support custom cache
     * expiration times specified in the cache name using a hash (#) delimiter.
     *
     * @param cacheWriter             the Redis cache writer to use for cache operations
     * @param redisCacheConfiguration the default configuration to use for caches
     */
    public TimeoutRedisCacheManager(RedisCacheWriter cacheWriter,
                                    RedisCacheConfiguration redisCacheConfiguration) {
        super(cacheWriter, redisCacheConfiguration);
    }

    @Override
    protected @NotNull RedisCache createRedisCache(@NotNull String name, RedisCacheConfiguration cacheConfig) {
        if (StringUtils.isEmpty(name)) {
            return super.createRedisCache(name, cacheConfig);
        }
        // 如果使用 # 分隔符, 大小不为 2, 则说明不使用自定义火气时间
        String[] names = StringUtils.split(name, SPLIT);
        if (names.length != 2) {
            super.createRedisCache(name, cacheConfig);
        }
        // 核心: 通过修改 cacheConfig 的过期时间, 实现自定义过期时间
        if (cacheConfig != null) {
            // 移除 # 后面的 : 以及后见面的内容, 避免影响解析
            String ttlStr = StringUtils.substringBefore(names[1], Strings.COLON); // 获取 ttlStr 时间部分
            names[1] = StringUtils.substringAfter(names[1], ttlStr); // 移除掉 ttlStr 时间部分
            // 解析时间
            Duration duration = parseDuration(ttlStr);
            cacheConfig = cacheConfig.entryTtl(duration);
        }

        // 创建 RedisCache 对象, 需要忽略掉 ttlStr
        return super.createRedisCache(names[0] + names[1], cacheConfig);
    }

    /**
     * 解析过期时间 Duration
     *
     * @param ttlStr 过期时间字符串
     * @return 过期时间 Duration
     */
    private Duration parseDuration(String ttlStr) {
        String timeUnit = StringUtils.right(ttlStr, 1);
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
     * 移除多余的后缀, 返回具体的时间
     *
     * @param ttlStr 过期时间字符串
     * @return 时间
     */
    private Long removeDurationSuffix(String ttlStr) {
        return NumberUtils.toLong(StringUtils.substring(ttlStr, 0, ttlStr.length() - 1));
    }
}
