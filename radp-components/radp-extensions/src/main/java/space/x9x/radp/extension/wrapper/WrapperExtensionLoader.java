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

    public Set<Class<?>> getCachedWrapperClasses() {
        return cachedWrapperClasses;
    }

    public void cacheWrapperClass(Class<?> clazz) {
        if (cachedWrapperClasses == null) {
            cachedWrapperClasses = new HashSet<>();
        }
        cachedWrapperClasses.add(clazz);
    }
}
