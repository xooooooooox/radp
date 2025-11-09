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

package space.x9x.radp.spring.test.embedded.support;

import org.junit.jupiter.api.Test;

import space.x9x.radp.spring.test.embedded.IEmbeddedServer;
import space.x9x.radp.spring.test.embedded.redis.EmbeddedRedisServer;

import static org.assertj.core.api.Assertions.assertThat;
import static space.x9x.radp.spring.test.embedded.support.EmbeddedServerHelper.EmbeddedServerType;
import static space.x9x.radp.spring.test.embedded.support.EmbeddedServerHelper.embeddedServer;

/**
 * Tests for {@link EmbeddedServerHelper}.
 *
 * @author x9x
 * @since 2024-10-30
 */
class EmbeddedServerHelperTest {

	@Test
	void testRedisServer() {
		// Test creating a Redis server with the default port
		EmbeddedRedisServer server = (EmbeddedRedisServer) embeddedServer(EmbeddedServerType.REDIS);
		assertThat(server).isNotNull();

		// Test creating a Redis server with a custom port
		int customPort = 6380;
		EmbeddedRedisServer portServer = (EmbeddedRedisServer) embeddedServer(EmbeddedServerType.REDIS.getSpi(),
				customPort);
		assertThat(portServer).isNotNull();

		// Test creating a Redis server with a custom port and password
	}

	@Test
	void testEmbeddedServer() {
		// Test creating an embedded server using the extension mechanism
		// Note: This requires the "redis" SPI to be properly registered
		IEmbeddedServer server = embeddedServer("redis", 6380);
		assertThat(server).isNotNull();
	}

}
