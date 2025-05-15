package space.x9x.radp.druid.spring.boot.autoconfigure;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
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
import space.x9x.radp.druid.spring.boot.jdbc.DruidDatasourcePoolMetadata;

/**
 * Druid 数据源监控连接池
 * <p>
 * Auto-configuration for Druid data source metrics monitoring.
 * This class provides configuration for monitoring Druid connection pools
 * and exposing their metrics through Spring Boot's metrics system.
 * It is activated when the 'spring.datasource.druid' property is set
 * and the Druid data source class is available on the classpath.
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

    /**
     * Creates a DataSourcePoolMetadataProvider for Druid data sources.
     * This bean provides metadata about Druid connection pools to Spring Boot's
     * metrics system, allowing for monitoring of connection pool statistics.
     * It is only created if a MeterRegistry and DruidDataSource are available
     * and no other DataSourcePoolMetadataProvider bean exists.
     *
     * @return a DataSourcePoolMetadataProvider that creates DruidDatasourcePoolMetadata
     * for Druid data sources
     */
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
