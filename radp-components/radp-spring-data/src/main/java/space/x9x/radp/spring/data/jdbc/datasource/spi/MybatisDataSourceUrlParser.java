package space.x9x.radp.spring.data.jdbc.datasource.spi;

import space.x9x.radp.spring.data.jdbc.datasource.DataSourceUrlParser;
import space.x9x.radp.spring.data.jdbc.datasource.DataSourceUrlParserException;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.datasource.unpooled.UnpooledDataSource;

import javax.sql.DataSource;
import java.lang.reflect.Field;

/**
 * @author x9x
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
