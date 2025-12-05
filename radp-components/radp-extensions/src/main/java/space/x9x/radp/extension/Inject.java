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
 * Annotation for injecting extension instances into fields of other extensions. This
 * annotation can be applied to methods (typically setter methods) to indicate that the
 * parameter should be injected with an extension instance.
 *
 * @author RADP x9x
 * @since 2024-09-24 16:32
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface Inject {

	/**
	 * Controls whether dependency injection is enabled for the annotated element. When
	 * set to true (default), the dependency will be injected. When set to false, the
	 * dependency will not be injected.
	 * @return true if injection is enabled, false otherwise
	 */
	boolean enable() default true;

	/**
	 * Specifies the type of injection to use. This determines how dependencies are
	 * matched and injected.
	 * @return the type of injection to use
	 */
	InjectType type() default InjectType.BY_NAME;

	/**
	 * Defines the available types of dependency injection. This enum specifies how
	 * dependencies should be matched and injected.
	 */
	enum InjectType {

		/**
		 * Indicates that dependencies should be injected by matching their names. This is
		 * the default injection type.
		 */
		BY_NAME,

		/**
		 * Indicates that dependencies should be injected by matching their types. This
		 * injection type ignores names and matches solely based on the type.
		 */
		BY_TYPE

	}

}
