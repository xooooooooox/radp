package space.x9x.radp.spring.boot.logging.autoconfigure;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Role;

import space.x9x.radp.spring.boot.bootstrap.constants.Conditions;
import space.x9x.radp.spring.boot.logging.env.BootstrapLogProperties;
import space.x9x.radp.spring.framework.logging.EnableBootstrapLog;

/**
 * @author IO x9x
 * @since 2024-09-30 11:37
 */
@ConditionalOnProperty(
        prefix = BootstrapLogProperties.PREFIX,
        name = Conditions.ENABLED,
        havingValue = Conditions.TRUE,
        matchIfMissing = true
)
@EnableConfigurationProperties(BootstrapLogProperties.class)
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@AutoConfiguration
@EnableBootstrapLog
public class BootstrapLogAutoConfiguration {
}
