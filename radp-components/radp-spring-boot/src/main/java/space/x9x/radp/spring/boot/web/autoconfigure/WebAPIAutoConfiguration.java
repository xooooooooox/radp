package space.x9x.radp.spring.boot.web.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Role;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import space.x9x.radp.spring.boot.web.env.WebAPIProperties;

/**
 * @author x9x
 * @since 2024-09-30 23:08
 */
@AutoConfiguration
@EnableConfigurationProperties(WebAPIProperties.class)
@ConditionalOnClass(WebMvcConfigurer.class)
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@Slf4j
public class WebAPIAutoConfiguration implements WebMvcConfigurer {


    private final WebAPIProperties properties;

    /**
     * Constructs a new WebAPIAutoConfiguration with the specified properties.
     * This constructor initializes the auto-configuration with the provided WebAPIProperties,
     * which will be used to configure the path prefix for REST controllers.
     *
     * @param properties the WebAPIProperties containing configuration for web API endpoints
     */
    public WebAPIAutoConfiguration(WebAPIProperties properties) {
        log.debug("Autowired webApiConfiguration");
        this.properties = properties;
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.addPathPrefix(properties.getPrefix(), clazz -> clazz.isAnnotationPresent(RestController.class));
    }
}
