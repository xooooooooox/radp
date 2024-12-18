package space.x9x.radp.swagger3.spring.boot.autoconfigure;

import space.x9x.radp.commons.lang.ObjectUtils;
import space.x9x.radp.spring.boot.bootstrap.constants.Conditions;
import space.x9x.radp.spring.framework.bootstrap.constant.SpringProperties;
import space.x9x.radp.spring.integration.swagger3.customizer.SwaggerCustomizer;
import space.x9x.radp.swagger3.spring.boot.env.DefaultSwaggerCustomizer;
import space.x9x.radp.swagger3.spring.boot.env.SwaggerProperties;
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
    public static final String DEFAULT_GROUP_NAME = "management";
    public static final String MSG_AUTOWIRED_SWAGGER = "Autowired Swagger";
    public static final String MSG_STARTED_SWAGGER = "Started Swagger in {} ms";

    private final SwaggerProperties properties;

    @Value(SpringProperties.NAME_PATTERN)
    private String applicationName;

    public RadpSwaggerAutoConfiguration(SwaggerProperties properties) {
        this.properties = properties;
        if (ObjectUtils.isEmpty(properties.getTitle())) {
            properties.setTitle(applicationName);
        }
    }

    @Bean
    public DefaultSwaggerCustomizer swaggerCustomizer() {
        return new DefaultSwaggerCustomizer(properties);
    }

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


    private OpenAPI createOpenAPI() {
        return new OpenAPI();
    }
}
