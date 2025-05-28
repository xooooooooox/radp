package space.x9x.radp.spring.framework.logging;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;

import space.x9x.radp.spring.framework.logging.access.config.AccessLogImportSelector;

/**
 * @author IO x9x
 * @since 2024-09-30 09:53
 */
@Import(AccessLogImportSelector.class)
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EnableAccessLog {

    /**
     * @return 是否开启 CGLIB 代理
     */
    boolean proxyTargetClass() default false;

    /**
     * @return 代理模式。默认 JDK 动态代理
     */
    AdviceMode mode() default AdviceMode.PROXY;

    /**
     * @return PointcutAdvisor 优先级
     */
    int order() default Ordered.LOWEST_PRECEDENCE;

    /**
     * @return AspectJ 切面表达式
     */
    String expression() default "";
}
