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

import org.springframework.context.annotation.Import;

import space.x9x.radp.spring.framework.logging.bootstrap.config.BootstrapLogConfiguration;

/**
 * Annotation that enables bootstrap logging for a Spring application. When applied to a
 * configuration class, this annotation sets up the necessary infrastructure for logging
 * application context information during startup and HTTP request processing.
 *
 * <p>
 * Bootstrap logging adds contextual information to logs, such as:
 * <ul>
 * <li>Application name</li>
 * <li>Active Spring profiles</li>
 * <li>Request URI</li>
 * <li>Remote user and IP address</li>
 * <li>Local server address</li>
 * </ul>
 *
 * <p>
 * This information is added to the MDC (Mapped Diagnostic Context) and is available to
 * all logging statements, making it easier to trace and debug application behavior.
 *
 * <p>
 * Example usage: <pre>
 * &#64;Configuration
 * &#64;EnableBootstrapLog
 * public class AppConfig {
 *     // configuration details
 * }
 * </pre>
 *
 * @author RADP x9x
 * @since 2024-09-30 09:53
 * @see space.x9x.radp.spring.framework.logging.bootstrap.config.BootstrapLogConfiguration
 * @see space.x9x.radp.spring.framework.logging.bootstrap.filter.BootstrapLogHttpFilter
 */
@Import(BootstrapLogConfiguration.class)
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EnableBootstrapLog {

}
