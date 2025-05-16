package space.x9x.radp.extension.strategy;

import space.x9x.radp.commons.lang.ArrayUtils;

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

    /**
     * Loads all available LoadingStrategy implementations using Java's ServiceLoader mechanism.
     * The strategies are sorted according to their priority.
     *
     * @return an array of sorted LoadingStrategy instances
     */
    public static LoadingStrategy[] loadLoadingStrategies() {
        return StreamSupport.stream(ServiceLoader.load(LoadingStrategy.class).spliterator(), false)
                .sorted()
                .toArray(LoadingStrategy[]::new);
    }

    /**
     * Returns a list of all currently loaded LoadingStrategy instances.
     *
     * @return a list containing all the LoadingStrategy instances
     */
    public static List<LoadingStrategy> getLoadingStrategies() {
        return Arrays.asList(strategies);
    }

    /**
     * Sets the loading strategies to be used by the application.
     * If the provided array is empty, no change will be made.
     *
     * @param strategies the LoadingStrategy instances to be set
     */
    public static void setLoadingStrategies(LoadingStrategy... strategies) {
        if (ArrayUtils.isNotEmpty(strategies)) {
            LoadingStrategyHolder.strategies = strategies;
        }
    }
}
