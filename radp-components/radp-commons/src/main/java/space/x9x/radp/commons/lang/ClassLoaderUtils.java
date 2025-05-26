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

package space.x9x.radp.commons.lang;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import lombok.experimental.UtilityClass;

/**
 * @author IO x9x
 * @since 2024-09-24 20:23
 */
@UtilityClass
public class ClassLoaderUtils {

	/**
	 * Map of primitive type names to their corresponding Class objects.
	 */
	private static final Map<String, Class<?>> PRIMITIVE_CLASSES;

	static {
		final Map<String, Class<?>> primitives = new HashMap<>(10, 1.0f);
		primitives.put("boolean", boolean.class);
		primitives.put("byte", byte.class);
		primitives.put("char", char.class);
		primitives.put("short", short.class);
		primitives.put("int", int.class);
		primitives.put("long", long.class);
		primitives.put("float", float.class);
		primitives.put("double", double.class);
		primitives.put("void", void.class);
		PRIMITIVE_CLASSES = Collections.unmodifiableMap(primitives);
	}

	/**
	 * Gets an appropriate ClassLoader based on context.
	 * <p>
	 * This method attempts to find a ClassLoader in the following order:
	 * <ol>
	 * <li>The current thread's context ClassLoader</li>
	 * <li>The ClassLoader of the specified class</li>
	 * <li>The system ClassLoader</li>
	 * </ol>
	 * @param clazz the class whose ClassLoader should be used if the thread context
	 * ClassLoader is unavailable
	 * @return an appropriate ClassLoader
	 */
	public static ClassLoader getClassLoader(Class<?> clazz) {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		if (classLoader == null) {
			classLoader = clazz.getClassLoader();
			if (classLoader == null) {
				classLoader = ClassLoader.getSystemClassLoader();
			}
		}
		return classLoader;
	}

}
