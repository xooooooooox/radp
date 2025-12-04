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

import java.util.HashMap;
import java.util.Map;

import cn.hutool.core.map.MapBuilder;
import cn.hutool.core.map.MapUtil;
import lombok.experimental.UtilityClass;

/**
 * Utility class for map operations. This class extends Hutool's MapUtil to provide
 * additional utility methods for working with maps.
 *
 * @author x9x
 * @since 2024-10-23 15:41
 * @see cn.hutool.core.map.MapUtil
 */
@UtilityClass
public class MapUtils {

	public static <K, V> MapBuilder<K, V> builder(Map<K, V> map) {
		return MapUtil.builder(map);
	}

	public static <K, V> HashMap<K, V> newHashMap() {
		return MapUtil.newHashMap();
	}

	public static <K, V> HashMap<K, V> newHashMap(int size, boolean isLinked) {
		return MapUtil.newHashMap(size, isLinked);
	}

	public static <K, V> HashMap<K, V> newHashMap(int size) {
		return MapUtil.newHashMap(size);
	}

	public static <K, V> HashMap<K, V> newHashMap(boolean isLinked) {
		return MapUtil.newHashMap(isLinked);
	}

	public static <K, V> HashMap<K, V> of(K key, V value) {
		return MapUtil.of(key, value);
	}

	public static <K, V> HashMap<K, V> of(K key, V value, boolean isOrder) {
		return MapUtil.of(key, value, isOrder);
	}

}
