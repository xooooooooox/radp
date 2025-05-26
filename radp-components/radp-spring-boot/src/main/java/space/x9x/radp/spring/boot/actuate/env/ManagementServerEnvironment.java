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

package space.x9x.radp.spring.boot.actuate.env;

import lombok.experimental.UtilityClass;

/**
 * @author IO x9x
 * @since 2024-09-30 09:21
 */
@UtilityClass
public class ManagementServerEnvironment {
    /**
     * Property name for the management server port configuration.
     * This constant defines the Spring Boot property name used to configure the port
     * on which the Spring Boot Actuator management server runs.
     */
    public static final String PORT = "management.server.port";

    /**
     * Property name for the web endpoints base path configuration.
     * This constant defines the Spring Boot property name used to configure the base path
     * for all web endpoints exposed by Spring Boot Actuator.
     */
    public static final String ENDPOINTS_WEB_BASE_PATH = "management.endpoints.web.base-path";
}
