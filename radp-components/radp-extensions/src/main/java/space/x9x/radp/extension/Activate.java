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
    String[] groups() default {};

    String[] value() default {};

    int order() default 0;
}
