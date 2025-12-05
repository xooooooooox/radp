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

package space.x9x.radp.spring.test.container.cases.nacos;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author RADP x9x
 * @since 2025-05-25 15:54
 */
@Testcontainers
class NacosContainerTests {

	/**
	 * 8848：HTTP/REST 9848：gRPC（Spring Cloud Alibaba ≥2021.x 客户端会用到，可选）
	 */
	@SuppressWarnings("resource")
	@Container
	private final GenericContainer<?> nacos = new GenericContainer<>("nacos/nacos-server:v2.5.1")
		.withExposedPorts(8848, 9848)
		.withEnv("MODE", "standalone")
		// 直接用 HTTP 健康端点做等待策略，避免“端口已开但服务未完全就绪”的假阳性
		.waitingFor(Wait.forHttp("/nacos/v1/console/health/liveness")
			.forPort(8848)
			.forStatusCode(200)
			.withStartupTimeout(Duration.ofMinutes(3)));

	@Test
	void should_report_healthy_status() throws Exception {
		// 取得映射后的主机和端口
		String host = nacos.getHost();
		int port = nacos.getMappedPort(8848);

		String url = String.format("http://%s:%d/nacos/v1/console/health/liveness", host, port);

		// --- JDK HttpClient ---
		HttpClient client = HttpClient.newBuilder()
			// 整体请求超时（含连接及读取）
			.connectTimeout(Duration.ofSeconds(10))
			.build();

		HttpRequest request = HttpRequest.newBuilder()
			.uri(URI.create(url))
			.timeout(Duration.ofSeconds(10))
			.GET()
			.build();

		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

		// --- 断言 ---
		assertThat(response.statusCode()).isEqualTo(200);

		String body = response.body();
		assertThat(body).isNotNull();
		assertThat(body.contains("\"status\":\"UP\"") || body.contains("\"healthy\":true") || "OK".equals(body.trim()))
			.as("Unexpected health payload: %s", body)
			.isTrue();
	}

}
