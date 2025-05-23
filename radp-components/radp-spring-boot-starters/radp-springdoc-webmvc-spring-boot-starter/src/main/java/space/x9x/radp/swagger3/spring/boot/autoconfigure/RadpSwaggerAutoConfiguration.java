package space.x9x.radp.swagger3.spring.boot.autoconfigure;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Role;
import org.springframework.util.StopWatch;
import org.springframework.web.servlet.DispatcherServlet;
import space.x9x.radp.commons.lang.ObjectUtils;
import space.x9x.radp.spring.boot.bootstrap.constants.Conditions;
import space.x9x.radp.spring.framework.bootstrap.constant.SpringProperties;
import space.x9x.radp.spring.integration.swagger3.customizer.SwaggerCustomizer;
import space.x9x.radp.swagger3.spring.boot.env.DefaultSwaggerCustomizer;
import space.x9x.radp.swagger3.spring.boot.env.SwaggerProperties;

import javax.servlet.Servlet;
import java.util.List;

/**
 * @author x9x
 * @since 2024-09-30 16:40
 */
@ConditionalOnClass({
        Info.class,
        Servlet.class,
        DispatcherServlet.class
})
@ConditionalOnProperty(
        prefix = "springdoc.api-docs",
        name = Conditions.ENABLED,
        havingValue = Conditions.TRUE,
        matchIfMissing = true
)
@ConditionalOnWebApplication
@EnableConfigurationProperties(SwaggerProperties.class)
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@AutoConfiguration
@Slf4j
public class RadpSwaggerAutoConfiguration {
    /**
     * Default group name for Swagger documentation.
     */
    public static final String DEFAULT_GROUP_NAME = "management";

    /**
     * Log message for when Swagger is autowired.
     */
    public static final String MSG_AUTOWIRED_SWAGGER = "Autowired Swagger";

    /**
     * Log message template for when Swagger has started, with placeholder for time taken.
     */
    public static final String MSG_STARTED_SWAGGER = "Started Swagger in {} ms";

    private final SwaggerProperties properties;

    @Value(SpringProperties.NAME_PATTERN)
    private String applicationName;

    /**
     * Constructs a new RadpSwaggerAutoConfiguration with the specified properties.
     * If the title property is not set, it will be initialized with the application name.
     *
     * @param properties the Swagger configuration properties to use
     */
    public RadpSwaggerAutoConfiguration(SwaggerProperties properties) {
        this.properties = properties;
        if (ObjectUtils.isEmpty(properties.getTitle())) {
            properties.setTitle(applicationName);
        }
    }

    /**
     * Creates a DefaultSwaggerCustomizer bean that configures the OpenAPI documentation.
     * This customizer applies the configured properties to the OpenAPI object.
     *
     * @return a DefaultSwaggerCustomizer instance initialized with the configuration properties
     */
    @Bean
    public DefaultSwaggerCustomizer swaggerCustomizer() {
        return new DefaultSwaggerCustomizer(properties);
    }

    /**
     * Creates and configures the OpenAPI bean for Swagger documentation.
     * This method creates a base OpenAPI object and applies all available SwaggerCustomizer
     * instances to it. It also logs performance metrics for the initialization process.
     *
     * @param swaggerCustomizers a list of customizers to apply to the OpenAPI object
     * @return a fully configured OpenAPI object for Swagger documentation
     */
    @Bean
    @ConditionalOnMissingBean(name = "swaggerOpenAPI")
    public OpenAPI swaggerOpenAPI(List<SwaggerCustomizer> swaggerCustomizers) {
        log.debug(MSG_AUTOWIRED_SWAGGER);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        OpenAPI openAPI = createOpenAPI();
        for (SwaggerCustomizer swaggerCustomizer : swaggerCustomizers) {
            swaggerCustomizer.customize(openAPI);
        }
        stopWatch.stop();
        log.debug(MSG_STARTED_SWAGGER, stopWatch.getTotalTimeMillis());
        return openAPI;
    }


    /**
     * Creates a new, empty OpenAPI instance.
     * This method provides a base OpenAPI object that will be customized by SwaggerCustomizer instances.
     *
     * @return a new OpenAPI instance
     */
    private OpenAPI createOpenAPI() {
        return new OpenAPI();
    }
}
