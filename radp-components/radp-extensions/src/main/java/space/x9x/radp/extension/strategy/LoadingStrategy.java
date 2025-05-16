package space.x9x.radp.extension.strategy;

/**
 * 扩展点加载策略
 * 定义了一种加载策略接口，该接口主要用于确定资源加载的目录，
 * 以及提供一些默认的加载行为配置选项。
 * 该接口扩展了Prioritized接口，意味着加载策略也可以被赋予一定的优先级。
 *
 * @author x9x
 * @since 2024-09-24 19:40
 */
public interface LoadingStrategy extends Prioritized {

    String directory();

    /**
     * Determines whether to prefer using the extension class loader.
     * When true,
     * the extension will be loaded using the extension class loader instead of the default class loader.
     *
     * @return true if the extension class loader should be preferred, false otherwise
     */
    default boolean preferExtensionClassLoader() {
        return false;
    }

    default String[] excludedPackages() {
        return null;
    }

    default boolean overridden() {
        return false;
    }
}
