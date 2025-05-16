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
     * Constructs a new AdaptiveExtensionFactory.
     * This constructor initializes the factory by loading all available ExtensionFactory
     * implementations using the ExtensionLoader mechanism. It creates an unmodifiable list
     * of these factories, which will be used to delegate extension lookup requests.
     * The factories are queried in the order they were loaded, with the first factory
     * that returns a non-null extension being used.
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
