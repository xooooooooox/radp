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

package space.x9x.radp.druid.spring.boot.autoconfigure;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot3.autoconfigure.DruidDataSourceAutoConfigure;
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
 * Autoconfiguration for Druid data source metrics monitoring. This class provides
 * configuration for monitoring Druid connection pools and exposing their metrics through
 * Spring Boot's metrics system. It is activated when the 'spring.datasource.druid'
 * property is set and the Druid data source class is available on the classpath.
 *
 * @author RADP x9x
 * @since 2024-10-01 01:13
 */
@ConditionalOnProperty(RadpDruidDataSourceMetricsAutoconfiguration.SPRING_DATASOURCE_DRUID)
@ConditionalOnClass(DruidDataSource.class)
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@AutoConfiguration(after = DruidDataSourceAutoConfigure.class)
@Slf4j
public class RadpDruidDataSourceMetricsAutoconfiguration {

	/**
	 * Property name for Druid datasource configuration. This constant defines the Spring
	 * Boot property prefix used for all Druid-specific configuration properties. It is
	 * used in conditional annotations to determine whether Druid auto-configuration
	 * should be activated.
	 */
	public static final String SPRING_DATASOURCE_DRUID = "spring.datasource.druid";

	/**
	 * Log message used when the DataSourcePoolMetadataProvider is autowired. This message
	 * is logged at debug level when the provider is initialized.
	 */
	private static final String AUTOWIRED_DATA_SOURCE_POOL_METADATA_PROVIDER = "Autowired DataSourcePoolMetadataProvider";

	/**
	 * Creates a DataSourcePoolMetadataProvider for Druid data sources. This bean provides
	 * metadata about Druid connection pools to Spring Boot's metrics system, allowing for
	 * monitoring of connection pool statistics. It is only created if a MeterRegistry and
	 * DruidDataSource are available and no other DataSourcePoolMetadataProvider bean
	 * exists.
	 * @return a DataSourcePoolMetadataProvider that creates DruidDatasourcePoolMetadata
	 * for Druid data sources
	 */
	@ConditionalOnMissingBean
	@ConditionalOnBean({ MeterRegistry.class, DruidDataSource.class })
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
