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
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * TestContainer 容器复用示例
 * <p>
 * TestContainers 支持容器复用, 可以大幅提高测试速度, 减少资源消耗
 * <p>
 * 启用方式:
 * <ol>
 *     <li>全局启用: 在 用户主目录 或 classpath 下创建 {@code .testcontainers.properties} 文件, 添加 {@code testcontainers.reuse.enable=true}</li>
 *     <li>单个容器启用: 在容器创建时调用 {@code .withReuse(true)}</li>
 * </ol>
 * <p>
 * 注意事项:
 * <ol>
 *     <li>复用容器需要为容器设置唯一标识符, 通过 {@code .withLabel("reuse.UUID", "xxxx")} 设置</li>
 *     <li>如果不设置标识符, TestContainers 会自动生成, 但每次运行可能不同</li>
 *     <li>复用容器在测试结束后不会自动停止, 需要手动停止或等待 Docker 自动清理</li>
 *     <li>复用容器适合只读测试, 如果测试会修改容器状态, 可能导致后续测试失败</li>
 * </ol>
 *
 * @author x9x
 * @since 2025-05-24 23:30
 */
@Testcontainers
@Slf4j
class ContainerReuseTest {

    // 全局复用已通过 testcontainers.properties 启用
    // 为容器设置固定的复用标识符，确保每次运行使用相同的容器
    private static final String CONTAINER_REUSE_ID = "test-nginx-reuse-demo";

    // 使用全局复用配置的容器
    @Container
    private final GenericContainer<?> globalReuseContainer = new GenericContainer<>("nginx:alpine")
            .withLabel("reuse.UUID", CONTAINER_REUSE_ID)
            .withExposedPorts(80);

    // 显式启用复用的容器
    @Container
    private final GenericContainer<?> explicitReuseContainer = new GenericContainer<>("nginx:alpine")
            .withLabel("reuse.UUID", CONTAINER_REUSE_ID + "-explicit")
            .withReuse(true)
            .withExposedPorts(80);

    // 禁用复用的容器
    @Container
    private final GenericContainer<?> noReuseContainer = new GenericContainer<>("nginx:alpine")
            .withReuse(false)
            .withExposedPorts(80);

    @Test
    void testGlobalReuseContainer() {
        log.info("全局复用容器ID: {}", globalReuseContainer.getContainerId());
        log.info("全局复用容器映射端口: {}", globalReuseContainer.getMappedPort(80));

        // 验证容器正在运行
        assertContainerRunning(globalReuseContainer);
    }

    @Test
    void testExplicitReuseContainer() {
        log.info("显式复用容器ID: {}", explicitReuseContainer.getContainerId());
        log.info("显式复用容器映射端口: {}", explicitReuseContainer.getMappedPort(80));

        // 验证容器正在运行
        assertContainerRunning(explicitReuseContainer);
    }

    @Test
    void testNoReuseContainer() {
        log.info("非复用容器ID: {}", noReuseContainer.getContainerId());
        log.info("非复用容器映射端口: {}", noReuseContainer.getMappedPort(80));

        // 验证容器正在运行
        assertContainerRunning(noReuseContainer);
    }

    @Test
    void verifyContainerReuse() {
        // 再次记录容器ID，验证与第一个测试方法中的ID相同
        log.info("验证全局复用容器ID: {}", globalReuseContainer.getContainerId());
        log.info("验证显式复用容器ID: {}", explicitReuseContainer.getContainerId());
        log.info("验证非复用容器ID: {}", noReuseContainer.getContainerId());

        // 验证容器正在运行
        assertContainerRunning(globalReuseContainer);
        assertContainerRunning(explicitReuseContainer);
        assertContainerRunning(noReuseContainer);
    }

    /**
     * 验证容器是否正在运行
     */
    private void assertContainerRunning(GenericContainer<?> container) {
        // 简单验证容器是否正在运行
        assert container.isRunning() : "容器未运行";
    }
}
