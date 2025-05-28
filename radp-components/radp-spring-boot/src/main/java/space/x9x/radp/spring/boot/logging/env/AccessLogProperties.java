package space.x9x.radp.spring.boot.logging.env;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import space.x9x.radp.spring.framework.bootstrap.constant.Globals;
import space.x9x.radp.spring.framework.logging.access.config.AccessLogConfig;

/**
 * @author IO x9x
 * @since 2024-09-30 09:44
 */
@EqualsAndHashCode(callSuper = true)
@ToString
@Setter
@Getter
@ConfigurationProperties(prefix = AccessLogProperties.PREFIX)
public class AccessLogProperties extends AccessLogConfig {

    /**
     * Configuration properties prefix for access logging.
     * This constant defines the prefix used for all access logging configuration properties.
     */
    public static final String PREFIX = Globals.RADP_CONFIGURATION_PROPERTIES_PREFIX + "logging.access";

    /**
     * 是否开启日志切面
     */
    private boolean enabled = false;
}
