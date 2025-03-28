package space.x9x.radp.spring.data.jdbc.datasource;

import space.x9x.radp.extension.SPI;

import javax.sql.DataSource;

/**
 * 数据源地址解析器
 *
 * @author x9x
 * @since 2024-09-30 13:50
 */
@SPI
public interface DataSourceUrlParser {

    /**
     * 获取数据源地址
     *
     * @param dataSource 数据源
     * @return 数据源地址
     */
    String getDatasourceUrl(DataSource dataSource);
}
