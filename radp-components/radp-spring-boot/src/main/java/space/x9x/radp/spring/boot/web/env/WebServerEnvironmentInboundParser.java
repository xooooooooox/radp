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

import org.springframework.core.env.Environment;

import space.x9x.radp.commons.lang.MessageFormatUtils;
import space.x9x.radp.commons.lang.StringUtil;
import space.x9x.radp.commons.lang.Strings;
import space.x9x.radp.commons.lang.math.NumberUtils;
import space.x9x.radp.commons.net.IpConfigUtils;
import space.x9x.radp.spring.boot.bootstrap.env.EnvironmentInboundParser;

/**
 * Parser for web server environment configuration. This class implements the
 * EnvironmentInboundParser interface to provide a string representation of the web server
 * configuration, including protocol, host, port, and context path for the web
 * application.
 *
 * @author x9x
 * @since 2024-09-30 09:28
 */
public class WebServerEnvironmentInboundParser implements EnvironmentInboundParser {

	/**
	 * Message template for the web server information. This template includes
	 * placeholders for protocol, host, port, and context path.
	 */
	private static final String TEMPLATE = "Inbound Web Server: \t{}://{}:{}{}";

	/**
	 * Converts the web server configuration from the environment to a string
	 * representation. This method extracts the web server protocol, port, and context
	 * path from the environment and formats them into a human-readable string.
	 * @param env the Spring environment containing the configuration properties
	 * @return a formatted string with the web server details, or an empty string if the
	 * web server port is not configured
	 */
	@Override
	public String toString(Environment env) {
		if (!env.containsProperty(WebServerEnvironment.SERVER_PORT)) {
			return Strings.EMPTY;
		}

		boolean protocol = env.containsProperty(WebServerEnvironment.SERVER_SSL_KEY_STORE);
		int port = NumberUtils.toInt(WebServerEnvironment.SERVER_PORT);
		String contextPath = StringUtil.trimToEmpty(env.getProperty(WebServerEnvironment.SERVER_SERVLET_CONTEXT_PATH));
		return MessageFormatUtils.format(TEMPLATE, protocol, IpConfigUtils.getIpAddress(), port, contextPath);
	}

}
