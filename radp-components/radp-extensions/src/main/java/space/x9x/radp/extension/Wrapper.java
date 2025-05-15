package space.x9x.radp.extension;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 扩展点包装器
 * <p>
 * Extension point wrapper annotation.
 * This annotation is used to mark classes that wrap or extend other extension points.
 * It allows specifying which extension points should be matched or excluded.
 *
 * @author x9x
 * @since 2024-09-24 13:55
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Wrapper {
    /**
     * Specifies the extension points that this wrapper should match.
     * Only extension points that match these patterns will be wrapped.
     *
     * @return an array of patterns to match extension points
     */
    String[] matches() default {};

    /**
     * Specifies the extension points that this wrapper should not match.
     * Extension points that match these patterns will be excluded from wrapping.
     *
     * @return an array of patterns to exclude extension points
     */
    String[] mismatches() default {};
}
