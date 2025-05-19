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
     * Gets the directory path for loading resources.
     * This method defines where the extension loader should look for extension resources.
     *
     * @return the directory path string for resource loading
     */
    String directory();

    /**
     * Determines whether to prefer using the extension class loader over the current thread's context class loader.
     * When true, the extension system will prioritize using the extension's own class loader.
     *
     * @return true if the extension class loader should be preferred, false otherwise
     */
    default boolean preferExtensionClassLoader() {
        return false;
    }

    /**
     * Gets an array of package names that should be excluded from extension loading.
     * Extensions in these packages will not be loaded by the extension system.
     *
     * @return an array of package names to exclude, or null if no packages should be excluded
     */
    default String[] excludedPackages() {
        return null;
    }

    /**
     * Determines whether this loading strategy should override existing strategies.
     * When true, this strategy will take precedence over other strategies with the same directory.
     *
     * @return true if this strategy should override others, false otherwise
     */
    default boolean overridden() {
        return false;
    }
}
