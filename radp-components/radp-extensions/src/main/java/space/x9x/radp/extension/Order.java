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
    int HIGHEST_PRECEDENCE = Integer.MIN_VALUE;
    int LOWEST_PRECEDENCE = Integer.MAX_VALUE;

    int value() default 0;
}
