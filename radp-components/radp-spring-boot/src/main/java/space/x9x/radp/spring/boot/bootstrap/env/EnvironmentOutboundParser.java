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
 * Interface for parsing outbound information from Spring Boot environment. This interface
 * defines methods for extracting and formatting outbound connection information from the
 * Spring environment, such as database connections, messaging systems, and external
 * service endpoints. Implementations can provide specific parsers for different types of
 * outbound connections.
 *
 * @author x9x
 * @since 2024-09-28 21:47
 */
@SPI
public interface EnvironmentOutboundParser {

	/**
	 * Converts the Spring Environment to a string representation. This method is used to
	 * transform environment properties into a string format suitable for outbound
	 * communication or logging.
	 * @param env the Spring Environment to convert
	 * @return a string representation of the environment
	 */
	String toString(Environment env);

}
