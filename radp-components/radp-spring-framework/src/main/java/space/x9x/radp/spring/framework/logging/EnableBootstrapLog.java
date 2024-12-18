package space.x9x.radp.spring.framework.logging;

import space.x9x.radp.spring.framework.logging.bootstrap.config.BootstrapLogConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author x9x
 * @since 2024-09-30 09:53
 */
@Import(BootstrapLogConfiguration.class)
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EnableBootstrapLog {
}
