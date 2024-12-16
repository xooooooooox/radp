package com.x9x.radp.spring.boot.logging.env;

import com.x9x.radp.spring.framework.bootstrap.constant.Globals;
import com.x9x.radp.spring.framework.logging.bootstrap.config.BootstrapLogConfig;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

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

    public static final String PREFIX = Globals.RADP_CONFIGURATION_PROPERTIES_PREFIX + "logging.bootstrap";
    public static final String ENABLED = PREFIX + ".enabled";

    /**
     * 是否开启日志配置
     */
    private boolean enabled = true;
}
