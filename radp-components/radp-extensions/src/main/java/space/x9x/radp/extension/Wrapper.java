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

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 扩展点包装器.
 * <p>
 * Extension point wrapper annotation. This annotation is used to mark classes that wrap
 * or extend other extension points. It allows specifying which extension points should be
 * matched or excluded.
 *
 * @author RADP x9x
 * @since 2024-09-24 13:55
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Wrapper {

	/**
	 * Specifies the extension points that this wrapper should match. Only extension
	 * points that match these patterns will be wrapped.
	 * @return an array of patterns to match extension points
	 */
	String[] matches() default {};

	/**
	 * Specifies the extension points that this wrapper should not match. Extension points
	 * that match these patterns will be excluded from wrapping.
	 * @return an array of patterns to exclude extension points
	 */
	String[] mismatches() default {};

}
