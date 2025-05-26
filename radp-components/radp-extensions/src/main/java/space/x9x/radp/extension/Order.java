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

package space.x9x.radp.extension;

import java.lang.annotation.*;

/**
 * 扩展点顺序, 相同扩展点名称根据 {@code Order} 优先排序
 *
 * @author x9x
 * @since 2024-09-24 23:47
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Order {
    /**
     * Constant indicating the highest precedence that can be assigned to an extension.
     * Extensions with this precedence value will be processed first.
     */
    int HIGHEST_PRECEDENCE = Integer.MIN_VALUE;

    /**
     * Constant indicating the lowest precedence that can be assigned to an extension.
     * Extensions with this precedence value will be processed last.
     */
    int LOWEST_PRECEDENCE = Integer.MAX_VALUE;

    /**
     * Specifies the order value for the annotated extension.
     * Lower values have higher priority. The default value is 0.
     *
     * @return the order value
     */
    int value() default 0;
}
