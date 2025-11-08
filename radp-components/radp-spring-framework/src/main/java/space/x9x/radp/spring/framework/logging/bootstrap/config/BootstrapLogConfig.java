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

package space.x9x.radp.spring.framework.logging.bootstrap.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Configuration class for bootstrap logging functionality. This class defines settings
 * that control the behavior of bootstrap logging, such as whether to store logging
 * information in MDC (Mapped Diagnostic Context). It can be customized by providing a
 * bean of this type in the application context.
 *
 * @author x9x
 * @since 2024-09-28 20:48
 * @see space.x9x.radp.spring.framework.logging.bootstrap.config.BootstrapLogConfiguration
 * @see space.x9x.radp.spring.framework.logging.bootstrap.filter.BootstrapLogHttpFilter
 */
@EqualsAndHashCode
@ToString
@Data
public class BootstrapLogConfig {

	/**
	 * Flag indicating whether to store logging information in MDC (Mapped Diagnostic
	 * Context). When enabled, contextual information like application name, active
	 * profiles, and request details are added to the MDC and available to all logging
	 * statements. Default is true.
	 */
	private boolean enabledMdc = true;

}
