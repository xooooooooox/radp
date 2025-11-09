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

package space.x9x.radp.spring.boot.beans.autoconfigure;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Role;

import space.x9x.radp.spring.framework.beans.ApplicationContextHelper;

/**
 * Autoconfiguration for the ApplicationContextHelper. This configuration class
 * automatically registers an ApplicationContextHelper bean in the Spring application
 * context if one is not already defined. The ApplicationContextHelper provides a way to
 * access the Spring ApplicationContext from non-Spring managed classes.
 *
 * @author x9x
 * @since 2024-09-27 21:14
 */
@Slf4j
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@AutoConfiguration
public class ApplicationContextAutoConfiguration {

	/**
	 * Log message for when the ApplicationContextHelper is autowired. This constant
	 * defines the message logged when the ApplicationContextHelper bean is created.
	 */
	private static final String AUTOWIRED_APPLICATION_CONTEXT_HELPER = "Autowired ApplicationContextHelper";

	/**
	 * Creates and configures an ApplicationContextHelper bean. This helper provides
	 * access to the Spring ApplicationContext from non-Spring managed classes. The bean
	 * is only created if no other ApplicationContextHelper bean is already defined.
	 * @return a configured ApplicationContextHelper instance
	 */
	@ConditionalOnMissingBean(ApplicationContextHelper.class)
	@Bean
	public ApplicationContextHelper applicationContextHelper() {
		log.debug(AUTOWIRED_APPLICATION_CONTEXT_HELPER);
		return new ApplicationContextHelper();
	}

}
