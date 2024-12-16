package com.x9x.radp.extension.strategy;

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
