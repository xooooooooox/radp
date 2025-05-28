package space.x9x.radp.mybatis.spring.boot.env;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

import space.x9x.radp.spring.framework.bootstrap.constant.Globals;

/**
 * Configuration properties for MyBatis plugins. This class defines settings for various
 * MyBatis plugins, particularly SQL logging functionality.
 *
 * @author IO x9x
 * @since 2024-09-30 13:24
 */
@Data
@ConfigurationProperties(prefix = Globals.RADP_CONFIGURATION_PROPERTIES_PREFIX + MybatisPluginProperties.PREFIX)
public class MybatisPluginProperties {
    /**
     * Configuration prefix for MyBatis plugin properties.
     * This constant is used as part of the property path in application configuration.
     */
    public static final String PREFIX = "mybatis.plugin";

    /**
     * Property path for enabling SQL logging.
     * This constant represents the full property path to enable/disable SQL logging.
     */
    public static final String SQL_LOG_ENABLED = PREFIX + ".sql-log.enabled";

    private final SqlLog sqlLog = new SqlLog();

    /**
     * Configuration properties for SQL logging functionality.
     * This inner class contains settings related to SQL execution logging,
     * including enabling/disabling the feature and setting thresholds for slow query detection.
     */
    @Data
    public static class SqlLog {
        private boolean enabled;
        private Duration slownessThreshold = Duration.ofMillis(1000);
    }
}
