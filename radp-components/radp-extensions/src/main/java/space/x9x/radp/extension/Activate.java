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

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 激活扩展点.
 * <p>
 * Activation annotation for extension points. This annotation is used to mark extensions
 * that should be activated under specific conditions, such as belonging to certain groups
 * or when specific configuration parameters are present.
 *
 * @author RADP x9x
 * @since 2024-09-24 12:49
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface Activate {

	/**
	 * Specifies the groups to which this extension belongs. Extensions can be activated
	 * based on their group membership. If not specified, the extension does not belong to
	 * any group.
	 * @return an array of group names this extension belongs to
	 */
	String[] groups() default {};

	/**
	 * Specifies the keys or values for conditional activation. Extensions can be
	 * activated when certain keys or values are present in the URL. If not specified, the
	 * extension will be activated regardless of URL parameters.
	 * @return an array of keys or values for conditional activation
	 */
	String[] value() default {};

	/**
	 * Specifies the order in which extensions should be executed. Extensions with lower
	 * order values are executed before those with higher values. The default order is 0.
	 * @return the order value for this extension
	 */
	int order() default 0;

}
