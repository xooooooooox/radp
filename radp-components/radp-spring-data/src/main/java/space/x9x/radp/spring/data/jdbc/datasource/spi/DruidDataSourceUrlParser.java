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

import com.alibaba.druid.pool.DruidDataSource;

import space.x9x.radp.spring.data.jdbc.datasource.DataSourceUrlParser;

/**
 * Implementation of DataSourceUrlParser for Druid datasource. This class extracts the
 * JDBC URL from Druid datasource by checking the class type and casting to
 * DruidDataSource when applicable.
 *
 * @author RADP x9x
 * @since 2024-09-30 14:33
 */
public class DruidDataSourceUrlParser implements DataSourceUrlParser {

	/**
	 * The fully qualified class name of DruidDataSource.
	 */
	private static final String CLASS_NAME = "com.alibaba.druid.pool.DruidDataSource";

	@Override
	public String getDatasourceUrl(DataSource dataSource) {
		if (CLASS_NAME.equalsIgnoreCase(dataSource.getClass().getName())) {
			return ((DruidDataSource) dataSource).getUrl();
		}
		return null;
	}

}
