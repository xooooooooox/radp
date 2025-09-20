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

package space.x9x.radp.dynamic.datasource.spring.boot.jdbc;

import javax.sql.DataSource;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;

import space.x9x.radp.spring.data.jdbc.datasource.DataSourceResolver;

/**
 * 动态数据源解析器.
 *
 * @author IO x9x
 * @since 2025-09-19 22:49
 */
public class DynamicDatasourceResolver implements DataSourceResolver {

	/**
	 * 解析数据源.
	 * @param dataSource the original data source to be resolved
	 * @return 解析后的数据源
	 */
	@Override
	public DataSource resolveDataSource(DataSource dataSource) {
		if (DynamicRoutingDataSource.class.isAssignableFrom(dataSource.getClass())) {
			dataSource = ((DynamicRoutingDataSource) dataSource).determineDataSource();
		}
		return dataSource;
	}

}
