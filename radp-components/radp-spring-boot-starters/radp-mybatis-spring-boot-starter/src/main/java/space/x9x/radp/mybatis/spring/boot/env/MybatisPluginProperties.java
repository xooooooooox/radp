package space.x9x.radp.mybatis.spring.boot.env;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import space.x9x.radp.spring.framework.bootstrap.constant.Globals;

import java.time.Duration;

/**
 * @author x9x
 * @since 2024-09-30 13:24
 */
@Data
@ConfigurationProperties(prefix = Globals.RADP_CONFIGURATION_PROPERTIES_PREFIX + MybatisPluginProperties.PREFIX)
public class MybatisPluginProperties {
    /**
     * Configuration prefix for MyBatis plugin properties.
     */
    public static final String PREFIX = "mybatis.plugin";
    /**
     * Configuration property key for enabling SQL logging.
     */
    public static final String SQL_LOG_ENABLED = PREFIX + ".sql-log.enabled";

    private final SqlLog sqlLog = new SqlLog();

    /**
     * Configuration properties for SQL logging.
     */
    @Data
    public static class SqlLog {
        private boolean enabled;
        private Duration slownessThreshold = Duration.ofMillis(1000);
    }
}
