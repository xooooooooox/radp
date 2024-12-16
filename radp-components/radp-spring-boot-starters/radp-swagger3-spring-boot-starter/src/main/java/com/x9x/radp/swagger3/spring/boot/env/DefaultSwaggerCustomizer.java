package com.x9x.radp.swagger3.spring.boot.env;

import com.x9x.radp.spring.integration.swagger3.customizer.SwaggerCustomizer;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import lombok.Setter;
import org.springframework.core.Ordered;

/**
 * @author x9x
 * @since 2024-09-30 18:14
 */
public class DefaultSwaggerCustomizer implements SwaggerCustomizer, Ordered {

    public static final int DEFAULT_ORDER = 0;

    private final SwaggerProperties properties;

    @Setter
    private int order = DEFAULT_ORDER;

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
