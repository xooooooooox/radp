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

package space.x9x.radp.spring.data.jdbc.datasource.spi;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariDataSource;

import space.x9x.radp.spring.data.jdbc.datasource.DataSourceUrlParser;

/**
 * Implementation of DataSourceUrlParser for HikariCP datasource.
 *
 * @author IO x9x
 * @since 2024-09-30 14:33
 */
public class HikariDataSourceUrlParser implements DataSourceUrlParser {

	/**
	 * The fully qualified class name of HikariDataSource.
	 */
	private static final String CLASS_NAME = "com.zaxxer.hikari.HikariDataSource";

	/**
	 * Gets the JDBC URL from a HikariCP datasource.
	 * @param dataSource the datasource to extract the URL from
	 * @return the JDBC URL of the datasource, or null if not applicable
	 */
	@Override
	public String getDatasourceUrl(DataSource dataSource) {
		if (CLASS_NAME.equalsIgnoreCase(dataSource.getClass().getName())) {
			return ((HikariDataSource) dataSource).getJdbcUrl();
		}
		return null;
	}

}
