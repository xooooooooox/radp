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

package space.x9x.radp.swagger3.spring.boot.env;

import space.x9x.radp.spring.framework.bootstrap.constant.Globals;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author x9x
 * @since 2024-09-30 16:36
 */
@Getter
@Setter
@ConfigurationProperties(prefix = Globals.RADP_CONFIGURATION_PROPERTIES_PREFIX + "swagger3")
public class SwaggerProperties {
    private String contactEmail = "xozozsos@gmailcom";
    private String contactName = "x9x";
    private String contactUrl = null;
    private String defaultIncludePattern = "/api/.*";
    private String description = "API documentation";
    private String host = null;
    private String license = null;
    private String licenseUrl = null;
    private String[] protocols = {};
    private String termsOfServiceUrl = null;
    private String title = "Application API";
    private boolean useDefaultResponseMessages = false;
    private String version = "1.0.0";
}
