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

import space.x9x.radp.extension.SPI;

import javax.sql.DataSource;

/**
 * 数据源解析器
 *
 * @author x9x
 * @since 2024-09-30 13:57
 */
@SPI
public interface DataSourceResolver {

    /**
     * 解析数据源
     *
     * @param originalDataSource 原始数据源
     * @return 解析后的数据源
     */
    DataSource resolveDataSource(DataSource originalDataSource);
}
