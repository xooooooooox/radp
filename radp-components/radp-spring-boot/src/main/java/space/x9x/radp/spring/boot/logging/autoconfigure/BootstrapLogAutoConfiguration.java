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

package space.x9x.radp.spring.boot.logging.autoconfigure;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Role;

import space.x9x.radp.spring.boot.bootstrap.constants.Conditions;
import space.x9x.radp.spring.boot.logging.env.BootstrapLogProperties;
import space.x9x.radp.spring.framework.logging.EnableBootstrapLog;

/**
 * Autoconfiguration for bootstrap logging. This configuration class automatically enables
 * bootstrap logging in the application when the appropriate property is set. It uses the
 * BootstrapLogProperties for configuration and applies the @EnableBootstrapLog annotation
 * to activate the bootstrap logging functionality. The configuration is activated when
 * the 'radp.bootstrap-log.enabled' property is set to 'true', or by default if the
 * property is not specified (matchIfMissing = true).
 *
 * @author IO x9x
 * @since 2024-09-30 11:37
 */
@ConditionalOnProperty(prefix = BootstrapLogProperties.PREFIX, name = Conditions.ENABLED, havingValue = Conditions.TRUE,
		matchIfMissing = true)
@EnableConfigurationProperties(BootstrapLogProperties.class)
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@AutoConfiguration
@EnableBootstrapLog
public class BootstrapLogAutoConfiguration {

}
