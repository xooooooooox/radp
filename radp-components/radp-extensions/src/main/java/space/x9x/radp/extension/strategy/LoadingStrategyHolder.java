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
     * Loads all available loading strategies using the Java ServiceLoader mechanism.
     * This method discovers all implementations of the LoadingStrategy interface
     * on the classpath, sorts them according to their priority, and returns them as an array.
     *
     * @return an array of discovered and sorted LoadingStrategy implementations
     */
    public static LoadingStrategy[] loadLoadingStrategies() {
        return StreamSupport.stream(ServiceLoader.load(LoadingStrategy.class).spliterator(), false)
                .sorted()
                .toArray(LoadingStrategy[]::new);
    }

    /**
     * Gets the current list of loading strategies.
     * This method returns the currently active loading strategies as an unmodifiable list.
     *
     * @return a list containing all currently active loading strategies
     */
    public static List<LoadingStrategy> getLoadingStrategies() {
        return Arrays.asList(strategies);
    }

    /**
     * Sets the loading strategies to be used by the extension system.
     * This method replaces the current set of strategies with the provided ones.
     * If the provided array is empty, no change will be made.
     *
     * @param strategies the loading strategies to set
     */
    public static void setLoadingStrategies(LoadingStrategy... strategies) {
        if (ArrayUtils.isNotEmpty(strategies)) {
            LoadingStrategyHolder.strategies = strategies;
        }
    }
}
