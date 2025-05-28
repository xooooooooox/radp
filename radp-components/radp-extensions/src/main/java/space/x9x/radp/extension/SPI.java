package space.x9x.radp.extension;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 这是一个用于标识SPI（Service Provider Interface）的注解。
 * 它的主要作用是标记一个接口为SPI接口，以便于Java运行时环境可以找到并加载相应的实现类。 该注解会被文档工具记录，并在运行时保留，允许通过反射机制获取。
 *
 * @author IO x9x
 * @since 2024-09-24 11:20
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SPI {

    /**
     * Specifies the name of the SPI implementation.
     * If not specified, the default implementation will be used.
     *
     * @return the name of the SPI implementation
     */
    String value() default "";
}
