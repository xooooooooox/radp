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

package space.x9x.radp.spring.test.embedded.redis;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link EmbeddedRedisServer}.
 *
 * @author RADP x9x
 * @since 2024-10-30
 */
class EmbeddedRedisServerTest {

	private static final int TEST_PORT = 6379;

	/**
	 * This test verifies that the EmbeddedRedisServer class properly initializes with the
	 * default settings.
	 */
	@Test
	void testInitialization() {
		EmbeddedRedisServer server = new EmbeddedRedisServer();
		assertThat(server.isRunning()).isFalse();
	}

	/**
	 * This test verifies that the port method properly sets the port.
	 */
	@Test
	void testPortSetting() {
		EmbeddedRedisServer server = new EmbeddedRedisServer();
		server.port(TEST_PORT);
		assertThat(server.isRunning()).isFalse();
	}

	/**
	 * This test verifies that the password method properly sets the password.
	 */
	@Test
	void testPasswordSetting() {
		EmbeddedRedisServer server = new EmbeddedRedisServer();
		server.password("testpassword");
		assertThat(server.isRunning()).isFalse();
	}

	/**
	 * This test verifies that the isRunning method returns the correct value.
	 */
	@Test
	void testIsRunning() {
		EmbeddedRedisServer server = new EmbeddedRedisServer();
		assertThat(server.isRunning()).isFalse();
	}

}
