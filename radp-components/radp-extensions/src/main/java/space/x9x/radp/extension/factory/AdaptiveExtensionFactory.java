package space.x9x.radp.extension.factory;

import space.x9x.radp.extension.Adaptive;
import space.x9x.radp.extension.ExtensionFactory;
import space.x9x.radp.extension.ExtensionLoader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 自适应扩展点实例工厂
 *
 * @author x9x
 * @since 2024-09-25 10:30
 */
@Adaptive
public class AdaptiveExtensionFactory implements ExtensionFactory {

    private final List<ExtensionFactory> factories;

    /**
     * Constructor that initializes the factory with all supported extension factories.
     * It loads all extension factories using the ExtensionLoader and stores them in an unmodifiable list.
     */
    public AdaptiveExtensionFactory() {
        ExtensionLoader<ExtensionFactory> loader = ExtensionLoader.getExtensionLoader(ExtensionFactory.class);
        List<ExtensionFactory> list = new ArrayList<>();
        for (String name : loader.getSupportedExtensions()) {
            list.add(loader.getExtension(name));
        }
        factories = Collections.unmodifiableList(list);
    }

    @Override
    public <T> T getExtension(Class<T> type, String name) {
        for (ExtensionFactory factory : factories) {
            T extension = factory.getExtension(type, name);
            if (extension != null) {
                return extension;
            }
        }
        return null;
    }
}
