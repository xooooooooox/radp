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

package space.x9x.radp.spring.test.container.cases.redis;

import java.net.HttpURLConnection;
import java.net.URL;

import com.redis.testcontainers.RedisContainer;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 在微服务架构中, 多个服务需要通过网络通信. TestContainers 支持创建自定义网络, 让容器之间可以相互访问.
 * <p>
 * 假设你有一个简单的 API 服务依赖 Redis, 我们可以使用 TestContainers 模拟这种依赖关系
 *
 * @author IO x9x
 * @since 2025-05-24 22:30
 */
@Testcontainers
class RedisContainerTest {

    // 创建一个自定义网络
    private static final Network NETWORK = Network.newNetwork();

    @Container
    private final GenericContainer<?> redis = new RedisContainer(DockerImageName.parse("redis:6.2.6"))
            .withNetwork(NETWORK) // 将容器加入网络 NETWORK
            .withNetworkAliases("redis") // 为 Redis 设置网络别名, 别的容器可以通过这个别名访问它
            .withExposedPorts(6379);

    @SuppressWarnings("resource")
    @Container
    private final GenericContainer<?> api = new GenericContainer<>(
            new ImageFromDockerfile("my-api") // 从指定Dockerfile构建镜像
                    .withFileFromClasspath("Dockerfile", "docker/case2/Dockerfile") // 将 resource 文件添加到 Docker 构建上下文
                    .withFileFromClasspath("nginx.conf", "docker/case2/nginx.conf")
                    .withFileFromClasspath("api.sh", "docker/case2/api.sh")
                    .withFileFromClasspath("health.sh", "docker/case2/health.sh"))
            .withNetwork(NETWORK) // 将容器加入网络 NETWORK
            .withEnv("REDIS_HOST", "redis") // 使用别名连接 Redis
            .withEnv("REDIS_PORT", "6379")
            .withExposedPorts(8080)
            .waitingFor(Wait.forHttp("/health"));   // 等待 API 就绪

    @Test
    void testApiWithRedis() throws Exception {
        String url = String.format("http://%s:%s", api.getHost(), api.getMappedPort(8080));
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("GET");
        assertEquals(200, connection.getResponseCode());
    }

}
