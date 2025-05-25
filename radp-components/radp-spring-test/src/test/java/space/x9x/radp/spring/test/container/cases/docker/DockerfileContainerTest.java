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

package space.x9x.radp.spring.test.container.cases.docker;

import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 从 Dockerfile 创建容器
 *
 * @author x9x
 * @since 2025-05-24 17:04
 */
@Testcontainers
class DockerfileContainerTest {

    @Container
    private GenericContainer<?> nginx = new GenericContainer<>(
            new ImageFromDockerfile("custom-nginx") // 从指定Dockerfile构建镜像
                    .withFileFromClasspath("Dockerfile", "docker/case1/Dockerfile") // 将 resource 文件添加到 Docker 构建上下文
                    .withFileFromClasspath("custom.html", "docker/case1/custom.html")
                    .withFileFromClasspath("custom.conf", "docker/case1/custom.conf"))
            .withExposedPorts(80);

    @Test
    void testCustomNginx() throws Exception {
        String url = String.format("http://%s:%s", nginx.getHost(), nginx.getMappedPort(80));
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("GET");
        assertEquals(200, connection.getResponseCode());
    }
}
