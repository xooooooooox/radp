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

package space.x9x.radp.spring.boot.logging.env;

import space.x9x.radp.commons.lang.StringUtils;
import space.x9x.radp.commons.lang.Strings;
import space.x9x.radp.commons.net.IpConfigUtils;
import space.x9x.radp.spring.framework.bootstrap.constant.SpringProperties;
import space.x9x.radp.spring.framework.bootstrap.utils.SpringProfileUtils;
import space.x9x.radp.spring.framework.logging.MdcConstants;
import org.slf4j.MDC;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * 全局日志环境解析后置处理
 * @author x9x
 * @since 2024-09-27 21:16
 */
public class BootstrapLogEnvironmentPostProcessor implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        if (environment.containsProperty(BootstrapLogProperties.ENABLED) || Boolean.parseBoolean(StringUtils.trimToEmpty(environment.getProperty(BootstrapLogProperties.ENABLED)))) {
            String appName = StringUtils.trimToEmpty(environment.getProperty(SpringProperties.SPRING_APPLICATION_NAME));
            String profile = StringUtils.trimToEmpty(String.join(Strings.COMMA, SpringProfileUtils.getActiveProfiles(environment)));
            MDC.put(MdcConstants.APP, appName);
            MDC.put(MdcConstants.PROFILE, profile);
            MDC.put(MdcConstants.LOCAL_ADDR, IpConfigUtils.getIpAddress());
        }
    }
}
