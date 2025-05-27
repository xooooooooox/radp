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

package space.x9x.radp.mybatis.spring.boot.env;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

import space.x9x.radp.spring.framework.bootstrap.constant.Globals;

/**
 * Configuration properties for MyBatis plugins. This class defines settings for various
 * MyBatis plugins, particularly SQL logging functionality.
 *
 * @author IO x9x
 * @since 2024-09-30 13:24
 */
@Data
@ConfigurationProperties(prefix = Globals.RADP_CONFIGURATION_PROPERTIES_PREFIX + MybatisPluginProperties.PREFIX)
public class MybatisPluginProperties {

	/**
	 * The configuration prefix for MyBatis plugin properties. This constant is used as
	 * part of the property path in application configuration.
	 */
	public static final String PREFIX = "mybatis.plugin";

	/**
	 * Property path for enabling SQL logging. This constant represents the full property
	 * path to enable/disable SQL logging.
	 */
	public static final String SQL_LOG_ENABLED = PREFIX + ".sql-log.enabled";

	/**
	 * SQL logging configuration properties. This field contains settings for SQL
	 * execution logging, including enabling/disabling the feature and configuring
	 * thresholds for slow query detection.
	 */
	private final SqlLog sqlLog = new SqlLog();

	/**
	 * Configuration properties for SQL logging functionality. This inner class contains
	 * settings related to SQL execution logging, including enabling/disabling the feature
	 * and setting thresholds for slow query detection.
	 */
	@Data
	public static class SqlLog {

		/**
		 * Flag to enable or disable SQL logging. When set to true, SQL statements
		 * executed by MyBatis will be logged along with their execution times.
		 */
		private boolean enabled;

		/**
		 * Threshold for identifying slow SQL queries. SQL statements that take longer
		 * than this duration to execute will be logged at WARN level instead of INFO
		 * level. Default value is 1000 milliseconds (1 second).
		 */
		private Duration slownessThreshold = Duration.ofMillis(1000);

	}

}
