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
     * Specifies the keys or names for which this adaptive implementation should be used.
     * When multiple implementations of an extension point exist, the one with matching keys
     * in this value array will be selected based on the runtime context.
     * If not specified (an empty array), this implementation will be used as a fallback
     * when no other implementation matches the context.
     *
     * @return an array of keys or names for adaptive selection
     */
    String[] value() default {};
}
