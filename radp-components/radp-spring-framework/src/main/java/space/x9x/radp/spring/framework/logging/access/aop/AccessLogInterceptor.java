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

package space.x9x.radp.spring.framework.logging.access.aop;

import java.time.Duration;
import java.time.Instant;

import lombok.RequiredArgsConstructor;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import org.springframework.aop.support.AopUtils;

import space.x9x.radp.spring.framework.logging.access.config.AccessLogConfig;
import space.x9x.radp.spring.framework.logging.access.util.AccessLogHelper;

/**
 * A method interceptor that logs method access information for monitoring and debugging
 * purposes. This interceptor captures method execution details including execution time,
 * parameters, return values, and exceptions.
 *
 * <p>
 * The interceptor uses sampling to control the volume of logs generated and can be
 * configured to highlight slow method executions. It integrates with MDC (Mapped
 * Diagnostic Context) for correlating log entries across threads.
 *
 * @author IO x9x
 * @since 2024-09-30 09:52
 */
@RequiredArgsConstructor
public class AccessLogInterceptor implements MethodInterceptor {

	/**
	 * The configuration for access logging behavior. This holds settings that control
	 * what and how information is logged.
	 */
	private final AccessLogConfig accessLogConfig;

	/**
	 * Intercepts method invocations to log access information. This method captures
	 * execution time, parameters, return values, and exceptions if they occur.
	 *
	 * <p>
	 * The method uses sampling based on the configured sample rate to determine whether
	 * to log a particular invocation. It also detects and highlights slow method
	 * executions based on the configured threshold.
	 * @param invocation the method invocation being intercepted
	 * @return the result of the method invocation
	 * @throws Throwable if the intercepted method throws an exception
	 */
	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		// 排除代理类
		if (AopUtils.isAopProxy(invocation.getThis())) {
			return invocation.proceed();
		}

		// 判断是否需要输出日志
		if (AccessLogHelper.shouldLog(this.accessLogConfig.getSampleRate())) {
			return invocation.proceed();
		}

		Instant start = Instant.now();
		Object result = null;
		Throwable throwable = null;
		try {
			result = invocation.proceed();
			return result;
		}
		catch (Throwable th) {
			throwable = th;
			throw throwable;
		}
		finally {
			long duration = Duration.between(start, Instant.now()).toMillis();
			AccessLogHelper.log(invocation, result, throwable, duration, this.accessLogConfig.isEnabledMdc(),
					this.accessLogConfig.getMaxLength(), this.accessLogConfig.getSlowThreshold());
		}
	}

}
