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

package space.x9x.radp.spring.test.embedded;

import space.x9x.radp.extension.SPI;

/**
 * Interface defining the contract for embedded servers used in testing. This interface
 * provides methods for configuring, starting, stopping, and checking the status of
 * embedded servers such as Redis, Zookeeper, etc.
 *
 * @author x9x
 * @since 2024-09-23 15:03
 */
@SPI
public interface IEmbeddedServer {

	/**
	 * Sets the username for the embedded server.
	 * @param username the username for the server
	 * @return the current EmbeddedServer instance, supporting method chaining
	 */
	default IEmbeddedServer username(String username) {
		return this;
	}

	/**
	 * Sets the password for the embedded server.
	 * @param password the password for the server
	 * @return the current EmbeddedServer instance, supporting method chaining
	 */
	default IEmbeddedServer password(String password) {
		return this;
	}

	/**
	 * Sets the port for the embedded server.
	 * @param port the port number on which the server will listen
	 * @return the current EmbeddedServer instance, supporting method chaining
	 */
	default IEmbeddedServer port(int port) {
		return this;
	}

	/**
	 * Starts the embedded server.
	 */
	void startup();

	/**
	 * Stops the embedded server.
	 */
	void shutdown();

	/**
	 * Checks if the embedded server is currently running.
	 * @return true if the server is running, false otherwise
	 */
	boolean isRunning();

}
