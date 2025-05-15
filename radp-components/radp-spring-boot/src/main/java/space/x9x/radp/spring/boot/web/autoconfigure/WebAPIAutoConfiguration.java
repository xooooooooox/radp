package space.x9x.radp.spring.boot.web.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Role;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import space.x9x.radp.spring.boot.web.env.WebAPIProperties;

/**
 * Auto-configuration for Web API endpoints.
 * This class configures path matching for REST controllers, allowing a common prefix
 * to be applied to all REST API endpoints in the application. The prefix is configurable
 * through the WebAPIProperties.
 *
 * @author x9x
 * @since 2024-09-30 23:08
 */
@AutoConfiguration
@EnableConfigurationProperties(WebAPIProperties.class)
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@Slf4j
public class WebAPIAutoConfiguration implements WebMvcConfigurer {


    private final WebAPIProperties properties;

    /**
     * Constructs a new WebAPIAutoConfiguration with the specified properties.
     * This constructor is automatically called by Spring's dependency injection system
     * when the auto-configuration is enabled.
     *
     * @param properties the WebAPIProperties containing configuration for API endpoints
     */
    public WebAPIAutoConfiguration(WebAPIProperties properties) {
        log.debug("Autowired webApiAutoConfiguration");
        this.properties = properties;
    }

    /**
     * Configures path matching for REST controllers.
     * This method adds a common prefix to all endpoints defined in classes annotated with @RestController.
     * The prefix is obtained from the WebAPIProperties configuration.
     *
     * @param configurer the PathMatchConfigurer to be configured
     */
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.addPathPrefix(properties.getPrefix(), clazz -> clazz.isAnnotationPresent(RestController.class));
    }
}
