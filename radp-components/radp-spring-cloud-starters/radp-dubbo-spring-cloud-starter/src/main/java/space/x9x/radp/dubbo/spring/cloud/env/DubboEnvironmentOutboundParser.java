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

import space.x9x.radp.commons.lang.MessageFormatUtils;
import space.x9x.radp.commons.lang.Strings;
import space.x9x.radp.spring.boot.bootstrap.constants.Conditions;
import space.x9x.radp.spring.boot.bootstrap.env.EnvironmentOutboundParser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;

/**
 * @author x9x
 * @since 2024-10-03 01:20
 */
public class DubboEnvironmentOutboundParser implements EnvironmentOutboundParser {

    private static final String TEMPLATE = "Outbound Dubbo Registry: \t{}";

    @Override
    public String toString(Environment env) {
        boolean disabled = !Boolean.parseBoolean(env.getProperty(DubboEnvironment.ENABLED, Conditions.TRUE));
        if (disabled) {
            return Strings.EMPTY;
        }

        String registryAddress = StringUtils.trimToEmpty(env.getProperty(DubboEnvironment.REGISTRY_ADDRESS));
        return MessageFormatUtils.format(TEMPLATE, registryAddress);
    }
}
