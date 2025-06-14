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
 * Global constants used throughout the RADP Spring Framework. This utility class provides
 * centralized access to common constants that are used across different components of the
 * framework.
 *
 * @author IO x9x
 * @since 2024-10-12 15:47
 */
@UtilityClass
public class Globals {

	/**
	 * radp-xxx-spring-boot-starter {@code ConfigurationProperties} global prefix. This
	 * constant is used to define the base prefix for all RADP configuration properties.
	 */
	public static final String RADP_CONFIGURATION_PROPERTIES_PREFIX = "radp.";

}
