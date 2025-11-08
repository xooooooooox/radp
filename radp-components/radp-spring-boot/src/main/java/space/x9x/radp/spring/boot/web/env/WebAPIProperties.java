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

package space.x9x.radp.spring.boot.web.env;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.springframework.boot.context.properties.ConfigurationProperties;

import space.x9x.radp.spring.framework.bootstrap.constant.Globals;
import space.x9x.radp.spring.framework.web.rest.config.ApiConfig;

/**
 * Configuration properties for web API functionality. This class defines properties that
 * can be set in application configuration files to customize web API behavior. It extends
 * ApiConfig to inherit common API configuration properties and adds additional properties
 * specific to the RADP framework.
 *
 * @author x9x
 * @since 2024-09-30 23:03
 */
@EqualsAndHashCode(callSuper = true)
@ToString
@Setter
@Getter
@ConfigurationProperties(prefix = WebAPIProperties.PREFIX)
public class WebAPIProperties extends ApiConfig {

	/**
	 * Configuration properties prefix.
	 */
	public static final String PREFIX = Globals.RADP_CONFIGURATION_PROPERTIES_PREFIX + "web";

	/**
	 * Flag to enable or disable the web API functionality. When set to true, web API
	 * features will be activated for the application.
	 */
	private boolean enabled = false;

}
