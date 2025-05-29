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

package space.x9x.radp.spring.framework.logging.access.config;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Configuration class for access logging aspects. This class defines various settings
 * that control the behavior of access logging, including what to log, how to log it, and
 * when to log it. It can be customized by providing a bean of this type in the
 * application context.
 *
 * @author IO x9x
 * @since 2024-09-30 09:45
 */
@EqualsAndHashCode
@ToString
@Setter
@Getter
public class AccessLogConfig {

	/**
	 * Flag indicating whether to store logging information in MDC (Mapped Diagnostic
	 * Context). When enabled, logging information is available to all logging statements.
	 * Default is true.
	 */
	private boolean enabledMdc = true;

	/**
	 * Pointcut expression that defines which methods should be logged. This is typically
	 * a Spring AOP expression that matches specific packages, classes, or methods.
	 */
	private String expression;

	/**
	 * Sampling rate for log output, between 0.0 and 1.0. This controls what percentage of
	 * method invocations are logged. Default is 1.0 (log all invocations).
	 */
	private double sampleRate = 1.0;

	/**
	 * Flag indicating whether to log method arguments. When enabled, the values of method
	 * parameters are included in the log. Default is true.
	 */
	private boolean logArguments = true;

	/**
	 * Flag indicating whether to log method return values. When enabled, the return value
	 * of the method is included in the log. Default is true.
	 */
	private boolean logReturnValue = true;

	/**
	 * Flag indicating whether to log method execution time. When enabled, the time taken
	 * to execute the method is included in the log. Default is true.
	 */
	private boolean logExecutionTime = true;

	/**
	 * Maximum length for logged values (arguments and return values). Values longer than
	 * this will be truncated to prevent excessive log sizes. Default is 500 characters.
	 */
	private int maxLength = 500;

	/**
	 * Threshold in milliseconds for identifying slow method executions. Methods that take
	 * longer than this threshold are logged with a warning level. Default is 1000
	 * milliseconds (1 second).
	 */
	private long slowThreshold = 1000;

}
