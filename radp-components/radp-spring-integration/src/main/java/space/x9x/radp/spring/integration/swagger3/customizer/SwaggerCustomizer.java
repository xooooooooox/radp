package space.x9x.radp.spring.integration.swagger3.customizer;

import io.swagger.v3.oas.models.OpenAPI;

/**
 * @author IO x9x
 * @since 2024-09-30 18:10
 */
public interface SwaggerCustomizer {

    /**
     * Customizes the OpenAPI specification.
     * This method allows for programmatic customization of the OpenAPI object
     * which represents the Swagger/OpenAPI documentation for the application.
     *
     * @param openAPI the OpenAPI specification object to customize
     */
    void customize(OpenAPI openAPI);
}
