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

package space.x9x.radp.mybatis.spring.boot.env;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import space.x9x.radp.spring.framework.bootstrap.constant.Globals;

/**
 * Configuration properties for MyBatis-Plus extensions.
 * This class defines settings for MyBatis-Plus extensions, particularly for
 * automatic field filling functionality like creation and modification timestamps.
 *
 * @author x9x
 * @since 2024-09-30 13:25
 */
@ConfigurationProperties(prefix = MybatisPlusExtensionProperties.PREFIX)
@Data
public class MybatisPlusExtensionProperties {

    /**
     * Configuration prefix for MyBatis-Plus extension properties.
     * This constant is used as part of the property path in application configuration.
     */
    public static final String PREFIX = Globals.RADP_CONFIGURATION_PROPERTIES_PREFIX + "mybatis-plus.extension";

    /**
     * Property path for enabling automatic field filling.
     * This constant represents the full property path to enable/disable automatic field filling.
     */
    public static final String AUTO_FILL_ENABLED = PREFIX + ".auto-fill.enabled";

    private final AutoFill autoFill = new AutoFill();

    /**
     * Configuration properties for automatic field filling functionality.
     * This inner class contains settings related to automatically filling
     * creation and modification timestamp fields in entity objects.
     */
    @Data
    public static class AutoFill {
        /**
         * 是否开启自动填充创建时间和更新时间字段,默认步开启
         */
        private boolean enabled = false;
        /**
         * 创建时间字段名(Java Property name not Database column name)
         */
        private String createdDataFieldName = "createdDate";
        /**
         * 更新时间字段名(Java Property name not Database column name)
         */
        private String lastModifiedDateFieldName = "lastModifiedDate";
    }
}
