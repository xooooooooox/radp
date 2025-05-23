package space.x9x.radp.spring.integration.swagger3.customizer;

import io.swagger.v3.oas.models.OpenAPI;

/**
 * @author x9x
 * @since 2024-09-30 18:10
 */
public interface SwaggerCustomizer {

    /**
     * Customizes the OpenAPI specification with implementation-specific configurations.
     * This method is called during application startup to modify or enhance the OpenAPI
     * documentation with additional information such as metadata, security schemes,
     * or other custom configurations.
     *
     * @param openAPI the OpenAPI specification to customize
     */
    void customize(OpenAPI openAPI);
}
