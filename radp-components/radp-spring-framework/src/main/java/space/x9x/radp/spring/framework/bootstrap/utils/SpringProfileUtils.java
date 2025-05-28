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

package space.x9x.radp.spring.framework.bootstrap.utils;

import org.springframework.core.env.Environment;

import space.x9x.radp.commons.lang.ArrayUtils;

/**
 * Utility class for working with Spring profiles. This class provides helper methods for
 * accessing and manipulating Spring environment profiles in a consistent way.
 *
 * @author IO x9x
 * @since 2024-09-28 21:04
 */
public final class SpringProfileUtils {

	/**
	 * Private constructor to prevent instantiation of this utility class. This class is
	 * not meant to be instantiated.
	 */
	private SpringProfileUtils() {
		throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
	}

	/**
	 * Gets the active profiles from the Spring environment. If no active profiles are
	 * set, return the default profiles. If no default profiles are set, return an empty
	 * array.
	 * @param env the Spring environment
	 * @return an array of active profile names or default profiles if no active profiles
	 * are set
	 */
	public static String[] getActiveProfiles(Environment env) {
		String[] activeProfiles = env.getActiveProfiles();
		if (ArrayUtils.isEmpty(activeProfiles)) {
			String[] defaultProfiles = env.getDefaultProfiles();
			if (ArrayUtils.isEmpty(defaultProfiles)) {
				return new String[0];
			}
			return defaultProfiles;
		}
		return activeProfiles;
	}

}
