package com.x9x.radp.spring.boot.logging.autoconfigure;

import com.x9x.radp.spring.boot.bootstrap.constants.Conditions;
import com.x9x.radp.spring.boot.logging.env.AccessLogProperties;
import com.x9x.radp.spring.framework.logging.EnableAccessLog;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Role;

/**
 * @author x9x
 * @since 2024-09-30 11:30
 */
@ConditionalOnProperty(
        prefix = AccessLogProperties.PREFIX,
        name = Conditions.ENABLED,
        havingValue = Conditions.TRUE
)
@EnableConfigurationProperties(AccessLogProperties.class)
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@AutoConfiguration
@EnableAccessLog
public class AccessLogAutoConfiguration {
}
