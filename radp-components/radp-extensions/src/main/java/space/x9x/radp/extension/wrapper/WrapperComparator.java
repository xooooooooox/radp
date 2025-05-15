package space.x9x.radp.extension.wrapper;

import space.x9x.radp.extension.active.ActivateComparator;

import java.util.Comparator;

/**
 * 针对 @Wrapper 排序
 * <p>
 * Comparator for sorting elements annotated with @Wrapper.
 * This class extends ActivateComparator to provide specialized sorting
 * for extension points that use the Wrapper annotation.
 *
 * @author x9x
 * @since 2024-09-24 13:59
 */
public class WrapperComparator extends ActivateComparator {
    /**
     * Static instance of the WrapperComparator for convenient use.
     * This comparator can be used to sort objects annotated with @Wrapper.
     */
    public static final Comparator<Object> COMPARATOR = new WrapperComparator();
}
