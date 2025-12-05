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

package space.x9x.radp.infrastructure.redis;

import space.x9x.radp.redis.spring.boot.constants.IRedisKeyProvider;

/**
 * Provider for Redis key prefixes used in the application. This enum implements the
 * IRedisKeyProvider interface to provide standardized Redis key management.
 *
 * @author RADP x9x
 * @since 2025-05-22 23:04
 */
public enum RedisKeyProvider implements IRedisKeyProvider {

	/**
	 * Singleton instance of the RedisKeyProvider.
	 */
	INSTANCE;

	/**
	 * The prefix used for Redis keys in this application.
	 */
	private static final String PREFIX = "radp";

	/**
	 * Returns the prefix to be used for Redis keys.
	 * @return the Redis key prefix as a String
	 */
	@Override
	public String getPrefix() {
		return PREFIX;
	}

}
