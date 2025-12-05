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

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;

import space.x9x.radp.spring.framework.logging.access.config.AccessLogImportSelector;

/**
 * Annotation that enables access logging for methods in a Spring application. When
 * applied to a configuration class, this annotation sets up the necessary infrastructure
 * for logging method invocations based on the specified pointcut expression.
 *
 * <p>
 * The logging behavior can be customized through various attributes of this annotation,
 * including the proxy mode, target class proxying, and advisor order.
 *
 * <p>
 * Example usage: <pre>
 * &#64;Configuration
 * &#64;EnableAccessLog(expression = "execution(* com.example.service.*.*(..))")
 * public class AppConfig {
 *     // configuration details
 * }
 * </pre>
 *
 * @author RADP x9x
 * @since 2024-09-30 09:53
 */
@Import(AccessLogImportSelector.class)
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EnableAccessLog {

	/**
	 * Indicates whether CGLIB proxying should be enabled. When set to true, CGLIB proxies
	 * will be used instead of JDK dynamic proxies, allowing proxying of classes that
	 * don't implement interfaces.
	 * @return true to enable CGLIB proxying, false to use standard JDK dynamic proxies
	 */
	boolean proxyTargetClass() default false;

	/**
	 * Specifies the advice mode to use for method interception. The default mode is
	 * PROXY, which uses Spring's AOP proxy mechanism. ASPECTJ mode can be used for more
	 * advanced scenarios.
	 * @return the advice mode to use (PROXY or ASPECTJ)
	 */
	AdviceMode mode() default AdviceMode.PROXY;

	/**
	 * Defines the order of the pointcut advisor. This affects the order in which this
	 * advisor is applied relative to other advisors. Lower values have higher precedence.
	 * @return the order value for the advisor
	 */
	int order() default Ordered.LOWEST_PRECEDENCE;

	/**
	 * Specifies the AspectJ pointcut expression that determines which methods to log.
	 * This expression defines the join points (methods) where access logging should be
	 * applied. If empty, a default expression may be used based on configuration.
	 * @return the AspectJ pointcut expression
	 */
	String expression() default "";

}
