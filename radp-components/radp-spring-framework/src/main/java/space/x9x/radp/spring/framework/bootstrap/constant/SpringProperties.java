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

package space.x9x.radp.spring.framework.bootstrap.constant;

import lombok.experimental.UtilityClass;

/**
 * Constants for Spring property keys used in the RADP framework. This utility class
 * defines standard property names for Spring configuration to ensure consistent property
 * naming across the application.
 *
 * @author x9x
 * @since 2024-09-28 20:57
 */
@UtilityClass
public class SpringProperties {

	/**
	 * Property key for Spring Boot application name. This property is used to identify
	 * the application in various contexts.
	 */
	public static final String SPRING_APPLICATION_NAME = "spring.application.name";

	/**
	 * Pattern for resolving the application name. This pattern follows Spring's property
	 * resolution order for application name.
	 */
	public static final String NAME_PATTERN = "${spring.application.name:${vcap.application.name:${spring.config.name:application}}}";

	/**
	 * Pattern for resolving the server port. This pattern is used to access the
	 * configured server port.
	 */
	public static final String PORT_PATTERN = "${server.port}";

	/**
	 * Pattern for resolving the application instance index. This pattern follows Spring's
	 * property resolution order for instance indexing.
	 */
	public static final String INDEX_PATTERN = "${vcap.application.instance_index:${spring.application.index:${server.port:${PORT:null}}}}";

	/**
	 * Property key for Spring Boot default profile. This property defines the default
	 * profile to use when no active profile is specified.
	 */
	public static final String SPRING_PROFILE_DEFAULT = "spring.profiles.default";

	/**
	 * Property key for Spring Boot active profiles. This property specifies which
	 * profiles are active for the current application.
	 */
	public static final String SPRING_PROFILE_ACTIVE = "spring.profiles.active";

}
