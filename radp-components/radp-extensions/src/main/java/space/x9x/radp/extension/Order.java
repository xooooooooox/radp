package space.x9x.radp.extension;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 扩展点顺序, 相同扩展点名称根据 {@code Order} 优先排序
 *
 * @author IO x9x
 * @since 2024-09-24 23:47
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Order {
    /**
     * Constant indicating the highest precedence that can be assigned to an extension.
     * Extensions with this precedence value will be processed first.
     */
    int HIGHEST_PRECEDENCE = Integer.MIN_VALUE;

    /**
     * Constant indicating the lowest precedence that can be assigned to an extension.
     * Extensions with this precedence value will be processed last.
     */
    int LOWEST_PRECEDENCE = Integer.MAX_VALUE;

    /**
     * Specifies the order value for the annotated extension.
     * Lower values have higher priority. The default value is 0.
     *
     * @return the order value
     */
    int value() default 0;
}
