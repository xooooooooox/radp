package com.x9x.radp.spring.data.jdbc.datasource.spi;

import com.x9x.radp.spring.data.jdbc.datasource.DataSourceUrlParser;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

/**
 * @author x9x
 * @since 2024-09-30 14:33
 */
public class HikariDataSourceUrlParser implements DataSourceUrlParser {

    private static final String CLASS_NAME = "com.zaxxer.hikari.HikariDataSource";

    @Override
    public String getDatasourceUrl(DataSource dataSource) {
        if (CLASS_NAME.equalsIgnoreCase(dataSource.getClass().getName())) {
            return ((HikariDataSource) dataSource).getJdbcUrl();
        }
        return null;
    }
}
