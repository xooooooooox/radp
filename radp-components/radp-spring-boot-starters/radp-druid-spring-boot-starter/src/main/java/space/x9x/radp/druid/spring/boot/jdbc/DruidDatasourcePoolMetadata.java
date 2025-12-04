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

package space.x9x.radp.druid.spring.boot.jdbc;

import com.alibaba.druid.pool.DruidDataSource;

import org.springframework.boot.jdbc.metadata.AbstractDataSourcePoolMetadata;

/**
 * Metadata provider for Druid datasource pools. This class implements the
 * DataSourcePoolMetadata interface for Alibaba Druid connection pools, providing access
 * to pool statistics and configuration such as active connections, maximum and minimum
 * pool size, validation query, and default auto-commit settings.
 *
 * @author RADP x9x
 * @since 2024-10-01 01:10
 */
public class DruidDatasourcePoolMetadata extends AbstractDataSourcePoolMetadata<DruidDataSource> {

	/**
	 * Create an instance with the data source to use.
	 * @param dataSource the data source
	 */
	public DruidDatasourcePoolMetadata(DruidDataSource dataSource) {
		super(dataSource);
	}

	@Override
	public Integer getActive() {
		return getDataSource().getActiveCount();
	}

	@Override
	public Integer getMax() {
		return getDataSource().getMaxActive();
	}

	@Override
	public Integer getMin() {
		return getDataSource().getMinIdle();
	}

	@Override
	public String getValidationQuery() {
		return getDataSource().getValidationQuery();
	}

	@Override
	public Boolean getDefaultAutoCommit() {
		return getDataSource().isDefaultAutoCommit();
	}

}
