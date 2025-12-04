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
 * Default implementation of SwaggerCustomizer that configures the OpenAPI documentation
 * based on properties defined in SwaggerProperties. This customizer sets the title,
 * description, version, contact information, and license details in the OpenAPI
 * specification. It also implements the Ordered interface to control its execution order
 * when multiple customizers are present.
 *
 * @author RADP x9x
 * @since 2024-09-30 18:14
 */
public class DefaultSwaggerCustomizer implements SwaggerCustomizer, Ordered {

	/**
	 * Default order value for this customizer. This constant defines the default priority
	 * of this customizer when multiple SwaggerCustomizer implementations are present.
	 */
	public static final int DEFAULT_ORDER = 0;

	/**
	 * The SwaggerProperties containing configuration for OpenAPI documentation. This
	 * includes title, description, version, contact information, and license details.
	 */
	private final SwaggerProperties properties;

	/**
	 * The order value for this customizer, which determines its execution priority when
	 * multiple SwaggerCustomizer implementations are present.
	 */
	@Setter
	private int order = DEFAULT_ORDER;

	/**
	 * Constructs a new DefaultSwaggerCustomizer with the specified properties. This
	 * constructor initializes the customizer with the provided SwaggerProperties, which
	 * will be used to configure the OpenAPI documentation.
	 * @param properties the SwaggerProperties containing configuration for Swagger
	 * documentation
	 */
	public DefaultSwaggerCustomizer(SwaggerProperties properties) {
		this.properties = properties;
	}

	/**
	 * Customizes the OpenAPI documentation with information from SwaggerProperties. This
	 * method configures the Info object with title, description, version, contact
	 * information, and license details from the properties.
	 * @param openAPI the OpenAPI instance to customize
	 */
	@Override
	public void customize(OpenAPI openAPI) {
		Info info = new Info().title(this.properties.getTitle())
			.description(this.properties.getDescription())
			.version(this.properties.getVersion())
			.contact(new Contact().name(this.properties.getContactName())
				.url(this.properties.getContactUrl())
				.email(this.properties.getContactEmail()))
			.license(new License().name(this.properties.getLicense()).url(this.properties.getLicenseUrl()));

		openAPI.info(info);
	}

	/**
	 * Returns the order value of this customizer.
	 * @return the order value that determines the execution priority of this customizer
	 */
	@Override
	public int getOrder() {
		return this.order;
	}

}
