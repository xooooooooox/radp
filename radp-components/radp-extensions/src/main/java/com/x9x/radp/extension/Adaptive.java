package com.x9x.radp.extension;

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
    String[] value() default {};
}
