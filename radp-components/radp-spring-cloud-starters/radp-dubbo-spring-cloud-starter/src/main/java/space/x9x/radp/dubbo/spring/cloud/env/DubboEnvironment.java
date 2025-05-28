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

package space.x9x.radp.dubbo.spring.cloud.env;

import lombok.experimental.UtilityClass;

/**
 * Constants for Dubbo environment configuration properties. This utility class provides a
 * centralized definition of property names used for configuring Dubbo in Spring Cloud
 * applications. These constants can be used when programmatically accessing or setting
 * Dubbo configuration properties.
 *
 * @author IO x9x
 * @since 2024-10-01 23:41
 */
@UtilityClass
public class DubboEnvironment {

	/**
	 * Property name for enabling/disabling Dubbo. This constant defines the property name
	 * used to control whether Dubbo is enabled.
	 */
	public static final String ENABLED = "dubbo.enabled";

	/**
	 * Property name for the Dubbo protocol name configuration. This constant defines the
	 * property name used to configure the protocol that Dubbo will use for communication
	 * (e.g., dubbo, rest, http).
	 */
	public static final String PROTOCOL = "dubbo.protocol.name";

	/**
	 * Property name for the Dubbo protocol port configuration. This constant defines the
	 * property name used to configure the port on which the Dubbo service will listen for
	 * incoming requests.
	 */
	public static final String PORT = "dubbo.protocol.port";

	/**
	 * Property name for the Dubbo registry address configuration. This constant defines
	 * the property name used to configure the address of the registry service (e.g.,
	 * ZooKeeper) that Dubbo will use for service discovery.
	 */
	public static final String REGISTRY_ADDRESS = "dubbo.registry.address";

}
