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

package space.x9x.radp.spring.data.mybatis.support;

import java.lang.reflect.Array;
import java.util.IdentityHashMap;
import java.util.Map;

import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.reflection.MetaObject;

import org.springframework.util.Assert;

/**
 * Small helper to unwrap MyBatis parameter containers and detect whether a particular
 * entity type participates in the current statement. Reuses across auto-fill and SQL
 * rewrite components to keep the heuristics in one place.
 *
 * @since 2025-01-28
 */
public final class MybatisEntityResolver {

	private static final int MAX_SCAN = 64;

	private static final String[] ENTITY_KEYS = { Constants.ENTITY, "entity", "param1", "arg0", "record" };

	private MybatisEntityResolver() {
	}

	/**
	 * Resolves the primary entity from a MyBatis {@link MetaObject} or parameter
	 * container. Unwraps common containers (ParamMap, Map, Iterable, array) to locate the
	 * entity value.
	 * @param metaObject myBatis meta object; may be null
	 * @return the resolved entity object, or null if none found
	 */
	public static Object resolve(MetaObject metaObject) {
		if (metaObject == null) {
			return null;
		}
		return unwrap(metaObject.getOriginalObject());
	}

	/**
	 * Checks whether the given parameter root (possibly a MyBatis container) contains an
	 * instance of the given type. Traverses common containers with a depth guard to avoid
	 * excessive scanning.
	 * @param root parameter root object; may be null
	 * @param type target type to search for (must not be null)
	 * @return true if an instance of {@code type} is found; false otherwise
	 */
	public static boolean containsType(Object root, Class<?> type) {
		Assert.notNull(type, "type must not be null");
		return containsType(root, type, new IdentityHashMap<>());
	}

	private static Object unwrap(Object candidate) {
		if (candidate instanceof MetaObject) {
			MetaObject metaObject = (MetaObject) candidate;
			return unwrap(metaObject.getOriginalObject());
		}
		if (candidate instanceof MapperMethod.ParamMap) {
			MapperMethod.ParamMap<?> paramMap = (MapperMethod.ParamMap<?>) candidate;
			for (String key : ENTITY_KEYS) {
				if (paramMap.containsKey(key)) {
					Object value = paramMap.get(key);
					if (value != null) {
						return unwrap(value);
					}
				}
			}
		}
		if (candidate instanceof Map) {
			Map<?, ?> map = (Map<?, ?>) candidate;
			for (String key : ENTITY_KEYS) {
				if (map.containsKey(key)) {
					Object value = map.get(key);
					if (value != null) {
						return unwrap(value);
					}
				}
			}
		}
		if (candidate instanceof Iterable) {
			Iterable<?> iterable = (Iterable<?>) candidate;
			for (Object value : iterable) {
				if (value != null) {
					return value;
				}
			}
		}
		if (candidate != null && candidate.getClass().isArray()) {
			int length = Array.getLength(candidate);
			if (length > 0) {
				return Array.get(candidate, 0);
			}
		}
		return candidate;
	}

	private static boolean containsType(Object root, Class<?> type, Map<Object, Boolean> visited) {
		if (root == null) {
			return false;
		}
		if (type.isInstance(root)) {
			return true;
		}
		if (visited.put(root, Boolean.TRUE) != null) {
			return false;
		}
		if (root instanceof MapperMethod.ParamMap) {
			MapperMethod.ParamMap<?> paramMap = (MapperMethod.ParamMap<?>) root;
			for (String key : ENTITY_KEYS) {
				if (paramMap.containsKey(key) && containsType(paramMap.get(key), type, visited)) {
					return true;
				}
			}
			return false;
		}
		if (root instanceof Map) {
			Map<?, ?> map = (Map<?, ?>) root;
			for (String key : ENTITY_KEYS) {
				if (map.containsKey(key) && containsType(map.get(key), type, visited)) {
					return true;
				}
			}
			return false;
		}
		if (root instanceof Iterable) {
			Iterable<?> iterable = (Iterable<?>) root;
			int scanned = 0;
			for (Object element : iterable) {
				if (containsType(element, type, visited)) {
					return true;
				}
				if (++scanned >= MAX_SCAN) {
					break;
				}
			}
			return false;
		}
		if (root.getClass().isArray()) {
			int len = Math.min(Array.getLength(root), MAX_SCAN);
			for (int i = 0; i < len; i++) {
				if (containsType(Array.get(root, i), type, visited)) {
					return true;
				}
			}
			return false;
		}
		return false;
	}

}
