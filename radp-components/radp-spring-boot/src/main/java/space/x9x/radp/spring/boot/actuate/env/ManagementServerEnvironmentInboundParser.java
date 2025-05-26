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

import space.x9x.radp.commons.lang.MessageFormatUtils;
import space.x9x.radp.commons.lang.StringUtils;
import space.x9x.radp.commons.lang.Strings;
import space.x9x.radp.commons.lang.math.NumberUtils;
import space.x9x.radp.commons.net.IpConfigUtils;
import space.x9x.radp.spring.boot.bootstrap.env.EnvironmentInboundParser;
import org.springframework.core.env.Environment;

/**
 * @author IO x9x
 * @since 2024-09-30 09:21
 */
public class ManagementServerEnvironmentInboundParser implements EnvironmentInboundParser {
    private static final String TEMPLATE = "Inbound Management Server: \t{}://{}:{}{}";
    private static final String PROTOCOL = "http";

    @Override
    public String toString(Environment env) {
        if (!env.containsProperty(ManagementServerEnvironment.PORT)) {
            return Strings.EMPTY;
        }

        int port = NumberUtils.toInt(env.getProperty(ManagementServerEnvironment.PORT));
        String basePath = StringUtils.trimToEmpty(env.getProperty(ManagementServerEnvironment.ENDPOINTS_WEB_BASE_PATH));
        return MessageFormatUtils.format(TEMPLATE, PROTOCOL, IpConfigUtils.getIpAddress(), port, basePath);
    }
}
