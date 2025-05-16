package space.x9x.radp.spring.boot.logging.env;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import space.x9x.radp.spring.framework.bootstrap.constant.Globals;
import space.x9x.radp.spring.framework.logging.bootstrap.config.BootstrapLogConfig;

/**
 * @author x9x
 * @since 2024-09-28 20:47
 */
@EqualsAndHashCode(callSuper = true)
@ToString
@Setter
@Getter
@ConfigurationProperties(prefix = BootstrapLogProperties.PREFIX)
public class BootstrapLogProperties extends BootstrapLogConfig {

    /**
     * Configuration properties prefix for bootstrap logging.
     * This constant defines the prefix used for all bootstrap logging configuration properties.
     */
    public static final String PREFIX = Globals.RADP_CONFIGURATION_PROPERTIES_PREFIX + "logging.bootstrap";

    /**
     * Property name for enabling/disabling bootstrap logging.
     * This property controls whether bootstrap logging is enabled or disabled.
     */
    public static final String ENABLED = PREFIX + ".enabled";

    /**
     * 是否开启日志配置
     */
    private boolean enabled = true;
}
