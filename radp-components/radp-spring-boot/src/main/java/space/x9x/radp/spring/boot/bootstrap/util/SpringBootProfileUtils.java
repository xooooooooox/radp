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

package space.x9x.radp.spring.boot.bootstrap.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.SpringApplication;

import lombok.experimental.UtilityClass;

import space.x9x.radp.spring.framework.bootstrap.constant.SpringProfiles;
import space.x9x.radp.spring.framework.bootstrap.constant.SpringProperties;

/**
 * Utility class for managing Spring Boot application profiles. This class provides
 * methods to configure default profiles for Spring Boot applications, making it easier to
 * set up development environments consistently.
 *
 * @author IO x9x
 * @since 2024-09-28 21:37
 */
@UtilityClass
public class SpringBootProfileUtils {

	/**
	 * Adds a default profile to a Spring Boot application. This method sets the default
	 * Spring profile to the development profile if no profile is explicitly specified
	 * when the application starts.
	 * @param application the Spring Boot application to configure
	 */
	public static void addDefaultProfile(SpringApplication application) {
		Map<String, Object> defaultProperties = new HashMap<>();
		defaultProperties.put(SpringProperties.SPRING_PROFILE_DEFAULT, SpringProfiles.SPRING_PROFILE_DEVELOPMENT);
		application.setDefaultProperties(defaultProperties);
	}

}
