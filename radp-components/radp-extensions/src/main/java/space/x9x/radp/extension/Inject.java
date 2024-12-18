package space.x9x.radp.extension;

import java.lang.annotation.*;

/**
 * @author x9x
 * @since 2024-09-24 16:32
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Inject {
    boolean enable() default true;

    InjectType type() default InjectType.BY_NAME;

    enum InjectType {
        BY_NAME, BY_TYPE
    }
}
