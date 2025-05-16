package space.x9x.radp.extension.wrapper;

import lombok.Getter;
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

    @Getter
    private Set<Class<?>> cachedWrapperClasses;


    /**
     * Caches a wrapper class for later use.
     * This method adds a class to the internal cache of wrapper classes.
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
