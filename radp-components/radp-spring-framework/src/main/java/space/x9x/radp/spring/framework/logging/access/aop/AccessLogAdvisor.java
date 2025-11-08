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

import java.io.Serial;

import org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;

/**
 * Advisor for access logging that uses AspectJ expression pointcuts. This advisor
 * connects the AccessLogInterceptor with the methods that should be logged, based on the
 * pointcut expression configured in the AccessLogConfig.
 *
 * <p>
 * It extends AspectJExpressionPointcutAdvisor to leverage Spring's AOP infrastructure for
 * method interception based on AspectJ pointcut expressions.
 *
 * @author x9x
 * @since 2024-09-30 09:52
 */
public class AccessLogAdvisor extends AspectJExpressionPointcutAdvisor {

	/**
	 * Serial version UID for serialization compatibility.
	 */
	@Serial
	private static final long serialVersionUID = 1L;

}
