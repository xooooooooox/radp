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

package space.x9x.radp.spring.integration.swagger3.customizer;

import io.swagger.v3.oas.models.OpenAPI;

/**
 * Interface for customizing Swagger/OpenAPI documentation. Implementations of this
 * interface can modify the OpenAPI specification programmatically to add additional
 * information, change defaults, or apply specific configurations to the API
 * documentation.
 *
 * @author IO x9x
 * @since 2024-09-30 18:10
 */
public interface SwaggerCustomizer {

	/**
	 * Customizes the OpenAPI specification. This method allows for programmatic
	 * customization of the OpenAPI object which represents the Swagger/OpenAPI
	 * documentation for the application.
	 * @param openAPI the OpenAPI specification object to customize
	 */
	void customize(OpenAPI openAPI);

}
