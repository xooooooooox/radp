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

import java.util.Collections;
import java.util.List;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Role;

import space.x9x.radp.mybatis.spring.boot.env.MybatisPlusExtensionProperties;
import space.x9x.radp.mybatis.spring.boot.interceptor.ColumnAliasRewriteInterceptor;
import space.x9x.radp.spring.data.mybatis.autofill.AutoFillStrategy;
import space.x9x.radp.spring.data.mybatis.autofill.BasePOAutoFillStrategy;
import space.x9x.radp.spring.data.mybatis.autofill.StrategyDelegatingMetaObjectHandler;
import space.x9x.radp.spring.framework.web.LoginUserResolver;

/**
 * Autoconfiguration for MyBatis-Plus extensions. This class automatically configures
 * MyBatis-Plus extensions, particularly the automatic field filling functionality that
 * handles creation and modification timestamps in entity objects. It is activated when
 * the appropriate property is set and the required MyBatis-Plus classes are available.
 *
 * @author x9x
 * @since 2024-09-30 14:45
 */
@Slf4j
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@EnableConfigurationProperties(MybatisPlusExtensionProperties.class)
@ConditionalOnClass({ SqlSessionFactory.class, SqlSessionFactoryBean.class, MybatisConfiguration.class })
@ConditionalOnProperty(name = MybatisPlusExtensionProperties.AUTO_FILL_ENABLED, havingValue = "true")
@AutoConfiguration(after = MybatisPlusAutoConfiguration.class)
public class MybatisPlusExtensionAutoConfiguration {

	/**
	 * Log a message for when the MetaObjectHandler is autowired. This constant defines
	 * the message logged when the handler is created.
	 */
	private static final String AUTOWIRED_META_OBJECT_HANDLER = "Autowired metaObjectHandler";

	/**
	 * Creates and configures a MetaObjectHandler for automatic field filling. This bean
	 * provides automatic filling of creation and modification timestamp fields in entity
	 * objects during insert and update operations.
	 * @param properties the configuration properties for MyBatis-Plus extensions
	 * @return a configured MetaObjectHandler that automatically fills timestamp fields
	 */
	@ConditionalOnMissingBean
	@Bean
	public MetaObjectHandler metaObjectHandler(MybatisPlusExtensionProperties properties,
			ObjectProvider<List<AutoFillStrategy>> strategiesProvider) {
		log.debug(AUTOWIRED_META_OBJECT_HANDLER);
		// Always prefer strategy-based delegation; users provide AutoFillStrategy beans
		List<AutoFillStrategy> strategies = strategiesProvider.getIfAvailable(Collections::emptyList);
		return new StrategyDelegatingMetaObjectHandler(strategies);
	}

	/**
	 * Register the default BasePO autofill strategy so users get sensible behavior out of
	 * the box. Users can define their own {@code AutoFillStrategy} beans to
	 * extend/replace this behavior; the delegating handler will pick the first strategy
	 * that supports the current entity.
	 * @return default BasePO strategy
	 */
	@ConditionalOnMissingBean(BasePOAutoFillStrategy.class)
	@Bean
	public BasePOAutoFillStrategy basePOAutoFillStrategy(
			@Autowired(required = false) LoginUserResolver loginUserResolver) {
		return new BasePOAutoFillStrategy(loginUserResolver);
	}

	/**
	 * Registers a lightweight SQL rewrite interceptor that aliases configured physical
	 * column names to logical defaults in SELECT lists and rewrites DML statements. The
	 * interceptor becomes a no-op when configured names equal the defaults
	 * (created_at/updated_at).
	 * @param properties the mybatis-plus extension properties
	 * @return interceptor bean recognized by MyBatis-Spring
	 */
	@ConditionalOnMissingBean(name = "columnAliasRewriteInterceptor")
	@ConditionalOnProperty(name = MybatisPlusExtensionProperties.SQL_REWRITE_ENABLED, havingValue = "true")
	@Bean
	public Interceptor columnAliasRewriteInterceptor(MybatisPlusExtensionProperties properties) {
		return new ColumnAliasRewriteInterceptor(properties.getSqlRewrite());
	}

}
