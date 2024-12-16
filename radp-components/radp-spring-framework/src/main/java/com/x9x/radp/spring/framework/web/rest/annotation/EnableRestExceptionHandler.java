package com.x9x.radp.spring.framework.web.rest.annotation;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 用于开启 REST 接口异常解析器自动装配
 *
 * @author x9x
 * @since 2024-09-26 23:52
 */
@Import(RestExceptionHandlerRegister.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EnableRestExceptionHandler {
}
