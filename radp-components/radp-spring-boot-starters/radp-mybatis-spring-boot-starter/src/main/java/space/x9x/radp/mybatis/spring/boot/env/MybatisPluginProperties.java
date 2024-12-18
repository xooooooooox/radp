package space.x9x.radp.mybatis.spring.boot.env;

import space.x9x.radp.spring.framework.bootstrap.constant.Globals;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

/**
 * @author x9x
 * @since 2024-09-30 13:24
 */
@Data
@ConfigurationProperties(prefix = Globals.RADP_CONFIGURATION_PROPERTIES_PREFIX + MybatisPluginProperties.PREFIX)
public class MybatisPluginProperties {
    public static final String PREFIX = "mybatis.plugin";
    public static final String SQL_LOG_ENABLED = PREFIX + ".sql-log.enabled";

    private final SqlLog sqlLog = new SqlLog();

    @Data
    public static class SqlLog {
        private boolean enabled;
        private Duration slownessThreshold = Duration.ofMillis(1000);
    }
}
