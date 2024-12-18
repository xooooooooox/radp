package space.x9x.radp.spring.data.jdbc.datasource.spi;

import com.alibaba.druid.pool.DruidDataSource;
import space.x9x.radp.spring.data.jdbc.datasource.DataSourceUrlParser;

import javax.sql.DataSource;

/**
 * @author x9x
 * @since 2024-09-30 14:33
 */
public class DruidDataSourceUrlParser implements DataSourceUrlParser {

    private static final String CLASS_NAME = "com.alibaba.druid.pool.DruidDataSource";

    @Override
    public String getDatasourceUrl(DataSource dataSource) {
        if (CLASS_NAME.equalsIgnoreCase(dataSource.getClass().getName())) {
            return ((DruidDataSource) dataSource).getUrl();
        }
        return null;
    }
}
