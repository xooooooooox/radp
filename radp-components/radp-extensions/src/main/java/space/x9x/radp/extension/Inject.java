package space.x9x.radp.extension;

import java.lang.annotation.*;

/**
 * Annotation for injecting extension instances into fields of other extensions.
 * This annotation can be applied to methods (typically setter methods) to indicate
 * that the parameter should be injected with an extension instance.
 *
 * @author x9x
 * @since 2024-09-24 16:32
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Inject {
    /**
     * Controls whether injection is enabled for the annotated element.
     *
     * @return true if injection is enabled, false otherwise
     */
    boolean enable() default true;

    /**
     * Specifies the type of injection to be used.
     *
     * @return the injection type, default is BY_NAME
     */
    InjectType type() default InjectType.BY_NAME;

    /**
     * Enum defining the types of injection that can be used.
     */
    enum InjectType {
        /**
         * Inject by name, using the property name to look up the extension.
         */
        BY_NAME,

        /**
         * Inject by type, using only the parameter type to look up the extension.
         */
        BY_TYPE
    }
}
