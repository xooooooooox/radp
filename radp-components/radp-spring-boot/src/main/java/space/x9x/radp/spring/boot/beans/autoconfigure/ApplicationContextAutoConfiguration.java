package space.x9x.radp.spring.boot.beans.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Role;
import space.x9x.radp.spring.framework.beans.ApplicationContextHelper;

/**
 * @author x9x
 * @since 2024-09-27 21:14
 */
@Slf4j
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@AutoConfiguration
public class ApplicationContextAutoConfiguration {
    private static final String AUTOWIRED_APPLICATION_CONTEXT_HELPER = "Autowired ApplicationContextHelper";

    /**
     * Creates and configures an ApplicationContextHelper bean.
     * This helper provides convenient access to the Spring application context and its beans.
     * Only created if no other ApplicationContextHelper bean is defined in the context.
     *
     * @return a new ApplicationContextHelper instance
     */
    @ConditionalOnMissingBean(ApplicationContextHelper.class)
    @Bean
    public ApplicationContextHelper applicationContextHelper() {
        log.debug(AUTOWIRED_APPLICATION_CONTEXT_HELPER);
        return new ApplicationContextHelper();
    }
}
