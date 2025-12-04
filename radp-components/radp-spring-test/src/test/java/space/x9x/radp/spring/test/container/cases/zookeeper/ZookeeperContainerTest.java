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

package space.x9x.radp.spring.test.container.cases.zookeeper;

import java.io.IOException;
import java.net.Socket;
import java.time.Duration;

import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author RADP x9x
 * @since 2025-05-25 15:48
 */
@Testcontainers
class ZookeeperContainerTest {

	@SuppressWarnings("resource")
	@Container
	private final GenericContainer<?> zookeeper = new GenericContainer<>("zookeeper:3.7.0").withExposedPorts(2181)
		.waitingFor(Wait.forListeningPort().withStartupTimeout(Duration.ofSeconds(60)));

	@Test
	void testZookeeperConnection() throws IOException {
		// Get the mapped port for RZookeeper
		String host = zookeeper.getHost();
		Integer port = zookeeper.getMappedPort(2181);

		// Verify container is running
		assertThat(zookeeper.isRunning()).as("Zookeeper container should be running").isTrue();

		// Test connection to Zookeeper port
		try (Socket socket = new Socket(host, port)) {
			assertThat(socket.isConnected()).as("Should be able to connect to Zookeeper").isTrue();
		}

		// Verify logs contain the expected startup message
		String logs = zookeeper.getLogs();
		assertThat(logs.contains("binding to port") || logs.contains("started"))
			.as("Zookeeper logs should indicate successful startup")
			.isTrue();
	}

}
