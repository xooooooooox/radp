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

package space.x9x.radp.commons.collections;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import lombok.experimental.UtilityClass;

/**
 * @author IO x9x
 * @since 2024-09-24 13:10
 */
@UtilityClass
public class CollectionUtils {

	/**
	 * Checks if the specified collection is empty.
	 * <p>
	 * This method is a wrapper around Apache Commons Collections' isEmpty method.
	 * @param coll the collection to check, may be null
	 * @return true if the collection is null or empty
	 */
	public static boolean isEmpty(final Collection<?> coll) {
		return org.apache.commons.collections4.CollectionUtils.isEmpty(coll);
	}

	/**
	 * Checks if the specified collection is not empty.
	 * <p>
	 * This method is a wrapper around Apache Commons Collections' isNotEmpty method.
	 * @param coll the collection to check, may be null
	 * @return true if the collection is not null and not empty
	 */
	public static boolean isNotEmpty(final Collection<?> coll) {
		return org.apache.commons.collections4.CollectionUtils.isNotEmpty(coll);
	}

	/**
	 * 创建一个不可变的集合，包含给定的元素 该方法接受任意数量的参数，并返回一个不可变的集合，其中包含了这些参数 如果没有提供参数或者参数数组为空，将返回一个空的不可变集合
	 * @param values 要包含在集合中的元素，可以是任意类型
	 * @param <T> the type of elements in the set
	 * @return 一个不可变的集合，包含给定的元素
	 */
	public static <T> Set<T> ofSet(T... values) {
		// Ensure that the array is not null and has elements
		if (values == null || values.length == 0) {
			return Collections.emptySet();
		}

		// Calculate the load factor
		float loadFactor = 1f / ((values.length + 1) * 1.0f);
		if (loadFactor > 0.75f) {
			loadFactor = 0.75f;
		}

		// Create a LinkedHashSet with the specified initial capacity and load factor
		Set<T> elements = new LinkedHashSet<>(values.length, loadFactor);
		Collections.addAll(elements, values);
		return Collections.unmodifiableSet(elements);
	}

}
