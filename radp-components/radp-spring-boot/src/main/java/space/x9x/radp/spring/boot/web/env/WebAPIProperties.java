package space.x9x.radp.spring.boot.web.env;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import space.x9x.radp.spring.framework.bootstrap.constant.Globals;
import space.x9x.radp.spring.framework.web.rest.config.ApiConfig;

/**
 * @author IO x9x
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
