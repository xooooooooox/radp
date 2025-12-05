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

package space.x9x.radp.dubbo.spring.cloud.env;

import org.springframework.core.env.Environment;

import space.x9x.radp.commons.lang.MessageFormatUtils;
import space.x9x.radp.commons.lang.StringConstants;
import space.x9x.radp.commons.lang.StringUtil;
import space.x9x.radp.commons.lang.math.NumberUtil;
import space.x9x.radp.spring.boot.bootstrap.constants.Conditions;
import space.x9x.radp.spring.boot.bootstrap.env.EnvironmentInboundParser;

/**
 * Parser for Dubbo environment information to be displayed in inbound connection logs.
 * This class implements the EnvironmentInboundParser interface to provide formatted
 * information about Dubbo provider connections, including protocol and port details. When
 * Dubbo is disabled, it returns an empty string.
 *
 * @author RADP x9x
 * @since 2024-10-03 01:15
 */
public class DubboEnvironmentInboundParser implements EnvironmentInboundParser {

	/**
	 * Template string for formatting Dubbo provider information. The placeholders are
	 * replaced with protocol, port, and protocol again.
	 */
	private static final String TEMPLATE = "Inbound Dubbo Provider: \t{}://{}:{}";

	/**
	 * Converts Dubbo environment configuration to a formatted string representation. If
	 * Dubbo is disabled, returns an empty string. Otherwise, returns a formatted string
	 * containing the protocol and port information.
	 * @param env the Spring environment containing Dubbo configuration properties
	 * @return a formatted string with Dubbo provider information or empty string if
	 * disabled
	 */
	@Override
	public String toString(Environment env) {
		boolean disabled = !Boolean.parseBoolean(env.getProperty(DubboEnvironment.ENABLED, Conditions.TRUE));
		if (disabled) {
			return StringConstants.EMPTY;
		}
		String protocol = StringUtil.trimToEmpty(env.getProperty(DubboEnvironment.PROTOCOL));
		int port = NumberUtil.toInt(env.getProperty(DubboEnvironment.PORT));
		return MessageFormatUtils.format(TEMPLATE, protocol, port, protocol);
	}

}
