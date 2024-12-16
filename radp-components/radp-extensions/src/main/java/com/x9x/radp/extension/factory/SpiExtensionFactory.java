package com.x9x.radp.extension.factory;

import com.x9x.radp.extension.ExtensionFactory;
import com.x9x.radp.extension.ExtensionLoader;
import com.x9x.radp.extension.SPI;

/**
 * SPI 扩展点实例工厂
 *
 * @author x9x
 * @since 2024-09-25 10:30
 */
public class SpiExtensionFactory implements ExtensionFactory {

    @Override
    public <T> T getExtension(Class<T> type, String name) {
        if (type.isInterface() && type.isAnnotationPresent(SPI.class)) {
            ExtensionLoader<T> extensionLoader = ExtensionLoader.getExtensionLoader(type);
            if (!extensionLoader.getSupportedExtensions().isEmpty()) {
                return extensionLoader.getAdaptiveExtension();
            }
        }
        return null;
    }
}
