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

package space.x9x.radp.extension.active;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import space.x9x.radp.commons.collections.CollectionUtils;
import space.x9x.radp.commons.lang.StringUtil;
import space.x9x.radp.extension.Activate;
import space.x9x.radp.extension.ExtensionLoader;
import space.x9x.radp.extension.common.Constants;
import space.x9x.radp.extension.common.URL;

/**
 * 激活扩展点加载器，负责加载和管理带有 @Activate 注解的扩展点.
 * <p>
 * Active extension loader responsible for loading and managing extensions with
 * the @Activate annotation. This class provides functionality to activate extensions
 * based on specified criteria such as groups and values, and to sort them according to
 * their activation order.
 *
 * @param <T> the type of extension this loader handles
 * @author RADP x9x
 * @since 2024-09-24 12:46
 */
@RequiredArgsConstructor
@Slf4j
public class ActiveExtensionLoader<T> {

	/**
	 * Prefix used to indicate that an extension should be removed from the activation
	 * list. When a value in the activation list starts with this prefix, the extension
	 * with the name that follows the prefix will be excluded from activation.
	 */
	public static final String REMOVE_VALUE_PREFIX = "-";

	/**
	 * Default key used for extension activation. This constant is used as a fallback when
	 * no specific key is provided, and for special handling in the activation process.
	 */
	public static final String DEFAULT_KEY = "default";

	/**
	 * 缓存激活的扩展点.
	 * <p>
	 * cache for activated extensions.
	 */
	private final Map<String, Object> cachedActives = new ConcurrentHashMap<>();

	/**
	 * The extension loader instance that this active extension loader works with. This
	 * field holds a reference to the parent extension loader that manages all extensions
	 * of type T.
	 */
	private final ExtensionLoader<T> extensionLoader;

	/**
	 * Caches a class with the Activate annotation. This method checks if the provided
	 * class has the Activate annotation, and if so, stores it in the cache with the
	 * specified name.
	 * @param clazz the class to check for the Activate annotation
	 * @param name the name under which to cache the class
	 */
	public void cacheActiveClass(Class<?> clazz, String name) {
		Activate activate = clazz.getAnnotation(Activate.class);
		if (activate != null) {
			this.cachedActives.put(name, activate);
		}
	}

	/**
	 * Gets a list of activated extensions based on the specified URL, values, and group.
	 * This is the main implementation method that handles the complex logic of extension
	 * activation. It processes the values array to determine which extensions should be
	 * activated, taking into account the group filter and any exclusions indicated by the
	 * REMOVE_VALUE_PREFIX. Extensions are sorted according to their order and returned as
	 * a list.
	 * @param url the URL containing parameters for extension activation
	 * @param values the array of values to use for extension activation, may contain
	 * exclusions
	 * @param group the group to filter extensions by, can be null for no filtering
	 * @return a list of activated extension instances that match the criteria
	 */
	public List<T> getActivateExtension(URL url, String[] values, String group) {
		List<T> activateExtensions = new ArrayList<>();
		TreeMap<Class<?>, T> activateExtensionsMap = new TreeMap<>(ActivateComparator.COMPARATOR);
		Set<String> loadedNames = new HashSet<>();
		Set<String> names = CollectionUtils.ofSet(values);
		if (!names.contains(REMOVE_VALUE_PREFIX + DEFAULT_KEY)) {
			this.extensionLoader.getExtensionClasses();
			for (Map.Entry<String, Object> entry : this.cachedActives.entrySet()) {
				String name = entry.getKey();
				Object activate = entry.getValue();
				String[] activateGroup;
				String[] activateValue;
				if (activate instanceof Activate) {
					activateGroup = ((Activate) activate).groups();
					activateValue = ((Activate) activate).value();
				}
				else {
					continue;
				}
				if (isMatchGroup(group, activateGroup) && !names.contains(name)
						&& !names.contains(REMOVE_VALUE_PREFIX + name) && isActive(activateValue, url)
						&& !loadedNames.contains(name)) {
					activateExtensionsMap.put(this.extensionLoader.getExtensionClass(name),
							this.extensionLoader.getExtension(name));
					loadedNames.add(name);
				}
			}
			if (!activateExtensionsMap.isEmpty()) {
				activateExtensions.addAll(activateExtensionsMap.values());
			}
		}
		List<T> loadedExtensions = new ArrayList<>();
		for (String name : names) {
			if (!name.startsWith(REMOVE_VALUE_PREFIX) && names.contains(REMOVE_VALUE_PREFIX + name)) {
				if (!loadedNames.contains(name)) {
					if (DEFAULT_KEY.equals(name)) {
						if (!loadedExtensions.isEmpty()) {
							activateExtensions.addAll(0, loadedExtensions);
							loadedExtensions.clear();
						}
					}
					else {
						loadedExtensions.add(this.extensionLoader.getExtension(name));
					}
					loadedNames.add(name);
				}
			}
			else {
				String simpleName = this.extensionLoader.getExtensionClass(name).getSimpleName();
				log.warn(
						"Catch duplicated filter, ExtensionLoader will ignore one of them. Please check. Filter Name: {}, Ignored Class Name: {}",
						name, simpleName);
			}
		}
		if (!loadedExtensions.isEmpty()) {
			activateExtensions.addAll(loadedExtensions);
		}
		return activateExtensions;
	}

	/**
	 * Gets a list of activated extensions based on the URL parameter value for the
	 * specified key. This is a convenience method that calls
	 * {@link #getActivateExtension(URL, String, String)} with a null group parameter.
	 * @param url the URL containing parameters for extension activation
	 * @param key the parameter key to look up in the URL
	 * @return a list of activated extension instances that match the criteria
	 */
	public List<T> getActivateExtension(URL url, String key) {
		return getActivateExtension(url, key, null);
	}

	/**
	 * Gets a list of activated extensions based on the URL parameter value for the
	 * specified key and group. This method retrieves the parameter value from the URL
	 * using the provided key, splits it by comma, and then returns the activated
	 * extensions that match the criteria.
	 * @param url the URL containing parameters for extension activation
	 * @param key the parameter key to look up in the URL
	 * @param group the group to filter extensions by, can be null for no filtering
	 * @return a list of activated extension instances that match the criteria
	 */
	public List<T> getActivateExtension(URL url, String key, String group) {
		String value = url.getParameter(key);
		return getActivateExtension(url, StringUtil.isEmpty(value) ? null : Constants.COMMA_SPLIT_PATTERN.split(value),
				group);
	}

	/**
	 * Gets a list of activated extensions based on the specified URL and values. This is
	 * a convenience method that calls
	 * {@link #getActivateExtension(URL, String[], String)} with a null group parameter.
	 * @param url the URL containing parameters for extension activation
	 * @param values the array of values to use for extension activation
	 * @return a list of activated extension instances that match the criteria
	 */
	public List<T> getActivateExtension(URL url, String[] values) {
		return getActivateExtension(url, values, null);
	}

	private boolean isMatchGroup(String group, String[] groups) {
		if (StringUtil.isEmpty(group)) {
			return true;
		}
		if (groups != null && groups.length > 0) {
			for (String g : groups) {
				if (group.equals(g)) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean isActive(String[] keys, URL url) {
		if (keys.length == 0) {
			return true;
		}
		for (String key : keys) {
			String keyValue = null;
			if (key.contains(":")) {
				String[] arr = key.split(":");
				key = arr[0];
				keyValue = arr[1];
			}
		}
		return false;
	}

}
