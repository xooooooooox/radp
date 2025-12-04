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

package space.x9x.radp.spring.boot.actuate.env;

import org.springframework.core.env.Environment;

import space.x9x.radp.commons.lang.MessageFormatUtils;
import space.x9x.radp.commons.lang.StrUtils;
import space.x9x.radp.commons.lang.Strings;
import space.x9x.radp.commons.lang.math.NumberUtils;
import space.x9x.radp.commons.net.IpConfigUtils;
import space.x9x.radp.spring.boot.bootstrap.env.EnvironmentInboundParser;

/**
 * Parser for Spring Boot Actuator management server environment configuration. This class
 * implements the EnvironmentInboundParser interface to provide a string representation of
 * the management server configuration, including protocol, host, port, and base path for
 * endpoints.
 *
 * @author x9x
 * @since 2024-09-30 09:21
 */
public class ManagementServerEnvironmentInboundParser implements EnvironmentInboundParser {

	/**
	 * Message template for the management server information. This template includes
	 * placeholders for protocol, host, port, and base path.
	 */
	private static final String TEMPLATE = "Inbound Management Server: \t{}://{}:{}{}";

	/**
	 * Protocol used for the management server. Currently fixed to HTTP.
	 */
	private static final String PROTOCOL = "http";

	/**
	 * Converts the management server configuration from the environment to a string
	 * representation. This method extracts the management server port and base path from
	 * the environment and formats them into a human-readable string.
	 * @param env the Spring environment containing the configuration properties
	 * @return a formatted string with the management server details, or an empty string
	 * if the management server port is not configured
	 */
	@Override
	public String toString(Environment env) {
		if (!env.containsProperty(ManagementServerEnvironment.PORT)) {
			return Strings.EMPTY;
		}

		int port = NumberUtils.toInt(env.getProperty(ManagementServerEnvironment.PORT));
		String basePath = StrUtils.trimToEmpty(env.getProperty(ManagementServerEnvironment.ENDPOINTS_WEB_BASE_PATH));
		return MessageFormatUtils.format(TEMPLATE, PROTOCOL, IpConfigUtils.getIpAddress(), port, basePath);
	}

}
