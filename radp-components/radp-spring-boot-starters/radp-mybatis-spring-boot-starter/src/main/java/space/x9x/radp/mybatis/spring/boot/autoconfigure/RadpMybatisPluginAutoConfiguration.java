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

package space.x9x.radp.mybatis.spring.boot.autoconfigure;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Role;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;

import space.x9x.radp.mybatis.spring.boot.env.MybatisPluginProperties;
import space.x9x.radp.spring.data.mybatis.plugin.MybatisSqlLogInterceptor;

/**
 * Autoconfiguration for MyBatis plugins. This class automatically configures MyBatis
 * plugins, particularly the SQL logging interceptor that provides SQL execution
 * monitoring.
 *
 * @author IO x9x
 * @since 2024-09-30 13:34
 */
@ConditionalOnMissingBean(SqlSessionFactory.class)
@ConditionalOnProperty(name = MybatisPluginProperties.SQL_LOG_ENABLED)
@EnableConfigurationProperties(MybatisPluginProperties.class)
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@AutoConfiguration(after = DataSourceAutoConfiguration.class)
@RequiredArgsConstructor
@Slf4j
public class RadpMybatisPluginAutoConfiguration {

	/**
	 * Log message used when the MybatisSqlLogInterceptor is autowired. This message is
	 * logged at debug level when the interceptor is initialized.
	 */
	private static final String AUTOWIRED_MYBATIS_SQL_LOG_INTERCEPTOR = "Autowired mybatisSqlLogInterceptor";

	/**
	 * The MybatisPluginProperties containing configuration for MyBatis plugins. This
	 * field holds the properties that control the behavior of SQL logging, including the
	 * slowness threshold for identifying slow queries.
	 */
	private final MybatisPluginProperties mybatisPluginProperties;

	/**
	 * Creates and configures a MybatisSqlLogInterceptor for SQL execution monitoring.
	 * This bean provides SQL logging functionality, including execution time tracking and
	 * slow query detection based on the configured threshold.
	 * @return a configured MybatisSqlLogInterceptor with the appropriate slowness
	 * threshold
	 */
	@Bean
	public MybatisSqlLogInterceptor mybatisSqlLogInterceptor() {
		log.debug(AUTOWIRED_MYBATIS_SQL_LOG_INTERCEPTOR);
		MybatisSqlLogInterceptor interceptor = new MybatisSqlLogInterceptor();
		interceptor.setSlownessThreshold(this.mybatisPluginProperties.getSqlLog().getSlownessThreshold());
		return interceptor;
	}

}
