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

import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.AdviceModeImportSelector;
import org.springframework.context.annotation.AutoProxyRegistrar;

import space.x9x.radp.spring.framework.logging.EnableAccessLog;

/**
 * Import selector for access log aspect configuration. This class determines which
 * configuration classes to import based on the advice mode specified in the
 * EnableAccessLog annotation.
 *
 * @author IO x9x
 * @since 2024-09-30 09:56
 */
public class AccessLogImportSelector extends AdviceModeImportSelector<EnableAccessLog> {

	/**
	 * Selects which configuration classes to import based on the specified advice mode.
	 * For PROXY mode, imports both AutoProxyRegistrar and AccessLogConfiguration. For
	 * ASPECTJ mode, imports only AccessLogConfiguration.
	 * @param adviceMode the advice mode specified in the EnableAccessLog annotation
	 * @return an array of fully qualified class names to import
	 */
	@Override
	protected String[] selectImports(AdviceMode adviceMode) {
		switch (adviceMode) {
			case PROXY:
				return new String[] { AutoProxyRegistrar.class.getName(), AccessLogConfiguration.class.getName() };
			case ASPECTJ:
				return new String[] { AccessLogConfiguration.class.getName() };
			default:
		}
		return new String[0];
	}

}
