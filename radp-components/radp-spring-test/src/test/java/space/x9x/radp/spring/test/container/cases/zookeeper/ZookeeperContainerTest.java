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

import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.net.Socket;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
* @author x9x
* @since 2025-05-25 15:48
*/
@Testcontainers
public class ZookeeperContainerTest {

    @Container
    private final GenericContainer<?> zookeeper = new GenericContainer<>("zookeeper:3.7.0")
            .withExposedPorts(2181)
            .waitingFor(Wait.forListeningPort().withStartupTimeout(Duration.ofSeconds(60)));

    @Test
    void testZookeeperConnection() throws IOException {
        // Get the mapped port for Zookeeper
        String host = zookeeper.getHost();
        Integer port = zookeeper.getMappedPort(2181);

        // Verify container is running
        assertTrue(zookeeper.isRunning(), "Zookeeper container should be running");

        // Test connection to Zookeeper port
        try (Socket socket = new Socket(host, port)) {
            assertTrue(socket.isConnected(), "Should be able to connect to Zookeeper");
        }

        // Verify logs contain expected startup message
        String logs = zookeeper.getLogs();
        assertTrue(logs.contains("binding to port") || logs.contains("started"), 
                "Zookeeper logs should indicate successful startup");
    }
}
