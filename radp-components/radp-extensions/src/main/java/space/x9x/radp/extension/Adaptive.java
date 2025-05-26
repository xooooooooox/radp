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
 * 默认扩展实现标记
 *
 * @author IO x9x
 * @since 2024-09-24 13:57
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface Adaptive {

	/**
	 * Specifies the keys or names for which this adaptive implementation should be used.
	 * When multiple implementations of an extension point exist, the one with matching
	 * keys in this value array will be selected based on the runtime context. If not
	 * specified (an empty array), this implementation will be used as a fallback when no
	 * other implementation matches the context.
	 * @return an array of keys or names for adaptive selection
	 */
	String[] value() default {};

}
