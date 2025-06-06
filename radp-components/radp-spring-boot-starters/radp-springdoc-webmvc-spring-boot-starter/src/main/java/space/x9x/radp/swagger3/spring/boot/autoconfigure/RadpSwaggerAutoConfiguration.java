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

package space.x9x.radp.swagger3.spring.boot.autoconfigure;

import java.util.List;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import jakarta.servlet.Servlet;
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

import space.x9x.radp.commons.lang.ObjectUtils;
import space.x9x.radp.spring.boot.bootstrap.constants.Conditions;
import space.x9x.radp.spring.framework.bootstrap.constant.SpringProperties;
import space.x9x.radp.spring.integration.swagger3.customizer.SwaggerCustomizer;
import space.x9x.radp.swagger3.spring.boot.env.DefaultSwaggerCustomizer;
import space.x9x.radp.swagger3.spring.boot.env.SwaggerProperties;

/**
 * Autoconfiguration for Swagger/OpenAPI documentation in Spring Boot applications. This
 * class provides automatic configuration for Swagger documentation based on application
 * properties. It creates and configures the necessary beans for generating OpenAPI
 * documentation, including customizers that apply the configured properties to the
 * OpenAPI specification.
 *
 * @author IO x9x
 * @since 2024-09-30 16:40
 */
@ConditionalOnClass({ Info.class, Servlet.class, DispatcherServlet.class })
@ConditionalOnProperty(prefix = "springdoc.api-docs", name = Conditions.ENABLED, havingValue = Conditions.TRUE,
		matchIfMissing = true)
@ConditionalOnWebApplication
@EnableConfigurationProperties(SwaggerProperties.class)
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@AutoConfiguration
@Slf4j
public class RadpSwaggerAutoConfiguration {

	/**
	 * Default group name for Swagger documentation. This constant defines the default
	 * group name used for management endpoints.
	 */
	public static final String DEFAULT_GROUP_NAME = "management";

	/**
	 * Log message for when Swagger is autowired. This constant is used for logging when
	 * the Swagger configuration is autowired.
	 */
	public static final String MSG_AUTOWIRED_SWAGGER = "Autowired Swagger";

	/**
	 * Log message template for Swagger startup time. This constant is used for logging
	 * the time taken to start Swagger.
	 */
	public static final String MSG_STARTED_SWAGGER = "Started Swagger in {} ms";

	/**
	 * The SwaggerProperties containing configuration for OpenAPI documentation. This
	 * field stores the properties that define how the Swagger documentation is generated,
	 * including title, description, and contact information.
	 */
	private final SwaggerProperties properties;

	/**
	 * The name of the application, injected from Spring properties. This field is used as
	 * the default title for the Swagger documentation if no title is explicitly set in
	 * the properties.
	 */
	@Value(SpringProperties.NAME_PATTERN)
	private String applicationName;

	/**
	 * Constructs a new RadpSwaggerAutoConfiguration with the specified properties. This
	 * constructor initializes the auto-configuration with the provided SwaggerProperties
	 * and sets the title to the application name if it's not already set.
	 * @param properties the SwaggerProperties containing configuration for Swagger
	 * documentation
	 */
	public RadpSwaggerAutoConfiguration(SwaggerProperties properties) {
		this.properties = properties;
		if (ObjectUtils.isEmpty(properties.getTitle())) {
			properties.setTitle(this.applicationName);
		}
	}

	/**
	 * Creates and configures a DefaultSwaggerCustomizer bean. This method provides a
	 * customizer that applies the configured SwaggerProperties to the OpenAPI
	 * documentation.
	 * @return a configured DefaultSwaggerCustomizer instance
	 */
	@Bean
	public DefaultSwaggerCustomizer swaggerCustomizer() {
		return new DefaultSwaggerCustomizer(this.properties);
	}

	/**
	 * Creates and configures the OpenAPI bean for Swagger documentation. This method
	 * creates a new OpenAPI instance and applies all available SwaggerCustomizer
	 * implementations to it. It also logs the time taken to initialize Swagger.
	 * @param swaggerCustomizers the list of SwaggerCustomizer implementations to apply
	 * @return a configured OpenAPI instance
	 */
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

	/**
	 * Creates a new, empty OpenAPI instance. This method provides a base OpenAPI object
	 * that will be customized by SwaggerCustomizer instances.
	 * @return a new OpenAPI instance
	 */
	private OpenAPI createOpenAPI() {
		return new OpenAPI();
	}

}
