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

package space.x9x.radp.smoke.tests.redis;

import space.x9x.radp.redis.spring.boot.constants.IRedisKeyProvider;

/**
 * Example implementation of RedisKeyProvider for test module.
 * <p>
 * This class demonstrates how scaffold projects can implement their own Redis key
 * management strategy while leveraging the centralized utilities.
 *
 * @author x9x
 * @since 2024-10-30
 */
public enum TestRedisKeys implements IRedisKeyProvider {

	INSTANCE;

	private static final String PREFIX = "radp:test";

	@Override
	public String getPrefix() {
		return PREFIX;
	}

}
