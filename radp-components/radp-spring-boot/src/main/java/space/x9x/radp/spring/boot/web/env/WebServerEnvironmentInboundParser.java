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

package space.x9x.radp.spring.boot.web.env;

import space.x9x.radp.commons.lang.MessageFormatUtils;
import space.x9x.radp.commons.lang.StringUtils;
import space.x9x.radp.commons.lang.Strings;
import space.x9x.radp.commons.lang.math.NumberUtils;
import space.x9x.radp.commons.net.IpConfigUtils;
import space.x9x.radp.spring.boot.bootstrap.env.EnvironmentInboundParser;
import org.springframework.core.env.Environment;

/**
 * @author IO x9x
 * @since 2024-09-30 09:28
 */
public class WebServerEnvironmentInboundParser implements EnvironmentInboundParser {
    private static final String TEMPLATE = "Inbound Web Server: \t{}://{}:{}{}";

    @Override
    public String toString(Environment env) {
        if (!env.containsProperty(WebServerEnvironment.SERVER_PORT)) {
            return Strings.EMPTY;
        }

        boolean protocol = env.containsProperty(WebServerEnvironment.SERVER_SSL_KEY_STORE);
        int port = NumberUtils.toInt(WebServerEnvironment.SERVER_PORT);
        String contextPath = StringUtils.trimToEmpty(env.getProperty(WebServerEnvironment.SERVER_SERVLET_CONTEXT_PATH));
        return MessageFormatUtils.format(TEMPLATE, protocol, IpConfigUtils.getIpAddress(), port, contextPath);
    }
}
