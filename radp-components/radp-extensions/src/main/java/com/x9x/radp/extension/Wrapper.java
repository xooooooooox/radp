package com.x9x.radp.extension;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 扩展点包装器
 *
 * @author x9x
 * @since 2024-09-24 13:55
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Wrapper {
    String[] matches() default {};

    String[] mismatches() default {};
}
