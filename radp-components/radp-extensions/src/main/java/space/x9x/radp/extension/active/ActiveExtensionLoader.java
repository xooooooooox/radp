package space.x9x.radp.extension.active;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import space.x9x.radp.commons.collections.CollectionUtils;
import space.x9x.radp.commons.lang.StringUtils;
import space.x9x.radp.extension.Activate;
import space.x9x.radp.extension.ExtensionLoader;
import space.x9x.radp.extension.common.Constants;
import space.x9x.radp.extension.common.URL;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author x9x
 * @since 2024-09-24 12:46
 */
@RequiredArgsConstructor
@Slf4j
public class ActiveExtensionLoader<T> {

    public static final String REMOVE_VALUE_PREFIX = "-";
    public static final String DEFAULT_KEY = "default";

    /**
     * 缓存激活的扩展点
     */
    private final Map<String, Object> cachedActives = new ConcurrentHashMap<>();

    private final ExtensionLoader<T> extensionLoader;

    public void cacheActiveClass(Class<?> clazz, String name) {
        Activate activate = clazz.getAnnotation(Activate.class);
        if (activate != null) {
            cachedActives.put(name, activate);
        }
    }

    public List<T> getActivateExtension(URL url, String[] values, String group) {
        List<T> activateExtensions = new ArrayList<>();
        TreeMap<Class<?>, T> activateExtensionsMap = new TreeMap<>(ActivateComparator.COMPARATOR);
        Set<String> loadedNames = new HashSet<>();
        Set<String> names = CollectionUtils.ofSet(values);
        if (!names.contains(REMOVE_VALUE_PREFIX + DEFAULT_KEY)) {
            extensionLoader.getExtensionClasses();
            for (Map.Entry<String, Object> entry : cachedActives.entrySet()) {
                String name = entry.getKey();
                Object activate = entry.getValue();
                String[] activateGroup;
                String[] activateValue;
                if (activate instanceof Activate) {
                    activateGroup = ((Activate) activate).groups();
                    activateValue = ((Activate) activate).value();
                } else {
                    continue;
                }
                if (isMatchGroup(group, activateGroup)
                        && !names.contains(name)
                        && !names.contains(REMOVE_VALUE_PREFIX + name)
                        && isActive(activateValue, url)
                        && !loadedNames.contains(name)) {
                    activateExtensionsMap.put(extensionLoader.getExtensionClass(name),
                            extensionLoader.getExtension(name));
                    loadedNames.add(name);
                }
            }
            if (!activateExtensionsMap.isEmpty()) {
                activateExtensions.addAll(activateExtensionsMap.values());
            }
        }
        List<T> loadedExtensions = new ArrayList<>();
        for (String name : names) {
            if (!name.startsWith(REMOVE_VALUE_PREFIX)
                    && names.contains(REMOVE_VALUE_PREFIX + name)) {
                if (!loadedNames.contains(name)) {
                    if (DEFAULT_KEY.equals(name)) {
                        if (!loadedExtensions.isEmpty()) {
                            activateExtensions.addAll(0, loadedExtensions);
                            loadedExtensions.clear();
                        }
                    } else {
                        loadedExtensions.add(extensionLoader.getExtension(name));
                    }
                    loadedNames.add(name);
                }
            } else {
                String simpleName = extensionLoader.getExtensionClass(name).getSimpleName();
                log.warn("Catch duplicated filter, ExtensionLoader will ignore one of them. Please check. Filter Name: {}, Ignored Class Name: {}", name, simpleName);
            }
        }
        if (!loadedExtensions.isEmpty()) {
            activateExtensions.addAll(loadedExtensions);
        }
        return activateExtensions;
    }

    public List<T> getActivateExtension(URL url, String key) {
        return getActivateExtension(url, key, null);
    }

    /**
     * Gets a list of activated extensions based on the URL parameter value for the specified key and group.
     * This method retrieves the parameter value from the URL using the provided key,
     * splits it by comma, and then returns the activated extensions that match the criteria.
     *
     * @param url   the URL containing parameters for extension activation
     * @param key   the parameter key to look up in the URL
     * @param group the group to filter extensions by, can be null for no filtering
     * @return a list of activated extension instances that match the criteria
     */
    public List<T> getActivateExtension(URL url, String key, String group) {
        String value = url.getParameter(key);
        return getActivateExtension(url, StringUtils.isEmpty(value) ? null :
                Constants.COMMA_SPLIT_PATTERN.split(value), group);
    }

    public List<T> getActivateExtension(URL url, String[] values) {
        return getActivateExtension(url, values, null);
    }

    private boolean isMatchGroup(String group, String[] groups) {
        if (StringUtils.isEmpty(group)) {
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
