package space.x9x.radp.druid.spring.boot.autoconfigure;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import space.x9x.radp.druid.spring.boot.jdbc.DruidDatasourcePoolMetadata;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.jdbc.DataSourceUnwrapper;
import org.springframework.boot.jdbc.metadata.DataSourcePoolMetadataProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Role;

/**
 * Druid 数据源监控连接池
 *
 * @author x9x
 * @since 2024-10-01 01:13
 */
@ConditionalOnProperty(RadpDruidDataSourceMetricsAutoconfiguration.SPRING_DATASOURCE_DRUID)
@ConditionalOnClass(DruidDataSource.class)
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@AutoConfiguration(after = DruidDataSourceAutoConfigure.class)
@Slf4j
public class RadpDruidDataSourceMetricsAutoconfiguration {
    public static final String SPRING_DATASOURCE_DRUID = "spring.datasource.druid";

    private static final String AUTOWIRED_DATA_SOURCE_POOL_METADATA_PROVIDER = "Autowired DataSourcePoolMetadataProvider";

    @ConditionalOnMissingBean
    @ConditionalOnBean({MeterRegistry.class, DruidDataSource.class})
    @Bean
    public DataSourcePoolMetadataProvider druidDataSourcePoolMetadataProvider() {
        log.debug(AUTOWIRED_DATA_SOURCE_POOL_METADATA_PROVIDER);
        return dataSource -> {
            DruidDataSource dds = DataSourceUnwrapper.unwrap(dataSource, DruidDataSource.class);
            if (dds != null) {
                return new DruidDatasourcePoolMetadata(dds);
            }
            return null;
        };
    }
}
