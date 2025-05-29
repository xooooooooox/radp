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

package space.x9x.radp.dubbo.spring.cloud.autoconfigure;

import java.util.Arrays;

import org.springframework.boot.autoconfigure.AutoConfigurationImportFilter;
import org.springframework.boot.autoconfigure.AutoConfigurationMetadata;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

import space.x9x.radp.spring.boot.bootstrap.constants.Conditions;

/**
 * Dubbo autoconfiguration import filter. This class filters Dubbo-related
 * auto-configurations based on the "dubbo.enabled" property, allowing conditional
 * activation of Dubbo functionality. When Dubbo is disabled, this filter prevents
 * specific Dubbo actuator auto-configurations from being imported.
 *
 * @author IO x9x
 * @since 2024-10-01 23:41
 */
public class DubboAutoConfigurationImportFilter implements AutoConfigurationImportFilter, EnvironmentAware {

	/**
	 * Configuration property key that determines if Dubbo is enabled. When set to false,
	 * specific Dubbo auto-configurations will be filtered out.
	 */
	private static final String MATCH_KEY = "dubbo.enabled";

	/**
	 * Array of Dubbo actuator auto-configuration class names that should be ignored when
	 * Dubbo is disabled via the dubbo.enabled property.
	 */
	private static final String[] IGNORE_CLASSES = {
			"org.apache.dubbo.spring.boot.actuate.autoconfigure.DubboEndpointAnnotationAutoConfiguration",
			"org.apache.dubbo.spring.boot.actuate.autoconfigure.DubboMetricsAutoConfiguration",
			"org.apache.dubbo.spring.boot.actuate.autoconfigure.DubboEndpointAutoConfiguration",
			"org.apache.dubbo.spring.boot.actuate.autoconfigure.DubboHealthIndicatorAutoConfiguration",
			"org.apache.dubbo.spring.boot.actuate.autoconfigure.DubboEndpointMetadataAutoConfiguration", };

	/**
	 * Spring Environment used to retrieve configuration properties.
	 */
	private Environment environment;

	/**
	 * Determines which auto-configuration classes should be imported based on the
	 * dubbo.enabled property. When Dubbo is disabled, this method filters out specific
	 * Dubbo actuator auto-configurations.
	 * @param autoConfigurationClasses the auto-configuration class names to filter
	 * @param autoConfigurationMetadata metadata about the auto-configuration classes
	 * @return an array of boolean values indicating which classes should be imported
	 */
	@Override
	public boolean[] match(String[] autoConfigurationClasses, AutoConfigurationMetadata autoConfigurationMetadata) {
		boolean disabled = !Boolean.parseBoolean(this.environment.getProperty(MATCH_KEY, Conditions.TRUE));
		boolean[] match = new boolean[autoConfigurationClasses.length];
		for (int i = 0; i < autoConfigurationClasses.length; i++) {
			int index = i;
			match[i] = !disabled
					|| Arrays.stream(IGNORE_CLASSES).noneMatch(e -> e.equals(autoConfigurationClasses[index]));
		}
		return match;
	}

	/**
	 * Sets the Spring Environment to be used for property resolution. This method is
	 * called by the Spring framework as part of the EnvironmentAware interface.
	 * @param environment the Spring Environment
	 */
	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

}
