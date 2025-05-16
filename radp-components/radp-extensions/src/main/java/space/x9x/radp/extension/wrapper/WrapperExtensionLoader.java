package space.x9x.radp.extension.wrapper;

import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.Set;

/**
 * 基于 @Wrapper 的扩展点加速器
 *
 * @author x9x
 * @since 2024-09-24 14:00
 */
@RequiredArgsConstructor
public class WrapperExtensionLoader {

    private Set<Class<?>> cachedWrapperClasses;

    /**
     * Returns the set of cached wrapper classes.
     * These are classes that have been previously registered with the loader.
     *
     * @return the set of cached wrapper classes, may be null if no classes have been cached
     */
    public Set<Class<?>> getCachedWrapperClasses() {
        return cachedWrapperClasses;
    }

    /**
     * Adds a wrapper class to the cache.
     * If the cache doesn't exist yet, it will be initialized.
     *
     * @param clazz the wrapper class to cache
     */
    public void cacheWrapperClass(Class<?> clazz) {
        if (cachedWrapperClasses == null) {
            cachedWrapperClasses = new HashSet<>();
        }
        cachedWrapperClasses.add(clazz);
    }
}
