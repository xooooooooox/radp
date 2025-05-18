package space.x9x.radp.extension;

import java.lang.annotation.*;

/**
 * 激活扩展点
 *
 * @author x9x
 * @since 2024-09-24 12:49
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Activate {
    /**
     * The groups to which this extension belongs.
     *
     * @return an array of group names
     */
    String[] groups() default {};

    /**
     * The values used for activation matching.
     *
     * @return an array of activation values
     */
    String[] value() default {};

    /**
     * The priority order of the extension when multiple extensions are found.
     * Extensions with lower order values have higher priority.
     *
     * @return the order value
     */
    int order() default 0;
}
