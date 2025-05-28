package space.x9x.radp.spring.test.embedded.extension;

import space.x9x.radp.extension.strategy.LoadingStrategy;

/**
 * Loading strategy for embedded server extensions. This class defines how embedded server
 * implementations are loaded from the classpath. It specifies the directory where
 * extension definitions are located and the priority of this loading strategy.
 *
 * @author IO x9x
 * @since 2024-10-13 17:44
 */
public class EmbeddedServerLoadingStrategy implements LoadingStrategy {

    /**
     * The directory path where embedded server extension definitions are located.
     * This path is relative to the classpath root.
     */
    public static final String META_INF = "META-INF/embedded-server/";

    /**
     * Returns the directory path where embedded server extension definitions are located.
     *
     * @return the directory path for extension definitions
     */
    @Override
    public String directory() {
        return META_INF;
    }

    /**
     * Returns the priority of this loading strategy.
     * Higher priority strategies are processed first.
     *
     * @return the maximum priority value
     */
    @Override
    public int getPriority() {
        return MAX_PRIORITY;
    }
}
