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
 * Constants for Spring profiles used in the RADP framework. This utility class defines
 * standard profile names for different environments to ensure consistent profile naming
 * across the application.
 *
 * @author IO x9x
 * @since 2024-09-28 21:39
 */
@UtilityClass
public class SpringProfiles {

	/**
	 * Local environment profile. Used for development on local machines.
	 */
	public static final String SPRING_PROFILE_LOCALHOST = "local";

	/**
	 * Development environment profile. Used for development servers and environments.
	 */
	public static final String SPRING_PROFILE_DEVELOPMENT = "dev";

	/**
	 * Production environment profile. Used for live production environments.
	 */
	public static final String SPRING_PROFILE_PRODUCTION = "prod";

	/**
	 * Staging environment profile. Used for pre-production testing and verification.
	 */
	public static final String SPRING_PROFILE_STAGING = "staging";

	/**
	 * Test environment profile. Used for automated testing environments.
	 */
	public static final String SPRING_PROFILE_TEST = "test";

	/**
	 * Demo environment profile. Used for demonstration and showcase environments.
	 */
	public static final String SPRING_PROFILE_DEMO = "demo";

}
