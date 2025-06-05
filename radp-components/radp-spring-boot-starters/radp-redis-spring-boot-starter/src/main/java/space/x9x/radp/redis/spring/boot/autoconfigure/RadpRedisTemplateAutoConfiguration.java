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

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Role;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

import cn.hutool.core.util.ReflectUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.redisson.spring.starter.RedissonAutoConfiguration;

/**
 * Auto-configuration for Redis templates. This class automatically configures a
 * RedisTemplate bean with enhanced serialization capabilities. Key features include:
 * <ul>
 * <li>String serialization for keys and hash keys</li>
 * <li>JSON serialization for values and hash values</li>
 * <li>Enhanced Java 8 time support via JavaTimeModule</li>
 * <li>Consistent serialization across all Redis operations</li>
 * </ul>
 * This configuration runs before RedissonAutoConfiguration to ensure the template is
 * available for other Redis-dependent configurations.
 *
 * @author IO x9x
 * @since 2024-10-21 11:00
 */
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@AutoConfiguration(before = RedissonAutoConfiguration.class)
@Slf4j
public class RadpRedisTemplateAutoConfiguration {

	/**
	 * Log message used when the Redis template is autowired. This message is logged at
	 * debug level when the template is initialized.
	 */
	private static final String AUTOWIRED_RADP_REDIS_TEMPLATE = "Autowired redisTemplate";

	/**
	 * Creates a Redis template with customized serialization. This bean configures a
	 * RedisTemplate with the following settings: - String serialization for keys and hash
	 * keys - JSON serialization with Java 8 time support for values and hash values
	 * <p>
	 * The template provides a high-level abstraction for Redis operations, with properly
	 * configured serialization to ensure consistent data handling.
	 * @param factory the Redis connection factory to use
	 * @return a configured RedisTemplate instance
	 */
	@Bean
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
		log.debug(AUTOWIRED_RADP_REDIS_TEMPLATE);
		RedisTemplate<String, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(factory);
		template.setKeySerializer(RedisSerializer.string());
		template.setHashKeySerializer(RedisSerializer.string());
		template.setValueSerializer(buildRedisSerializer());
		template.setHashValueSerializer(buildRedisSerializer());
		return template;
	}

	/**
	 * Creates a Redis serializer with enhanced Java 8 time support. This method builds a
	 * JSON-based Redis serializer and enhances it with the JavaTimeModule to properly
	 * handle Java 8 date and time types (java.time.*). The serializer is used for both
	 * regular values and hash values in Redis operations.
	 * @return a configured Redis serializer with Java 8 time support
	 */
	public static RedisSerializer<?> buildRedisSerializer() {
		RedisSerializer<Object> json = RedisSerializer.json();
		ObjectMapper objectMapper = (ObjectMapper) ReflectUtil.getFieldValue(json, "mapper");
		objectMapper.registerModule(new JavaTimeModule());
		return json;
	}

}
