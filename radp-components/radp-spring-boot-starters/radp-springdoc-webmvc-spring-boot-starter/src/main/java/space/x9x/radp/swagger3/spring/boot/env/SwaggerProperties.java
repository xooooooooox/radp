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

import lombok.Getter;
import lombok.Setter;

import org.springframework.boot.context.properties.ConfigurationProperties;

import space.x9x.radp.spring.framework.bootstrap.constant.Globals;

/**
 * Configuration properties for Swagger/OpenAPI documentation. This class defines
 * properties that can be set in application configuration files to customize the OpenAPI
 * documentation generated for the application. It includes properties for contact
 * information, API description, license details, and other metadata.
 *
 * @author IO x9x
 * @since 2024-09-30 16:36
 */
@Getter
@Setter
@ConfigurationProperties(prefix = Globals.RADP_CONFIGURATION_PROPERTIES_PREFIX + "swagger3")
public class SwaggerProperties {

	/**
	 * The email address of the API contact person or team. This appears in the contact
	 * section of the OpenAPI documentation.
	 */
	private String contactEmail = "xozozsos@gmailcom";

	/**
	 * The name of the API contact person or team. This appears in the contact section of
	 * the OpenAPI documentation.
	 */
	private String contactName = "x9x";

	/**
	 * The URL of the API contact person or team. This appears in the contact section of
	 * the OpenAPI documentation.
	 */
	private String contactUrl = null;

	/**
	 * The regex pattern for paths to include in the API documentation. Only paths
	 * matching this pattern will be included in the generated documentation.
	 */
	private String defaultIncludePattern = "/api/.*";

	/**
	 * The general description of the API. This appears in the info section of the OpenAPI
	 * documentation.
	 */
	private String description = "API documentation";

	/**
	 * The host name or IP address serving the API. This is used to generate server URLs
	 * in the OpenAPI documentation.
	 */
	private String host = null;

	/**
	 * The name of the license under which the API is available. This appears in the
	 * license section of the OpenAPI documentation.
	 */
	private String license = null;

	/**
	 * The URL of the license under which the API is available. This appears in the
	 * license section of the OpenAPI documentation.
	 */
	private String licenseUrl = null;

	/**
	 * The protocols supported by the API (e.g., "http", "https"). This is used to
	 * generate server URLs in the OpenAPI documentation.
	 */
	private String[] protocols = {};

	/**
	 * The URL pointing to the terms of service for the API. This appears in the info
	 * section of the OpenAPI documentation.
	 */
	private String termsOfServiceUrl = null;

	/**
	 * The title of the API. This appears as the main heading in the OpenAPI
	 * documentation.
	 */
	private String title = "Application API";

	/**
	 * Whether to use default response messages for HTTP methods. If set to false, only
	 * explicitly documented responses will be included in the documentation.
	 */
	private boolean useDefaultResponseMessages = false;

	/**
	 * The version of the API. This appears in the info section of the OpenAPI
	 * documentation.
	 */
	private String version = "1.0.0";

}
