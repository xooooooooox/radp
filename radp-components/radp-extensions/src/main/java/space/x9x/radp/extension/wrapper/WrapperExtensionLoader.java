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

package space.x9x.radp.extension.wrapper;

import java.util.HashSet;
import java.util.Set;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 基于 @Wrapper 的扩展点加速器.
 * <p>
 * Extension loader based on the @Wrapper annotation. This class is responsible for
 * loading and managing extension implementations that are annotated with @Wrapper, which
 * allows extensions to wrap or enhance other extensions.
 *
 * @author IO x9x
 * @since 2024-09-24 14:00
 */
@RequiredArgsConstructor
public class WrapperExtensionLoader {

	/**
	 * Cache for wrapper classes. This set stores all classes that have been identified as
	 * wrapper classes (classes annotated with @Wrapper) during the extension loading
	 * process.
	 */
	@Getter
	private Set<Class<?>> cachedWrapperClasses;

	/**
	 * Caches a wrapper class for later use. This method adds a class to the internal
	 * cache of wrapper classes. If the cache doesn't exist yet, it will be initialized.
	 * @param clazz the wrapper class to cache
	 */
	public void cacheWrapperClass(Class<?> clazz) {
		if (this.cachedWrapperClasses == null) {
			this.cachedWrapperClasses = new HashSet<>();
		}
		this.cachedWrapperClasses.add(clazz);
	}

}
