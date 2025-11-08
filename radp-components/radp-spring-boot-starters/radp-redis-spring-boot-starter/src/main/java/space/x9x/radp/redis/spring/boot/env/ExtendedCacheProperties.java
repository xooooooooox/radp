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

package space.x9x.radp.redis.spring.boot.env;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;

import space.x9x.radp.spring.framework.bootstrap.constant.Globals;

/**
 * Extended cache properties for Redis configuration. This class defines additional
 * configuration properties for Redis caching beyond what Spring Boot provides by default.
 * It includes settings for Redis scan operations and other cache-related configurations.
 *
 * @author x9x
 * @since 2024-10-21 11:45
 */
@Data
@ConfigurationProperties(prefix = Globals.RADP_CONFIGURATION_PROPERTIES_PREFIX + "cache")
public class ExtendedCacheProperties {

	/**
	 * Default batch size for Redis scan operations. This constant defines the default
	 * number of elements returned in a single Redis SCAN command execution.
	 */
	private static final Integer REDIS_SCAN_BATCH_SIZE_DEFAULT = 30;

	/**
	 * Batch size for Redis scan operations. This property controls how many elements are
	 * returned in a single Redis SCAN command execution.
	 */
	private Integer redisScanBatchSize = REDIS_SCAN_BATCH_SIZE_DEFAULT;

}
