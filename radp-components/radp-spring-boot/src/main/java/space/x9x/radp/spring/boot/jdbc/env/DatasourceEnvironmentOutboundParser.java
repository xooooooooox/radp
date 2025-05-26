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

package space.x9x.radp.spring.boot.jdbc.env;

import space.x9x.radp.commons.lang.MessageFormatUtils;
import space.x9x.radp.commons.lang.StringUtils;
import space.x9x.radp.commons.lang.Strings;
import space.x9x.radp.spring.boot.bootstrap.env.EnvironmentOutboundParser;
import org.springframework.core.env.Environment;

/**
 * @author IO x9x
 * @since 2024-09-30 09:38
 */
public class DatasourceEnvironmentOutboundParser implements EnvironmentOutboundParser {

    private static final String TEMPLATE = "Outbound Datasource: \t{}";

    @Override
    public String toString(Environment env) {
        if (!env.containsProperty(DatasourceEnvironment.URL)) {
            return Strings.EMPTY;
        }

        String url = env.getProperty(DatasourceEnvironment.URL);
        if (StringUtils.isNotBlank(url) && url.contains(Strings.PLACEHOLDER)) {
            url = url.substring(0, url.indexOf(Strings.PLACEHOLDER));
        }
        return MessageFormatUtils.format(TEMPLATE, url);
    }
}
