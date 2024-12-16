package com.x9x.radp.extension.strategy;

import com.x9x.radp.commons.lang.ArrayUtils;

import java.util.Arrays;
import java.util.List;
import java.util.ServiceLoader;
import java.util.stream.StreamSupport;

/**
 * LoadingStrategy 容器
 *
 * @author x9x
 * @since 2024-09-24 19:56
 */
public class LoadingStrategyHolder {

    /**
     * 策略数组，存储所有加载策略
     */
    public static volatile LoadingStrategy[] strategies = loadLoadingStrategies();

    public static LoadingStrategy[] loadLoadingStrategies() {
        return StreamSupport.stream(ServiceLoader.load(LoadingStrategy.class).spliterator(), false)
                .sorted()
                .toArray(LoadingStrategy[]::new);
    }

    public static List<LoadingStrategy> getLoadingStrategies() {
        return Arrays.asList(strategies);
    }

    public static void setLoadingStrategies(LoadingStrategy... strategies) {
        if (ArrayUtils.isNotEmpty(strategies)) {
            LoadingStrategyHolder.strategies = strategies;
        }
    }
}
