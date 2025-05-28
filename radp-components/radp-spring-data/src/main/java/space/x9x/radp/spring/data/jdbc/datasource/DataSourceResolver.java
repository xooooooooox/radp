package space.x9x.radp.spring.data.jdbc.datasource;

import javax.sql.DataSource;

import space.x9x.radp.extension.SPI;

/**
 * 数据源解析器
 *
 * @author IO x9x
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
