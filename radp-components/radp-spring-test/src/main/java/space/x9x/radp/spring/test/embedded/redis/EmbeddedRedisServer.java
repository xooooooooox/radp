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

package space.x9x.radp.spring.test.embedded.redis;

import java.io.IOException;

import lombok.extern.slf4j.Slf4j;
import redis.embedded.RedisServer;
import redis.embedded.core.RedisServerBuilder;

import space.x9x.radp.spring.framework.error.util.ExceptionUtils;
import space.x9x.radp.spring.test.embedded.IEmbeddedServer;

/**
 * Implementation of an embedded Redis server for testing purposes. This class provides a
 * lightweight Redis server that can be started and stopped programmatically within test
 * environments, eliminating the need for an external Redis installation. It supports
 * configuration of port, password, and other Redis settings, and manages the lifecycle of
 * the embedded server instance.
 *
 * @author RADP x9x
 * @since 2024-09-23 15:02
 */
@Slf4j
public class EmbeddedRedisServer implements IEmbeddedServer {

	/**
	 * Default bind address for the embedded Redis server. This constant defines the IP
	 * address to which the Redis server will bind. The value "0.0.0.0" means the server
	 * will accept connections on all available network interfaces.
	 */
	public static final String DEFAULT_BIND = "0.0.0.0";

	/**
	 * Default port for the embedded Redis server. This constant defines the port on which
	 * the Redis server will listen for connections. Value 6379 is the standard port used
	 * by Redis servers.
	 */
	public static final int DEFAULT_PORT = 6379;

	/**
	 * Default maximum memory setting for the embedded Redis server. This constant defines
	 * the maximum amount of memory that the Redis server can use. The value "max-memory
	 * 64mb" limits the Redis server to using at most 64MB of memory.
	 */
	public static final String DEFAULT_MAX_MEMORY = "maxmemory 64mb";

	/**
	 * The builder for creating the Redis server instance. This field holds the
	 * configuration for the Redis server that will be built and started.
	 */
	private final RedisServerBuilder redisServerBuilder;

	/**
	 * The Redis server instance. This field is initialized when the server is started and
	 * is used to control the server's lifecycle.
	 */
	private RedisServer redisServer;

	/**
	 * Flag indicating whether the Redis server is currently running. This field is set to
	 * true when the server is successfully started and to false when it is stopped.
	 */
	private boolean isRunning = false;

	/**
	 * The port on which the Redis server is configured to listen. This field is
	 * initialized with the default port value and can be changed using the port() method.
	 */
	private int port = DEFAULT_PORT;

	/**
	 * Constructs a new EmbeddedRedisServer with default settings. This constructor
	 * initializes the Redis server builder with the default bind address, port, and
	 * maximum memory size. The server is not started until the startup() method is
	 * called.
	 */
	public EmbeddedRedisServer() {
		this.redisServerBuilder = new RedisServerBuilder().bind(DEFAULT_BIND)
			.port(DEFAULT_PORT)
			.setting(DEFAULT_MAX_MEMORY)
			.setting("daemonize no");
	}

	/**
	 * Sets the password for the embedded Redis server. This method configures the Redis
	 * server to require authentication with the specified password.
	 * @param password the password to set for Redis authentication
	 * @return the current EmbeddedRedisServer instance, supporting method chaining
	 */
	@Override
	public IEmbeddedServer password(String password) {
		this.redisServerBuilder.setting("requirepass " + password);
		return this;
	}

	/**
	 * Sets the port for the embedded Redis server. This method configures the Redis
	 * server to listen on the specified port.
	 * @param port the port number on which the Redis server will listen
	 * @return the current EmbeddedRedisServer instance, supporting method chaining
	 */
	@Override
	public IEmbeddedServer port(int port) {
		this.port = port;
		this.redisServerBuilder.port(port);
		return this;
	}

	/**
	 * Starts the embedded Redis server. This method builds and starts the Redis server
	 * with the configured settings. If the server fails to start, an exception is thrown.
	 * @throws RuntimeException if the server fails to start due to an IOException
	 */
	@Override
	public void startup() {
		try {
			this.redisServer = this.redisServerBuilder.build();
			this.redisServer.start();
			this.isRunning = true;
			log.info("Embedded Redis server started on port {}", this.port);
		}
		catch (IOException ex) {
			this.isRunning = false;
			log.error("Failed to start embedded Redis server on port {}", this.port, ex);
			throw ExceptionUtils.serverException0("Failed to start embedded Redis server on port {}", this.port, ex);
		}
	}

	/**
	 * Stops the embedded Redis server if it is running. This method gracefully shuts down
	 * the Redis server. If the server is not running, this method does nothing. If the
	 * server fails to stop, an exception is thrown.
	 * @throws RuntimeException if the server fails to stop due to an IOException
	 */
	@Override
	public void shutdown() {
		if (!this.isRunning) {
			return;
		}
		try {
			this.redisServer.stop();
			this.isRunning = false;
			log.info("Embedded Redis server stopped");
		}
		catch (IOException ex) {
			log.error("Failed to stop embedded Redis server", ex);
			throw ExceptionUtils.serverException0("Failed to stop embedded Redis server", ex);
		}
	}

	/**
	 * Checks if the embedded Redis server is currently running.
	 * @return true if the server is running, false otherwise
	 */
	@Override
	public boolean isRunning() {
		return this.isRunning;
	}

}
