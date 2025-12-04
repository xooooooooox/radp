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

package space.x9x.radp.spring.boot.jdbc.env;

import org.springframework.core.env.Environment;

import space.x9x.radp.commons.lang.MessageFormatUtils;
import space.x9x.radp.commons.lang.StringConstants;
import space.x9x.radp.commons.lang.StringUtil;
import space.x9x.radp.spring.boot.bootstrap.env.EnvironmentOutboundParser;

/**
 * Parser for datasource environment configuration. This class implements the
 * EnvironmentOutboundParser interface to provide a string representation of the
 * datasource configuration, extracting the JDBC URL from the Spring environment and
 * formatting it for display in logs or monitoring tools.
 *
 * @author x9x
 * @since 2024-09-30 09:38
 */
public class DatasourceEnvironmentOutboundParser implements EnvironmentOutboundParser {

	/**
	 * Message template for the datasource information. This template includes a
	 * placeholder for the JDBC URL.
	 */
	private static final String TEMPLATE = "Outbound Datasource: \t{}";

	/**
	 * Converts the datasource configuration from the environment to a string
	 * representation. This method extracts the JDBC URL from the environment and formats
	 * it into a human-readable string. If the URL contains placeholders, they are
	 * removed.
	 * @param env the Spring environment containing the configuration properties
	 * @return a formatted string with the datasource URL, or an empty string if the
	 * datasource URL is not configured
	 */
	@Override
	public String toString(Environment env) {
		if (!env.containsProperty(DatasourceEnvironment.URL)) {
			return StringConstants.EMPTY;
		}

		String url = env.getProperty(DatasourceEnvironment.URL);
		if (StringUtil.isNotBlank(url) && url.contains(StringConstants.PLACEHOLDER)) {
			url = url.substring(0, url.indexOf(StringConstants.PLACEHOLDER));
		}
		return MessageFormatUtils.format(TEMPLATE, url);
	}

}
