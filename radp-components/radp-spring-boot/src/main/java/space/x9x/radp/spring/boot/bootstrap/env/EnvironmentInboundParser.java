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

package space.x9x.radp.spring.boot.bootstrap.env;

import org.springframework.core.env.Environment;

import space.x9x.radp.extension.SPI;

/**
 * Interface for parsing inbound information from Spring Boot environment. This interface
 * defines methods for extracting and formatting inbound connection information from the
 * Spring environment, such as server ports, hostnames, and endpoint paths.
 * Implementations can provide specific parsers for different types of inbound
 * connections.
 *
 * @author IO x9x
 * @since 2024-09-28 21:46
 */
@SPI
public interface EnvironmentInboundParser {

	/**
	 * Converts environment information to a string representation. This method extracts
	 * relevant inbound connection information from the Spring environment and formats it
	 * as a human-readable string.
	 * @param env the Spring environment containing configuration properties
	 * @return a formatted string with inbound connection information
	 */
	String toString(Environment env);

}
