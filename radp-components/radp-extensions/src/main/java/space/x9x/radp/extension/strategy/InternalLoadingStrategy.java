package space.x9x.radp.extension.strategy;

/**
 * 内部扩展点加载策略
 *
 * @author x9x
 * @since 2024-09-24 19:54
 */
public class InternalLoadingStrategy implements LoadingStrategy {

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
