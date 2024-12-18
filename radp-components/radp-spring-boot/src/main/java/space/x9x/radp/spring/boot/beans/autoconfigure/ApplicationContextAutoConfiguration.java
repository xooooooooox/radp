package space.x9x.radp.spring.boot.beans.autoconfigure;

import space.x9x.radp.spring.framework.beans.ApplicationContextHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Role;

/**
 * @author x9x
 * @since 2024-09-27 21:14
 */
@Slf4j
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@AutoConfiguration
public class ApplicationContextAutoConfiguration {
    private static final String AUTOWIRED_APPLICATION_CONTEXT_HELPER = "Autowired ApplicationContextHelper";

    @ConditionalOnMissingBean(ApplicationContextHelper.class)
    @Bean
    public ApplicationContextHelper applicationContextHelper() {
        log.debug(AUTOWIRED_APPLICATION_CONTEXT_HELPER);
        return new ApplicationContextHelper();
    }
}
