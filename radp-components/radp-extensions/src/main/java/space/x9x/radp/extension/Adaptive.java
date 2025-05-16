package space.x9x.radp.extension;

import java.lang.annotation.*;

/**
 * 默认扩展实现标记
 *
 * @author x9x
 * @since 2024-09-24 13:57
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Adaptive {
    /**
     * The keys to be used for getting the adaptive extension.
     *
     * @return An array of keys
     */
    String[] value() default {};
}
