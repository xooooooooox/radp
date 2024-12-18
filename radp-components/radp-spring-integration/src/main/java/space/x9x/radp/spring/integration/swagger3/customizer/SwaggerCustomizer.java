package space.x9x.radp.spring.integration.swagger3.customizer;

import io.swagger.v3.oas.models.OpenAPI;

/**
 * @author x9x
 * @since 2024-09-30 18:10
 */
public interface SwaggerCustomizer {

    void customize(OpenAPI openAPI);
}
