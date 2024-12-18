package space.x9x.radp.extension.wrapper;

import space.x9x.radp.extension.active.ActivateComparator;

import java.util.Comparator;

/**
 * 针对 @Wrapper 排序
 *
 * @author x9x
 * @since 2024-09-24 13:59
 */
public class WrapperComparator extends ActivateComparator {
    public static final Comparator<Object> COMPARATOR = new WrapperComparator();
}
