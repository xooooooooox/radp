/*
 * Copyright 2012-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
     * This constant defines the default priority of this customizer when multiple
     * SwaggerCustomizer implementations are present.
     */
    public static final int DEFAULT_ORDER = 0;

    private final SwaggerProperties properties;

    @Setter
    private int order = DEFAULT_ORDER;

    /**
     * Constructs a new DefaultSwaggerCustomizer with the specified properties.
     * This constructor initializes the customizer with the provided SwaggerProperties,
     * which will be used to configure the OpenAPI documentation.
     *
     * @param properties the SwaggerProperties containing configuration for Swagger documentation
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
