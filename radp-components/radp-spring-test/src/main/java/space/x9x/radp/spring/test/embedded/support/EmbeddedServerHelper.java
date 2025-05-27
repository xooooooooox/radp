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

package space.x9x.radp.spring.test.embedded.support;

import java.io.IOException;
import java.net.ServerSocket;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import space.x9x.radp.extension.ExtensionLoader;
import space.x9x.radp.spring.framework.error.util.ExceptionUtils;
import space.x9x.radp.spring.test.embedded.IEmbeddedServer;
import space.x9x.radp.spring.test.embedded.redis.EmbeddedRedisServer;

/**
 * Helper class for working with embedded servers. This class provides utility methods for
 * creating, configuring, and managing embedded servers for testing. It supports various
 * server types including Redis, Zookeeper, and Kafka.
 *
 * @author IO x9x
 * @since 2024-10-13 17:52
 */
@Slf4j
@UtilityClass
public class EmbeddedServerHelper {

	/**
	 * Creates and configures an embedded server instance using the extension mechanism.
	 * This method loads an EmbeddedServer implementation based on the provided SPI name,
	 * and configures it with the specified port.
	 * @param spi the SPI name of the embedded server implementation to load
	 * @param port the port on which the embedded server should listen
	 * @return a configured EmbeddedServer instance ready to be started
	 */
	public static IEmbeddedServer embeddedServer(String spi, int port) {
		try {
			return ExtensionLoader.getExtensionLoader(IEmbeddedServer.class).getExtension(spi).port(port);
		}
		catch (Exception ex) {
			log.error("Failed to create embedded server with SPI: {}", spi, ex);
			throw ExceptionUtils.serverException0("Failed to create embedded server with SPI: {}", spi, ex);
		}
	}

	/**
	 * Creates and configures an embedded server instance using the extension mechanism
	 * with an automatically assigned port. This method delegates to
	 * {@link #embeddedServer(String, int)} with a randomly selected available port.
	 * @param spi the SPI name of the embedded server implementation to load
	 * @return a configured EmbeddedServer instance ready to be started
	 */
	public static IEmbeddedServer embeddedServer(String spi) {
		return embeddedServer(spi, findAvailablePort(10000, 30000));
	}

	/**
	 * Creates and configures an embedded server instance based on a predefined server
	 * type. This method delegates to {@link #embeddedServer(String, int)} using the SPI
	 * name and port defined in the specified EmbeddedServerType.
	 * @param type the predefined server type from the EmbeddedServerType enum
	 * @return a configured EmbeddedServer instance ready to be started
	 */
	public static IEmbeddedServer embeddedServer(EmbeddedServerType type) {
		return embeddedServer(type.getSpi(), type.port);
	}

	/**
	 * Finds an available port within the specified range. This method iterates through
	 * the ports in the given range and attempts to create a ServerSocket on each port.
	 * The first port that can be successfully bound is returned.
	 * @param min the lower bound of the port range (inclusive)
	 * @param max the upper bound of the port range (inclusive)
	 * @return an available port within the specified range
	 * @throws RuntimeException if no available ports are found in the specified range
	 */
	public static int findAvailablePort(int min, int max) {
		for (int port = min; port <= max; port++) {
			try (ServerSocket socket = new ServerSocket(port)) {
				socket.setReuseAddress(true);
				return port;
			}
			catch (IOException ignored) {
				// Port is not available, try the next one
			}
		}
		throw ExceptionUtils.serverException0("No available ports found in range {}-{}", min, max);
	}

	/**
	 * Enumeration of supported embedded server types. Each server type is defined with
	 * its SPI name and default port. This enum is used with the
	 * {@link #embeddedServer(EmbeddedServerType)} method to create preconfigured embedded
	 * server instances.
	 */
	@RequiredArgsConstructor
	@Getter
	public enum EmbeddedServerType {

		/**
		 * Redis embedded server with the default configuration.
		 */
		REDIS("redis", EmbeddedRedisServer.DEFAULT_PORT),;

		/**
		 * The SPI (Service Provider Interface) name used to load the appropriate embedded
		 * server implementation. This identifier is used with the ExtensionLoader to
		 * instantiate the correct IEmbeddedServer implementation.
		 */
		private final String spi;

		/**
		 * The default port number on which the embedded server will listen for
		 * connections. For Redis, this is typically 6379, which is the standard Redis
		 * port.
		 */
		private final Integer port;

	}

}
