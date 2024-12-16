package com.x9x.radp.redis.spring.boot.env;

import com.x9x.radp.spring.framework.bootstrap.constant.Globals;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author x9x
 * @since 2024-10-21 11:45
 */
@Data
@ConfigurationProperties(prefix = Globals.RADP_CONFIGURATION_PROPERTIES_PREFIX + "cache")
public class ExtendedCacheProperties {

    private static final Integer REDIS_SCAN_BATCH_SIZE_DEFAULT = 30;

    /**
     * Redis Scan 一次返回数量
     */
    private Integer redisScanBatchSize = REDIS_SCAN_BATCH_SIZE_DEFAULT;
}
