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

package space.x9x.radp.spring.boot.bootstrap.constants;

import lombok.experimental.UtilityClass;

/**
 * Constants for Spring Boot auto-configuration conditions. This utility class defines
 * common string constants used in conditional configurations for enabling/disabling
 * features and controlling bean registration behavior in Spring Boot applications.
 *
 * @author IO x9x
 * @since 2024-09-30 09:15
 */
@UtilityClass
public class Conditions {

	/**
	 * Constant representing the 'enabled' property name. Used in conditional
	 * configurations to check if a feature is enabled.
	 */
	public static final String ENABLED = "enabled";

	/**
	 * Constant representing the 'true' string value. Used in conditional configurations
	 * for boolean property comparisons.
	 */
	public static final String TRUE = "true";

	/**
	 * Constant representing the 'false' string value. Used in conditional configurations
	 * for boolean property comparisons.
	 */
	public static final String FALSE = "false";

	/**
	 * Constant representing the 'primary' property name. Used in bean definitions to mark
	 * a bean as primary when multiple candidates exist.
	 */
	public static final String PRIMARY = "primary";

}
