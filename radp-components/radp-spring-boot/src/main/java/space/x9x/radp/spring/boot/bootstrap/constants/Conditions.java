/*
 * Copyright 2012-2019 the original author or authors.
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

package space.x9x.radp.spring.boot.bootstrap.constants;

import lombok.experimental.UtilityClass;

/**
 * 自动转配条件常量定义
 *
 * @author x9x
 * @since 2024-09-30 09:15
 */
@UtilityClass
public class Conditions {

    /**
     * Common property suffix for enabling features.
     * This constant is used in conditional annotations to check if a feature is enabled.
     */
    public static final String ENABLED = "enabled";

    /**
     * String representation of boolean true value.
     * This constant is used in conditional annotations for property value comparison.
     */
    public static final String TRUE = "true";

    /**
     * String representation of boolean false value.
     * This constant is used in conditional annotations for property value comparison.
     */
    public static final String FALSE = "false";

    /**
     * Property suffix for marking a bean as primary.
     * This constant is used in conditional annotations to check if a bean should be primary.
     */
    public static final String PRIMARY = "primary";
}
