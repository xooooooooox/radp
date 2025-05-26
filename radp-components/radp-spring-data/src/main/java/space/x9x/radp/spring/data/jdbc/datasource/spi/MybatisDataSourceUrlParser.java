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

import space.x9x.radp.spring.data.jdbc.datasource.DataSourceUrlParser;
import space.x9x.radp.spring.data.jdbc.datasource.DataSourceUrlParserException;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.datasource.unpooled.UnpooledDataSource;

import javax.sql.DataSource;
import java.lang.reflect.Field;

/**
 * @author IO x9x
 * @since 2024-09-30 13:51
 */
@Slf4j
public class MybatisDataSourceUrlParser implements DataSourceUrlParser {
    private static final String DATA_SOURCE = "datasource";

    @Override
    public String getDatasourceUrl(DataSource dataSource) {
        if (dataSource instanceof PooledDataSource) {
            try {

                Field ds = dataSource.getClass().getDeclaredField(DATA_SOURCE);
                ds.setAccessible(true);
                UnpooledDataSource uds = (UnpooledDataSource) ds.get(dataSource);
                return uds.getUrl();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                throw new DataSourceUrlParserException(e.getMessage(), e);
            }
        }
        return null;
    }
}
