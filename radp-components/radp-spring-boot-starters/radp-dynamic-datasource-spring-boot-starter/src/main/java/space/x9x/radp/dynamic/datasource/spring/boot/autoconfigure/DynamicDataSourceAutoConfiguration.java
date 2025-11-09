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

package space.x9x.radp.dynamic.datasource.spring.boot.autoconfigure;

import java.util.List;

import javax.sql.DataSource;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.dynamic.datasource.aop.DynamicDataSourceAnnotationAdvisor;
import com.baomidou.dynamic.datasource.processor.DsProcessor;
import com.baomidou.dynamic.datasource.provider.DynamicDataSourceProvider;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceProperties;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDatasourceAopProperties;
import lombok.extern.slf4j.Slf4j;

import org.springframework.aop.Advisor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import space.x9x.radp.dynamic.datasource.spring.boot.custom.CustomDynamicDataSourceAnnotationInterceptor;
import space.x9x.radp.dynamic.datasource.spring.boot.custom.CustomDynamicRoutingDataSource;
import space.x9x.radp.spring.boot.bootstrap.constants.Conditions;

/**
 * 动态数据源自动配置.
 *
 * @author x9x
 * @since 2025-09-20 00:48
 */

@Slf4j
@ConditionalOnProperty(prefix = DynamicDataSourceProperties.PREFIX, name = Conditions.ENABLED,
		havingValue = Conditions.ENABLED, matchIfMissing = true)
@ConditionalOnClass(DynamicDataSourceProvider.class)
@Order(Ordered.HIGHEST_PRECEDENCE)
@AutoConfiguration
public class DynamicDataSourceAutoConfiguration {

	/**
	 * 日志前缀：自动装配动态路由数据源.
	 */
	private static final String AUTOWIRED_DYNAMIC_ROUTING_DATA_SOURCE = "Autowired DynamicDataSourceDataSource";

	/**
	 * 日志前缀：自动装配动态数据源注解 Advisor.
	 */
	private static final String AUTOWIRED_DYNAMIC_DATASOURCE_ANNOTATION_ADVISOR = "Autowired DynamicDataSourceAnnotationAdvisor";

	/**
	 * 动态数据源配置属性.
	 */
	private final DynamicDataSourceProperties properties;

	public DynamicDataSourceAutoConfiguration(DynamicDataSourceProperties properties) {
		this.properties = properties;
	}

	@Primary
	@Bean
	public DataSource dataSource(List<DynamicDataSourceProvider> providers) {
		log.info(AUTOWIRED_DYNAMIC_ROUTING_DATA_SOURCE);
		DynamicRoutingDataSource dataSource = new CustomDynamicRoutingDataSource(providers);
		dataSource.setPrimary(this.properties.getPrimary());
		dataSource.setStrict(this.properties.getStrict());
		dataSource.setStrategy(this.properties.getStrategy());
		dataSource.setP6spy(this.properties.getP6spy());
		dataSource.setSeata(this.properties.getSeata());
		return dataSource;
	}

	@ConditionalOnProperty(prefix = DynamicDataSourceProperties.PREFIX + ".aop", name = Conditions.ENABLED,
			havingValue = Conditions.TRUE, matchIfMissing = true)
	@Primary
	@Bean
	public Advisor dynamicDatasourceAnnotationAdvisor(DsProcessor dsProcessor) {
		log.debug(AUTOWIRED_DYNAMIC_DATASOURCE_ANNOTATION_ADVISOR);
		DynamicDatasourceAopProperties aopProperties = this.properties.getAop();
		CustomDynamicDataSourceAnnotationInterceptor interceptor = new CustomDynamicDataSourceAnnotationInterceptor(
				aopProperties.getAllowedPublicOnly(), dsProcessor);
		DynamicDataSourceAnnotationAdvisor advisor = new DynamicDataSourceAnnotationAdvisor(interceptor, DS.class);
		advisor.setOrder(aopProperties.getOrder());
		return advisor;
	}

}
