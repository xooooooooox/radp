package space.x9x.radp.swagger3.spring.boot.env;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

import space.x9x.radp.spring.framework.bootstrap.constant.Globals;

/**
 * @author IO x9x
 * @since 2024-09-30 16:36
 */
@Getter
@Setter
@ConfigurationProperties(prefix = Globals.RADP_CONFIGURATION_PROPERTIES_PREFIX + "swagger3")
public class SwaggerProperties {
    private String contactEmail = "xozozsos@gmailcom";
    private String contactName = "x9x";
    private String contactUrl = null;
    private String defaultIncludePattern = "/api/.*";
    private String description = "API documentation";
    private String host = null;
    private String license = null;
    private String licenseUrl = null;
    private String[] protocols = {};
    private String termsOfServiceUrl = null;
    private String title = "Application API";
    private boolean useDefaultResponseMessages = false;
    private String version = "1.0.0";
}
