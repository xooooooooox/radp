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

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Role;

import space.x9x.radp.mybatis.spring.boot.env.MybatisPlusExtensionProperties;
import space.x9x.radp.spring.data.mybatis.autofill.AutofillMetaObjectHandler;

/**
 * Autoconfiguration for MyBatis-Plus extensions. This class automatically configures
 * MyBatis-Plus extensions, particularly the automatic field filling functionality that
 * handles creation and modification timestamps in entity objects. It is activated when
 * the appropriate property is set and the required MyBatis-Plus classes are available.
 *
 * @author IO x9x
 * @since 2024-09-30 14:45
 */
@Slf4j
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@EnableConfigurationProperties(MybatisPlusExtensionProperties.class)
@ConditionalOnClass({ SqlSessionFactory.class, SqlSessionFactoryBean.class, MybatisConfiguration.class })
@ConditionalOnProperty(name = MybatisPlusExtensionProperties.AUTO_FILL_ENABLED, havingValue = "true")
@AutoConfiguration(after = MybatisPlusAutoConfiguration.class)
public class RadpMybatisPlusExtensionAutoConfiguration {

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
	public MetaObjectHandler metaObjectHandler(MybatisPlusExtensionProperties properties) {
		log.debug(AUTOWIRED_META_OBJECT_HANDLER);
		return new AutofillMetaObjectHandler(properties.getAutoFill().getCreatedDataFieldName(),
				properties.getAutoFill().getLastModifiedDateFieldName());
	}

}
