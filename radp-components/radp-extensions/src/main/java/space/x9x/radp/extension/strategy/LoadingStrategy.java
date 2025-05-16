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

    /**
     * Gets the directory path where extension configuration files are located.
     * This method returns the base directory path that will be used to load extension configurations.
     * Each implementation of LoadingStrategy can specify a different directory.
     *
     * @return the directory path for loading extension configurations
     */
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

    /**
     * Gets an array of package names that should be excluded from extension loading.
     * Extensions from these packages will not be loaded even if they are found in the extension directory.
     * The default implementation returns null, meaning no packages are excluded.
     *
     * @return an array of package names to exclude, or null if no packages should be excluded
     */
    default String[] excludedPackages() {
        return null;
    }

    /**
     * Determines whether extensions loaded by this strategy can override existing ones.
     * When true, if an extension with the same name already exists, it will be replaced by the one
     * loaded by this strategy. When false, existing extensions will not be overridden.
     * The default implementation returns false, meaning existing extensions are not overridden.
     *
     * @return true if extensions loaded by this strategy can override existing ones, false otherwise
     */
    default boolean overridden() {
        return false;
    }
}
