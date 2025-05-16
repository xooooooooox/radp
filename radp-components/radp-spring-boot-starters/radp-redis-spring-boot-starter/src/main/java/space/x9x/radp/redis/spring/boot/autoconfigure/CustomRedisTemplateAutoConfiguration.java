package space.x9x.radp.redis.spring.boot.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Role;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import space.x9x.radp.spring.data.redis.core.CustomRedisTemplate;
import space.x9x.radp.spring.data.redis.core.CustomRedisTemplateImpl;

/**
 * @author x9x
 * @since 2024-10-19 20:57
 */
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@ConditionalOnBean(StringRedisTemplate.class)
@ConditionalOnClass(RedisOperations.class)
@AutoConfiguration(after = RedisAutoConfiguration.class)
@Slf4j
public class CustomRedisTemplateAutoConfiguration {

    private static final String AUTOWIRED_CUSTOM_REDIS_TEMPLATE = "Autowired customRedisTemplate";

    /**
     * Creates and configures a CustomRedisTemplate bean.
     * This method provides a template for Redis operations with custom functionality,
     * wrapping the standard StringRedisTemplate for enhanced capabilities.
     *
     * @param stringRedisTemplate the StringRedisTemplate to use as the underlying Redis operations provider
     * @return a configured CustomRedisTemplate instance
     */
    @ConditionalOnMissingBean
    @Bean
    public CustomRedisTemplate customRedisTemplate(StringRedisTemplate stringRedisTemplate) {
        log.debug(AUTOWIRED_CUSTOM_REDIS_TEMPLATE);
        return new CustomRedisTemplateImpl(stringRedisTemplate);
    }
}
