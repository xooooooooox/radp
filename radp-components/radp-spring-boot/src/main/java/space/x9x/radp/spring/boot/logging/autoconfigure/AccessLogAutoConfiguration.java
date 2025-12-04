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
import space.x9x.radp.spring.boot.logging.env.AccessLogProperties;
import space.x9x.radp.spring.framework.logging.EnableAccessLog;

/**
 * Autoconfiguration for access logging. This configuration class automatically enables
 * access logging in the application when the appropriate property is set. It uses the
 * AccessLogProperties for configuration and applies the @EnableAccessLog annotation to
 * activate the access logging functionality. The configuration is only activated when the
 * 'radp.access-log.enabled' property is set to 'true'.
 *
 * @author RADP x9x
 * @since 2024-09-30 11:30
 */
@ConditionalOnProperty(prefix = AccessLogProperties.PREFIX, name = Conditions.ENABLED, havingValue = Conditions.TRUE)
@EnableConfigurationProperties(AccessLogProperties.class)
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@AutoConfiguration
@EnableAccessLog
public class AccessLogAutoConfiguration {

}
