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

import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * TestContainer 容器复用示例
 * <p>
 * TestContainers 支持容器复用, 可以大幅提高测试速度, 减少资源消耗
 * <p>
 * 启用方式:
 * <ol>
 *     <li>全局启用: 在 classpath 下创建 testcontainers.properties 文件, 添加 testcontainers.reuse.enable=true</li>
 *     <li>单个容器启用: 在容器创建时调用 .withReuse(true)</li>
 * </ol>
 * <p>
 * 注意事项:
 * <ol>
 *     <li>复用容器需要为容器设置唯一标识符, 通过 .withLabel("reuse.UUID", "xxxx") 设置</li>
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

    /**
     * 全局复用配置
     * <p>
     * 通过 testcontainers.properties 文件中的 testcontainers.reuse.enable=true 启用
     * <p>
     * 为容器设置唯一标识符, 确保每次运行复用相同的容器
     */
    @Container
    private GenericContainer<?> globalReuseContainer = new GenericContainer<>("nginx:1.21.6")
            .withExposedPorts(80)
            .withLabel("reuse.UUID", "global-reuse-nginx")
            .withStartupTimeout(Duration.ofSeconds(30));

    /**
     * 单个容器复用配置
     * <p>
     * 通过 .withReuse(true) 显式启用复用
     * <p>
     * 即使全局未启用复用, 此容器也会被复用
     */
    @Container
    private GenericContainer<?> explicitReuseContainer = new GenericContainer<>("nginx:1.21.6")
            .withExposedPorts(80)
            .withLabel("reuse.UUID", "explicit-reuse-nginx")
            .withReuse(true)
            .withStartupTimeout(Duration.ofSeconds(30));

    @Test
    void testGlobalReuseContainer() throws Exception {
        log.info("全局复用容器 ID: {}", globalReuseContainer.getContainerId());
        log.info("全局复用容器映射端口: {}", globalReuseContainer.getMappedPort(80));

        String url = String.format("http://%s:%s", globalReuseContainer.getHost(), globalReuseContainer.getMappedPort(80));
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("GET");

        assertEquals(200, connection.getResponseCode(), "Nginx应该返回200状态码");
        assertTrue(globalReuseContainer.isRunning(), "容器应该处于运行状态");
    }

    @Test
    void testExplicitReuseContainer() throws Exception {
        log.info("显式复用容器 ID: {}", explicitReuseContainer.getContainerId());
        log.info("显式复用容器映射端口: {}", explicitReuseContainer.getMappedPort(80));

        String url = String.format("http://%s:%s", explicitReuseContainer.getHost(), explicitReuseContainer.getMappedPort(80));
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("GET");

        assertEquals(200, connection.getResponseCode(), "Nginx应该返回200状态码");
        assertTrue(explicitReuseContainer.isRunning(), "容器应该处于运行状态");
    }

    /**
     * 验证两个测试方法使用相同的容器
     * <p>
     * 如果容器被复用, 两个测试方法应该使用相同的容器ID和端口映射
     */
    @Test
    void verifyContainersAreReused() {
        log.info("全局复用容器 ID: {}", globalReuseContainer.getContainerId());
        log.info("全局复用容器映射端口: {}", globalReuseContainer.getMappedPort(80));
        
        log.info("显式复用容器 ID: {}", explicitReuseContainer.getContainerId());
        log.info("显式复用容器映射端口: {}", explicitReuseContainer.getMappedPort(80));
        
        assertTrue(globalReuseContainer.isRunning(), "全局复用容器应该处于运行状态");
        assertTrue(explicitReuseContainer.isRunning(), "显式复用容器应该处于运行状态");
    }
}