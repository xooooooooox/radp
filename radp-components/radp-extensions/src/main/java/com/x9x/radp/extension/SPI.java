package com.x9x.radp.extension;

import java.lang.annotation.*;

/**
 * 这是一个用于标识SPI（Service Provider Interface）的注解。
 * 它的主要作用是标记一个接口为SPI接口，以便于Java运行时环境可以找到并加载相应的实现类。
 * 该注解会被文档工具记录，并在运行时保留，允许通过反射机制获取。
 *
 * @author x9x
 * @since 2024-09-24 11:20
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SPI {

    String value() default "";
}
