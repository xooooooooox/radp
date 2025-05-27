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

package space.x9x.radp.spring.test.container.cases.reuse;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * TestContainer 容器复用示例
 * <p>
 * TestContainers 支持容器复用, 可以大幅提高测试速度, 减少资源消耗
 * <p>
 * 启用方式:
 * <ol>
 * <li>全局启用: 在 用户主目录下创建 {@code .testcontainers.properties} 文件, 添加
 * {@code testcontainers.reuse.enable=true}</li>
 * <li>单个容器启用: 在容器创建时调用 {@code .withReuse(true)}</li>
 * </ol>
 * <p>
 * 注意事项:
 * <ol>
 * <li>复用容器需要为容器设置唯一标识符, 通过 {@code .withLabel("reuse.UUID", "xxxx")} 设置</li>
 * <li>如果不设置标识符, TestContainers 会自动生成, 但每次运行可能不同</li>
 * <li>复用容器在测试结束后不会自动停止, 需要手动停止或等待 Docker 自动清理</li>
 * <li>复用容器适合只读测试, 如果测试会修改容器状态, 可能导致后续测试失败</li>
 * </ol>
 *
 * @author IO x9x
 * @since 2025-05-24 23:30
 */
@Testcontainers
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ContainerReuseTest {

	// Static field to store the container ID from the first test
	private static String firstContainerId;

	/**
	 * Using Redis container for demonstration as it's lightweight and starts quickly
	 */
	@SuppressWarnings("resource")
	@Container
	private static final GenericContainer<?> REDIS_CONTAINER = new GenericContainer<>("redis:6.2.6-alpine")
		.withExposedPorts(6379)
		.withReuse(true); // Explicitly enable reuse for this container

	/**
	 * First test that uses the container and stores its ID
	 */
	@Test
	@Order(1)
	void testFirstContainerUse() {
		assertTrue(REDIS_CONTAINER.isRunning(), "Redis container should be running");

		// Store the container ID for comparison in the second test
		firstContainerId = REDIS_CONTAINER.getContainerId();
		int mappedPort = REDIS_CONTAINER.getMappedPort(6379);

		log.info("First test - Container ID: {}", firstContainerId);
		log.info("First test - Mapped port: {}", mappedPort);

		// Verify the container ID is not null
		assertNotNull(firstContainerId, "Container ID should not be null");

		// Simple assertion to verify the container is working
		assertEquals(6379, REDIS_CONTAINER.getExposedPorts().get(0), "Redis should expose port 6379");
	}

	/**
	 * The Second test that uses the same container and verifies it has the same ID
	 */
	@Test
	@Order(2)
	void testSecondContainerUse() {
		assertTrue(REDIS_CONTAINER.isRunning(), "Redis container should be running");

		String secondContainerId = REDIS_CONTAINER.getContainerId();
		int mappedPort = REDIS_CONTAINER.getMappedPort(6379);

		log.info("Second test - Container ID: {}", secondContainerId);
		log.info("Second test - Mapped port: {}", mappedPort);

		// Verify the container ID is not null
		assertNotNull(secondContainerId, "Container ID should not be null");

		// Verify that the container ID is the same as in the first test
		assertEquals(firstContainerId, secondContainerId,
				"Container ID should be the same across tests, indicating container reuse");

		// Simple assertion to verify the container is working
		assertEquals(6379, REDIS_CONTAINER.getExposedPorts().get(0), "Redis should expose port 6379");
	}

}
