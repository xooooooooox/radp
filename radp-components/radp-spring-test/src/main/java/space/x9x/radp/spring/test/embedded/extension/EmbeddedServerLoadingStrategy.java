package space.x9x.radp.spring.test.embedded.extension;

import space.x9x.radp.extension.strategy.LoadingStrategy;

/**
 * @author x9x
 * @since 2024-10-13 17:44
 */
public class EmbeddedServerLoadingStrategy implements LoadingStrategy {

    public static final String META_INF = "META-INF/embedded-server/";

    @Override
    public String directory() {
        return META_INF;
    }

    @Override
    public int getPriority() {
        return MAX_PRIORITY;
    }
}
