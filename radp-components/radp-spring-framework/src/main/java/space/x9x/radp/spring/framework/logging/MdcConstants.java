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

package space.x9x.radp.spring.framework.logging;

import lombok.experimental.UtilityClass;

/**
 * Constants for MDC (Mapped Diagnostic Context) keys used in logging. This utility class
 * defines standard key names for storing contextual information in the logging MDC, which
 * allows for enriched log entries with application, request, and method execution
 * details.
 *
 * <p>
 * MDC is a feature provided by logging frameworks like SLF4J and Log4j that allows
 * storing diagnostic context data by thread. This data can then be included in log
 * messages to provide additional context for troubleshooting and monitoring.
 *
 * <p>
 * The constants defined in this class are used by various logging components in the
 * framework, including:
 * <ul>
 * <li>Bootstrap logging for application startup information</li>
 * <li>Access logging for HTTP request details</li>
 * <li>Method execution logging for performance monitoring</li>
 * </ul>
 *
 * @author x9x
 * @since 2024-09-28 21:08
 * @see org.slf4j.MDC
 * @see space.x9x.radp.spring.framework.logging.bootstrap.filter.BootstrapLogHttpFilter
 * @see space.x9x.radp.spring.framework.logging.access.util.AccessLogHelper
 */
@UtilityClass
public class MdcConstants {

	/**
	 * MDC key for the application name. Used to identify which application generated the
	 * log entry.
	 */
	public static final String APP = "app";

	/**
	 * MDC key for the application profile. Used to identify which environment profile
	 * (e.g., dev, test, prod) is active.
	 */
	public static final String PROFILE = "profile";

	/**
	 * MDC key for the class name. Used to record the fully qualified name of the class
	 * that generated the log entry.
	 */
	public static final String CLASS_NAME = "className";

	/**
	 * MDC key for the method name. Used to record the name of the method that generated
	 * the log entry.
	 */
	public static final String METHOD_NAME = "methodName";

	/**
	 * MDC key for method arguments. Used to record the arguments passed to the method
	 * that generated the log entry.
	 */
	public static final String ARGUMENTS = "arguments";

	/**
	 * MDC key for method return value. Used to record the value returned by the method
	 * that generated the log entry.
	 */
	public static final String RETURN_VALUE = "returnValue";

	/**
	 * MDC key for method execution duration. Used to record the time taken to execute the
	 * method that generated the log entry.
	 */
	public static final String DURATION = "duration";

	/**
	 * MDC key for HTTP request URI. Used to record the URI of the HTTP request that
	 * generated the log entry.
	 */
	public static final String REQUEST_URI = "requestUri";

	/**
	 * MDC key for remote user. Used to record the username of the user who made the
	 * request that generated the log entry.
	 */
	public static final String REMOTE_USER = "remoteUser";

	/**
	 * MDC key for remote address. Used to record the IP address of the client that made
	 * the request that generated the log entry.
	 */
	public static final String REMOTE_ADDR = "remoteAddr";

	/**
	 * MDC key for local address. Used to record the local IP address that received the
	 * request that generated the log entry.
	 */
	public static final String LOCAL_ADDR = "localAddr";

}
