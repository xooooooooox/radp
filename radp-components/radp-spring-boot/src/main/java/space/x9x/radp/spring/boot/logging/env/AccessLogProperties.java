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

package space.x9x.radp.spring.boot.logging.env;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import space.x9x.radp.spring.framework.bootstrap.constant.Globals;
import space.x9x.radp.spring.framework.logging.access.config.AccessLogConfig;

/**
 * Configuration properties for access logging. This class defines properties that can be
 * set in application configuration files to customize access logging behavior. It extends
 * AccessLogConfig to inherit common access logging configuration properties and adds
 * additional properties specific to the RADP framework.
 *
 * @author IO x9x
 * @since 2024-09-30 09:44
 */
@EqualsAndHashCode(callSuper = true)
@ToString
@Setter
@Getter
@ConfigurationProperties(prefix = AccessLogProperties.PREFIX)
public class AccessLogProperties extends AccessLogConfig {

	/**
	 * Configuration properties prefix for access logging. This constant defines the
	 * prefix used for all access logging configuration properties.
	 */
	public static final String PREFIX = Globals.RADP_CONFIGURATION_PROPERTIES_PREFIX + "logging.access";

	/**
	 * Flag to enable or disable the access logging aspect. When set to true, access
	 * logging will be activated for the application.
	 */
	private boolean enabled = false;

}
