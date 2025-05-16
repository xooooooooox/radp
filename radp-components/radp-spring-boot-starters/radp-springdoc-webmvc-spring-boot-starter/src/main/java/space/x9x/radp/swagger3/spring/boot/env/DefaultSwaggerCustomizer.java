package space.x9x.radp.swagger3.spring.boot.env;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import lombok.Setter;
import org.springframework.core.Ordered;
import space.x9x.radp.spring.integration.swagger3.customizer.SwaggerCustomizer;

/**
 * @author x9x
 * @since 2024-09-30 18:14
 */
public class DefaultSwaggerCustomizer implements SwaggerCustomizer, Ordered {

    /**
     * Default order value for this customizer.
     * This ensures the customizer is applied in a predictable order relative to other customizers.
     */
    public static final int DEFAULT_ORDER = 0;

    private final SwaggerProperties properties;

    @Setter
    private int order = DEFAULT_ORDER;

    /**
     * Constructs a new DefaultSwaggerCustomizer with the specified properties.
     * This customizer uses the provided properties to configure the OpenAPI documentation.
     *
     * @param properties the Swagger configuration properties to use
     */
    public DefaultSwaggerCustomizer(SwaggerProperties properties) {
        this.properties = properties;
    }

    @Override
    public void customize(OpenAPI openAPI) {
        Info info = new Info()
                .title(properties.getTitle())
                .description(properties.getDescription())
                .version(properties.getVersion())
                .contact(new Contact()
                        .name(properties.getContactName())
                        .url(properties.getContactUrl())
                        .email(properties.getContactEmail()))
                .license(new License()
                        .name(properties.getLicense())
                        .url(properties.getLicenseUrl()));

        openAPI.info(info);
    }

    @Override
    public int getOrder() {
        return order;
    }

}
