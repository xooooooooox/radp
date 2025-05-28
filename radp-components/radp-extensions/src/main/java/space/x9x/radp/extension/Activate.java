package space.x9x.radp.extension;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 激活扩展点
 *
 * @author IO x9x
 * @since 2024-09-24 12:49
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Activate {
    /**
     * Specifies the groups to which this extension belongs.
     * Extensions can be activated based on their group membership.
     * If not specified, the extension does not belong to any group.
     *
     * @return an array of group names this extension belongs to
     */
    String[] groups() default {};

    /**
     * Specifies the keys or values for conditional activation.
     * Extensions can be activated when certain keys or values are present in the URL.
     * If not specified, the extension will be activated regardless of URL parameters.
     *
     * @return an array of keys or values for conditional activation
     */
    String[] value() default {};

    /**
     * Specifies the order in which extensions should be executed.
     * Extensions with lower order values are executed before those with higher values.
     * The default order is 0.
     *
     * @return the order value for this extension
     */
    int order() default 0;
}
