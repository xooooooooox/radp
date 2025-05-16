package space.x9x.radp.extension.strategy;

import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

/**
 * @author x9x
 * @since 2024-09-24 19:41
 */
public interface Prioritized extends Comparable<Prioritized> {

    /**
     * Constant indicating the maximum priority value.
     * Objects with this priority will be processed last.
     */
    int MAX_PRIORITY = Integer.MAX_VALUE;

    /**
     * Constant indicating the minimum priority value.
     * Objects with this priority will be processed first.
     */
    int MIN_PRIORITY = Integer.MIN_VALUE;

    /**
     * Constant indicating the normal (default) priority value.
     * Objects with this priority will be processed in the middle.
     */
    int NORMAL_PRIORITY = 0;

    /**
     * Comparator for comparing objects based on their priority.
     * This comparator handles the following cases:
     * - If only one object is a Prioritized instance, it gets higher priority
     * - If both objects are Prioritized instances, they are compared using their compareTo method
     * - If neither object is a Prioritized instance, they are considered equal
     */
    Comparator<Object> COMPARATOR = (one, two) -> {
        // 判断两个对象是否为Prioritized的实例
        boolean b1 = one instanceof Prioritized;
        boolean b2 = two instanceof Prioritized;
        // 如果第一个对象是Prioritized实例而第二个不是，返回-1，表示第一个优先级更高
        if (b1 && !b2) {
            return -1;
        }
        // 如果第二个对象是Prioritized实例而第一个不是，返回1，表示第二个优先级更高
        if (b2 && !b1) {
            return 1;
        }
        // 如果两个对象都是Prioritized实例，根据它们的compareTo方法返回结果
        if (b1) {
            return ((Prioritized) one).compareTo((Prioritized) two);
        }
        // 如果两个对象都不是Prioritized实例，或者上述条件都不满足，返回0
        return 0;
    };

    /**
     * 获取当前对象的优先级
     * 默认实现返回NORMAL_PRIORITY，意味着未特殊指定优先级的默认为正常优先级
     *
     * @return 当前对象的优先级
     */
    default int getPriority() {
        return NORMAL_PRIORITY;
    }

    /**
     * 比较当前对象与另一个Prioritized对象的优先级
     * 重写了Comparable接口的compareTo方法，基于优先级进行比较
     *
     * @param that 要比较的Prioritized对象
     * @return 比较结果，-1表示this优先级较低，0表示优先级相同，1表示this优先级较高
     */
    @Override
    default int compareTo(@NotNull Prioritized that) {
        return Integer.compare(this.getPriority(), that.getPriority());
    }
}
