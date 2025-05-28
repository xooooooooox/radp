package space.x9x.radp.spring.framework.logging;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

import space.x9x.radp.spring.framework.logging.bootstrap.config.BootstrapLogConfiguration;

/**
 * @author IO x9x
 * @since 2024-09-30 09:53
 */
@Import(BootstrapLogConfiguration.class)
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EnableBootstrapLog {
}
