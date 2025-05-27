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

package space.x9x.radp.spring.data.jdbc.datasource;

import java.util.Set;

import javax.sql.DataSource;

import space.x9x.radp.commons.lang.StringUtils;
import space.x9x.radp.extension.ExtensionLoader;

/**
 * Data Source URL Parser Loader utility class. This class provides methods for loading
 * and using DataSourceResolver and DataSourceUrlParser extensions to extract URLs from
 * DataSource objects. It implements a chain of responsibility pattern where each resolver
 * and parser is tried in sequence until a valid URL is found.
 *
 * @author IO x9x
 * @since 2024-09-30 13:50
 */
public final class DataSourceUrlParserLoader {

	/**
	 * Private constructor to prevent instantiation of this utility class.
	 */
	private DataSourceUrlParserLoader() {
	}

	/**
	 * Default URL to be returned when a data source URL cannot be determined. This
	 * constant provides a fallback value for cases where the actual database URL is
	 * unavailable.
	 */
	public static final String UNKNOWN_URL = "jdbc:database://host:port/unknown_db";

	/**
	 * Parses the URL from the provided DataSource by using available DataSourceResolver
	 * and DataSourceUrlParser extensions. This method first resolves the DataSource using
	 * all available DataSourceResolver extensions, then attempts to extract the URL using
	 * available DataSourceUrlParser extensions.
	 * @param dataSource the DataSource to parse the URL from
	 * @return the parsed URL string, or UNKNOWN_URL if no parser could extract a valid
	 * URL
	 */
	public static String parse(DataSource dataSource) {
		ExtensionLoader<DataSourceResolver> resolverExtensionLoader = ExtensionLoader
			.getExtensionLoader(DataSourceResolver.class);
		Set<String> resolverExtensions = resolverExtensionLoader.getSupportedExtensions();
		for (String extension : resolverExtensions) {
			DataSourceResolver resolver = resolverExtensionLoader.getExtension(extension);
			dataSource = resolver.resolveDataSource(dataSource);
		}

		ExtensionLoader<DataSourceUrlParser> extensionLoader = ExtensionLoader
			.getExtensionLoader(DataSourceUrlParser.class);
		Set<String> extensions = extensionLoader.getSupportedExtensions();
		for (String extension : extensions) {
			DataSourceUrlParser parser = extensionLoader.getExtension(extension);
			String url = parser.getDatasourceUrl(dataSource);
			if (StringUtils.isNoneEmpty(url)) {
				return url;
			}
		}
		return UNKNOWN_URL;
	}

}
