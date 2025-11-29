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

package space.x9x.radp.redis.spring.boot.autoconfigure;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.data.redis.autoconfigure.DataRedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Role;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

import space.x9x.radp.spring.data.redis.core.CustomRedisTemplate;
import space.x9x.radp.spring.data.redis.core.CustomRedisTemplateImpl;

/**
 * Auto-configuration for CustomRedisTemplate. This class automatically configures a
 * CustomRedisTemplate bean that provides enhanced Redis operations. It is activated when
 * a StringRedisTemplate bean is available and Redis operations classes are on the
 * classpath. The CustomRedisTemplate wraps the standard StringRedisTemplate to provide
 * additional functionality while maintaining compatibility with standard Redis
 * operations.
 *
 * @author x9x
 * @since 2024-10-19 20:57
 */
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@ConditionalOnBean(StringRedisTemplate.class)
@ConditionalOnClass(RedisOperations.class)
@AutoConfiguration(after = DataRedisAutoConfiguration.class)
@Slf4j
public class CustomRedisTemplateAutoConfiguration {

	/**
	 * Log message used when the CustomRedisTemplate is autowired. This message is logged
	 * at debug level when the template is initialized.
	 */
	private static final String AUTOWIRED_CUSTOM_REDIS_TEMPLATE = "Autowired customRedisTemplate";

	/**
	 * Creates a CustomRedisTemplate bean that provides enhanced Redis operations. This
	 * bean wraps a StringRedisTemplate to provide additional functionality while
	 * maintaining compatibility with standard Redis operations.
	 * @param stringRedisTemplate the StringRedisTemplate to wrap
	 * @return a CustomRedisTemplate implementation
	 */
	@ConditionalOnMissingBean
	@Bean
	public CustomRedisTemplate customRedisTemplate(StringRedisTemplate stringRedisTemplate) {
		log.debug(AUTOWIRED_CUSTOM_REDIS_TEMPLATE);
		return new CustomRedisTemplateImpl(stringRedisTemplate);
	}

}
