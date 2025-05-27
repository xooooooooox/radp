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

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.MDC;
import space.x9x.radp.commons.lang.ObjectUtils;
import space.x9x.radp.commons.lang.StringUtils;
import space.x9x.radp.commons.lang.Strings;
import space.x9x.radp.commons.net.IpConfigUtils;
import space.x9x.radp.spring.framework.json.support.JSONHelper;
import space.x9x.radp.spring.framework.logging.MdcConstants;
import space.x9x.radp.spring.framework.logging.access.model.AccessLog;
import space.x9x.radp.spring.framework.web.util.ServletUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * @author IO x9x
 * @since 2024-09-30 10:20
 */
@UtilityClass
@Slf4j
public class AccessLogHelper {

	/**
	 * 根据采样率的判断是否需要输出日志
	 * @param sampleRate 采样率
	 * @return 是否输出
	 */
	public static boolean shouldLog(double sampleRate) {
		return sampleRate >= 1.0 || Math.random() < sampleRate;
	}

	/**
	 * 输出访问日志
	 * @param invocation 方法拦截
	 * @param result 返回值
	 * @param throwable 异常
	 * @param duration 耗时
	 * @param enabledMdc 是否保存到 MDC
	 * @param maxLength 最大长度
	 * @param slowThreshold 慢访问阈值
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

		String returnValue = ObjectUtils.isEmpty(result) ? Strings.EMPTY : JSONHelper.json().toJSONString(result);
		if (returnValue.length() > maxLength) {
			returnValue = returnValue.substring(0, maxLength);
		}
		accessLog.setReturnValue(returnValue);

		if (enabledMdc) {
			MDC.put(MdcConstants.CLASS_NAME, className);
			MDC.put(MdcConstants.METHOD_NAME, methodName);
			MDC.put(MdcConstants.ARGUMENTS, StringUtils.trimToEmpty(arguments));
			MDC.put(MdcConstants.RETURN_VALUE, StringUtils.trimToEmpty(returnValue));
			MDC.put(MdcConstants.DURATION, String.valueOf(duration));
		}
		log(accessLog, slowThreshold);
	}

	/**
	 * 输出访问日志
	 * @param req 请求
	 * @param resp 响应
	 * @param throwable 异常
	 * @param duration 耗时
	 * @param enabledMdc 是否保存到 MDC
	 * @param maxLength 最大长度
	 * @param slowThreshold 慢访问阈值
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
			MDC.put(MdcConstants.ARGUMENTS, StringUtils.trimToEmpty(arguments));
			MDC.put(MdcConstants.RETURN_VALUE, StringUtils.trimToEmpty(returnValue));
			MDC.put(MdcConstants.DURATION, String.valueOf(duration));
		}

		log(accessLog, slowThreshold);
	}

	/**
	 * Logs the access information with appropriate log level based on execution result.
	 * If an exception occurred, logs at ERROR level. If execution time exceeded the slow
	 * threshold, logs at WARN level. Otherwise, logs at INFO level.
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
