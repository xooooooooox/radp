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

package space.x9x.radp.spring.test.container.cases.wait;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.AbstractWaitStrategy;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.containers.wait.strategy.WaitStrategy;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 自定义等待策略
 * <p>
 * 容器启动后可能需要时间初始化(如数据库创建表, API服务监听端口)
 * <p>
 * TestContainer 提供了等待策略, 确保容器就绪后再运行测试
 * <p>
 * 内置等待策略:
 * <ul>
 * <li>端口就绪: {@code Wait.forListeningPort}</li>
 * <li>HTTP就绪: {@code Wait.forHttp("/health")}</li>
 * <li>日志匹配: {@code Wait.forLogMessage(".*Started.*", 1)}</li>
 * </ul>
 *
 * @author IO x9x
 * @since 2025-05-24 23:03
 */
@Testcontainers
@Slf4j
class CustomWaitStrategyTest {

	/**
	 * 自定义等待策略实现 继承 AbstractWaitStrategy 并实现 waitUntilReady 方法
	 */
	static class SimpleCustomWaitStrategy extends AbstractWaitStrategy {

		private final int targetPort;

		public SimpleCustomWaitStrategy(int targetPort) {
			this.targetPort = targetPort;
		}

		@Override
		protected void waitUntilReady() {
			// 自定义检查逻辑, 例如通过 API 调用确认服务就绪
			log.info("等待容器在端口 {} 就绪...", targetPort);

			int attempts = 0;
			boolean ready = false;

			while (!ready && attempts < 30) {
				try {
					Thread.sleep(500);
					ready = isServiceReady();
					attempts++;
				}
				catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					throw new RuntimeException("等待容器就绪时被中断", e);
				}
			}

			if (!ready) {
				throw new RuntimeException("容器未能在预期时间内就绪");
			}

			log.info("容器已就绪");
		}

		private boolean isServiceReady() {
			// 这里可以实现具体的检查逻辑
			// 示例: 检查容器是否已经启动并监听端口
			return waitStrategyTarget.isRunning();
		}

	}

	/**
	 * 组合多个等待策略的自定义策略
	 */
	static class CompositeWaitStrategy extends AbstractWaitStrategy {

		private final WaitStrategy[] strategies;

		public CompositeWaitStrategy(WaitStrategy... strategies) {
			this.strategies = strategies;
		}

		@Override
		protected void waitUntilReady() {
			for (WaitStrategy strategy : strategies) {
				// 为每个策略设置相同的目标容器
				strategy.withStartupTimeout(this.startupTimeout);

				// 应用每个等待策略
				log.info("应用等待策略: {}", strategy.getClass().getSimpleName());
				strategy.waitUntilReady(this.waitStrategyTarget);
			}
		}

	}

	// 使用内置的HTTP等待策略的容器
	@SuppressWarnings("resource")
	@Container
	private final GenericContainer<?> nginxContainer = new GenericContainer<>("nginx:1.21.6").withExposedPorts(80)
		.waitingFor(Wait.forHttp("/").forStatusCode(200).withStartupTimeout(Duration.ofSeconds(30)));

	// 使用自定义简单等待策略的容器
	@SuppressWarnings("resource")
	@Container
	private final GenericContainer<?> customWaitContainer = new GenericContainer<>("nginx:1.21.6").withExposedPorts(80)
		.waitingFor(new SimpleCustomWaitStrategy(80));

	// 使用组合等待策略的容器
	@SuppressWarnings("resource")
	@Container
	private final GenericContainer<?> compositeWaitContainer = new GenericContainer<>("nginx:1.21.6")
		.withExposedPorts(80)
		.waitingFor(new CompositeWaitStrategy(Wait.forListeningPort(), Wait.forHttp("/").forStatusCode(200)));

	@Test
	void testBuiltInWaitStrategy() throws IOException {
		// 测试内置的HTTP等待策略
		String url = String.format("http://%s:%s", nginxContainer.getHost(), nginxContainer.getMappedPort(80));
		HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
		connection.setRequestMethod("GET");

		assertThat(connection.getResponseCode()).as("Nginx应该返回200状态码").isEqualTo(200);
	}

	@Test
	void testCustomWaitStrategy() throws IOException {
		// 测试自定义等待策略
		assertThat(customWaitContainer.isRunning()).as("容器应该处于运行状态").isTrue();

		String url = String.format("http://%s:%s", customWaitContainer.getHost(),
				customWaitContainer.getMappedPort(80));
		HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
		connection.setRequestMethod("GET");

		assertThat(connection.getResponseCode()).as("Nginx应该返回200状态码").isEqualTo(200);
	}

	@Test
	void testCompositeWaitStrategy() throws IOException {
		// 测试组合等待策略
		assertThat(compositeWaitContainer.isRunning()).as("容器应该处于运行状态").isTrue();

		String url = String.format("http://%s:%s", compositeWaitContainer.getHost(),
				compositeWaitContainer.getMappedPort(80));
		HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
		connection.setRequestMethod("GET");

		assertThat(connection.getResponseCode()).as("Nginx应该返回200状态码").isEqualTo(200);
	}

}
