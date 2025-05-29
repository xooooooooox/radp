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

package space.x9x.radp.spring.boot.web.autoconfigure;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Role;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.extern.slf4j.Slf4j;

import space.x9x.radp.spring.boot.web.env.WebAPIProperties;

/**
 * Autoconfiguration for Web API endpoints. This class configures path matching for REST
 * controllers, allowing a common prefix to be applied to all REST API endpoints in the
 * application. The prefix is configurable through the WebAPIProperties.
 *
 * @author IO x9x
 * @since 2024-09-30 23:08
 */
@AutoConfiguration
@EnableConfigurationProperties(WebAPIProperties.class)
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@Slf4j
public class WebAPIAutoConfiguration implements WebMvcConfigurer {

	/**
	 * The WebAPIProperties containing configuration for API endpoints. This field holds
	 * the properties that control the behavior of the web API, including the prefix
	 * applied to REST controller endpoints.
	 */
	private final WebAPIProperties properties;

	/**
	 * Constructs a new WebAPIAutoConfiguration with the specified properties. This
	 * constructor is automatically called by Spring's dependency injection system when
	 * the autoconfiguration is enabled.
	 * @param properties the WebAPIProperties containing configuration for API endpoints
	 */
	public WebAPIAutoConfiguration(WebAPIProperties properties) {
		log.debug("Autowired webApiAutoConfiguration");
		this.properties = properties;
	}

	/**
	 * Configures path matching for REST controllers. This method adds a common prefix to
	 * all endpoints defined in classes annotated with @RestController. The prefix is
	 * obtained from the WebAPIProperties configuration.
	 * @param configurer the PathMatchConfigurer to be configured
	 */
	@Override
	public void configurePathMatch(PathMatchConfigurer configurer) {
		configurer.addPathPrefix(this.properties.getPrefix(), clazz -> clazz.isAnnotationPresent(RestController.class));
	}

}
