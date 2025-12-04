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

package space.x9x.radp.spring.framework.logging.access.util;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.MDC;

import space.x9x.radp.commons.lang.ObjUtils;
import space.x9x.radp.commons.lang.StringUtil;
import space.x9x.radp.commons.lang.Strings;
import space.x9x.radp.commons.net.IpConfigUtils;
import space.x9x.radp.spring.framework.json.support.JSONHelper;
import space.x9x.radp.spring.framework.logging.MdcConstants;
import space.x9x.radp.spring.framework.logging.access.model.AccessLog;
import space.x9x.radp.spring.framework.web.util.ServletUtils;

/**
 * Utility class for access logging operations. This class provides helper methods for
 * generating, formatting, and outputting access logs for both method invocations and HTTP
 * requests. It handles sampling, MDC integration, and log level selection based on
 * execution results.
 *
 * @author x9x
 * @since 2024-09-30 10:20
 */
@UtilityClass
@Slf4j
public class AccessLogHelper {

	/**
	 * Determines whether to log based on the sampling rate. This method implements a
	 * probabilistic sampling strategy where the likelihood of logging is determined by
	 * the sample rate.
	 * @param sampleRate the sampling rate between 0.0 and 1.0
	 * @return true if logging should occur, false otherwise
	 */
	public static boolean shouldLog(double sampleRate) {
		return sampleRate >= 1.0 || Math.random() < sampleRate;
	}

	/**
	 * Logs access information for a method invocation. This method captures and formats
	 * details about the method execution including class name, method name, arguments,
	 * return value, and execution time.
	 * @param invocation the method invocation being logged
	 * @param result the return value from the method
	 * @param throwable the exception thrown by the method, or null if successful
	 * @param duration the execution time in milliseconds
	 * @param enabledMdc whether to store logging information in MDC
	 * @param maxLength maximum length for logged values to prevent excessive log sizes
	 * @param slowThreshold threshold in milliseconds for identifying slow executions
	 */
	public static void log(MethodInvocation invocation, Object result, Throwable throwable, long duration,
			boolean enabledMdc, int maxLength, long slowThreshold) {
		AccessLog accessLog = new AccessLog();
		accessLog.setThrowable(throwable);
		accessLog.setDuration(duration);

		String className = Objects.requireNonNull(invocation.getThis()).getClass().getName();
		String methodName = invocation.getMethod().getName();
		String location = className + Strings.DOT + methodName;
		accessLog.setLocation(location);

		Object[] args = invocation.getArguments();
		StringBuilder argsBuilder = new StringBuilder();
		for (int i = 0; i < args.length; i++) {
			if (i > 0) {
				argsBuilder.append(", ");
			}
			argsBuilder.append(args[i] == null ? Strings.NULL : args[i].toString());
		}
		String arguments = argsBuilder.toString();
		if (arguments.length() > maxLength) {
			arguments = arguments.substring(0, maxLength);
		}
		accessLog.setArguments(arguments);

		String returnValue = ObjUtils.isEmpty(result) ? Strings.EMPTY : JSONHelper.json().toJSONString(result);
		if (returnValue.length() > maxLength) {
			returnValue = returnValue.substring(0, maxLength);
		}
		accessLog.setReturnValue(returnValue);

		if (enabledMdc) {
			MDC.put(MdcConstants.CLASS_NAME, className);
			MDC.put(MdcConstants.METHOD_NAME, methodName);
			MDC.put(MdcConstants.ARGUMENTS, StringUtil.trimToEmpty(arguments));
			MDC.put(MdcConstants.RETURN_VALUE, StringUtil.trimToEmpty(returnValue));
			MDC.put(MdcConstants.DURATION, String.valueOf(duration));
		}
		log(accessLog, slowThreshold);
	}

	/**
	 * Logs access information for an HTTP request. This method captures and formats
	 * details about the HTTP request and response, including URI, request body, response
	 * body, and execution time.
	 * @param req the HTTP servlet request
	 * @param resp the HTTP servlet response
	 * @param throwable the exception thrown during processing, or null if successful
	 * @param duration the request processing time in milliseconds
	 * @param enabledMdc whether to store logging information in MDC
	 * @param maxLength maximum length for logged values to prevent excessive log sizes
	 * @param slowThreshold threshold in milliseconds for identifying slow requests
	 */
	public static void log(HttpServletRequest req, HttpServletResponse resp, Throwable throwable, long duration,
			boolean enabledMdc, int maxLength, long slowThreshold) {
		AccessLog accessLog = new AccessLog();
		accessLog.setThrowable(throwable);
		accessLog.setDuration(duration);

		String remoteUser = ServletUtils.getRemoteUser();
		String remoteAddr = IpConfigUtils.parseIpAddress(req);
		String location = req.getRequestURI();
		accessLog.setLocation(location);

		String arguments = ServletUtils.getRequestBOdy(req);
		if (arguments != null && arguments.length() > maxLength) {
			arguments = arguments.substring(0, maxLength);

		}
		accessLog.setArguments(arguments);

		String returnValue = ServletUtils.getResponseBody(resp);
		if (returnValue.length() > maxLength) {
			returnValue = returnValue.substring(0, maxLength);
		}
		accessLog.setReturnValue(returnValue);

		if (enabledMdc) {
			MDC.put(MdcConstants.REMOTE_USER, remoteUser);
			MDC.put(MdcConstants.REMOTE_ADDR, remoteAddr);
			MDC.put(MdcConstants.ARGUMENTS, StringUtil.trimToEmpty(arguments));
			MDC.put(MdcConstants.RETURN_VALUE, StringUtil.trimToEmpty(returnValue));
			MDC.put(MdcConstants.DURATION, String.valueOf(duration));
		}

		log(accessLog, slowThreshold);
	}

	/**
	 * Logs the access information with the appropriate log level based on the execution
	 * result. If an exception occurred, logs at ERROR level. If execution time exceeds
	 * the slow threshold, logs at WARN level. Otherwise, logs at INFO level.
	 * @param accessLog the access log object containing execution details
	 * @param slowThreshold the threshold in milliseconds above which a request is
	 * considered slow
	 */
	public static void log(AccessLog accessLog, long slowThreshold) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(accessLog.getLocation()).append("(").append(accessLog.getArguments()).append(")");

		if (accessLog.getThrowable() != null) {
			stringBuilder.append(" threw exception: ")
				.append(accessLog.getThrowable())
				.append(" (")
				.append(accessLog.getDuration())
				.append("ms)");
			log.error(stringBuilder.toString());
		}
		else {
			stringBuilder.append(" returned: ")
				.append(accessLog.getReturnValue())
				.append(" (")
				.append(accessLog.getDuration())
				.append("ms)");
			if (accessLog.getDuration() >= slowThreshold) {
				log.warn(stringBuilder.toString());
			}
			else {
				log.info(stringBuilder.toString());
			}
		}
	}

}
