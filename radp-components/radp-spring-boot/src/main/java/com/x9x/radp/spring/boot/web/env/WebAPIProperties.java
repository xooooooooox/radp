package com.x9x.radp.spring.boot.web.env;

import com.x9x.radp.spring.framework.bootstrap.constant.Globals;
import com.x9x.radp.spring.framework.web.rest.config.ApiConfig;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author x9x
 * @since 2024-09-30 23:03
 */
@EqualsAndHashCode(callSuper = true)
@ToString
@Setter
@Getter
@ConfigurationProperties(prefix = Globals.RADP_CONFIGURATION_PROPERTIES_PREFIX + "web")
public class WebAPIProperties extends ApiConfig {

    private boolean enabled = false;
}
