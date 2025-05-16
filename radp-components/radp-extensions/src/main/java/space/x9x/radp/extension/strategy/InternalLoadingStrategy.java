package space.x9x.radp.extension.strategy;

/**
 * 内部扩展点加载策略
 *
 * @author x9x
 * @since 2024-09-24 19:54
 */
public class InternalLoadingStrategy implements LoadingStrategy {

    /**
     * Directory path for internal extensions.
     * This constant defines the location where internal extension configuration files are stored.
     * Internal extensions are loaded from this directory during the extension loading process.
     */
    public static final String META_INF = "META-INF/internal/";

    @Override
    public String directory() {
        return META_INF;
    }

    @Override
    public int getPriority() {
        return MAX_PRIORITY;
    }
}
