package space.x9x.radp.extension;

import java.lang.annotation.*;

/**
 * 扩展点顺序, 相同扩展点名称根据 {@code Order} 优先排序
 *
 * @author x9x
 * @since 2024-09-24 23:47
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Order {
    /**
     * Constant representing the highest possible precedence value.
     * Extensions with this precedence value will be processed first.
     */
    int HIGHEST_PRECEDENCE = Integer.MIN_VALUE;

    /**
     * Constant representing the lowest possible precedence value.
     * Extensions with this precedence value will be processed last.
     */
    int LOWEST_PRECEDENCE = Integer.MAX_VALUE;

    /**
     * Specifies the order value for the annotated element.
     * Lower values indicate higher priority. The default value is 0.
     *
     * @return the order value
     */
    int value() default 0;
}
