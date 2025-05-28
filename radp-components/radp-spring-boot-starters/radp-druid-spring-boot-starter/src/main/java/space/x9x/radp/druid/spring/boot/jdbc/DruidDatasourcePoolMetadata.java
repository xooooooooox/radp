package space.x9x.radp.druid.spring.boot.jdbc;

import org.springframework.boot.jdbc.metadata.AbstractDataSourcePoolMetadata;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * @author IO x9x
 * @since 2024-10-01 01:10
 */
public class DruidDatasourcePoolMetadata extends AbstractDataSourcePoolMetadata<DruidDataSource> {


    /**
     * Create an instance with the data source to use.
     *
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
