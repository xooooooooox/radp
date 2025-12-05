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

package space.x9x.radp.spring.boot.web.env;

import lombok.experimental.UtilityClass;

/**
 * Constants for web server environment properties. This utility class provides string
 * constants for common web server configuration properties used in Spring Boot
 * applications.
 *
 * @author RADP x9x
 * @since 2024-09-30 09:28
 */
@UtilityClass
public class WebServerEnvironment {

	/**
	 * Property name for the web server port configuration. This constant defines the
	 * Spring Boot property name used to configure the port on which the embedded web
	 * server listens for incoming requests.
	 */
	public static final String SERVER_PORT = "server.port";

	/**
	 * Property name for the servlet context path configuration. This constant defines the
	 * Spring Boot property name used to configure the context path of the web
	 * application, which is the prefix added to all URL paths.
	 */
	public static final String SERVER_SERVLET_CONTEXT_PATH = "server.servlet.context-path";

	/**
	 * Property name for the SSL key store configuration. This constant defines the Spring
	 * Boot property name used to configure the path to the key store file containing the
	 * SSL certificate.
	 */
	public static final String SERVER_SSL_KEY_STORE = "server.ssl.key-store";

}
