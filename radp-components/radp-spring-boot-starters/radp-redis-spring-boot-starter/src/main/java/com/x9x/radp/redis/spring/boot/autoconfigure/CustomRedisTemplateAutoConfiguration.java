package com.x9x.radp.redis.spring.boot.autoconfigure;

import com.x9x.radp.spring.data.redis.core.CustomRedisTemplate;
import com.x9x.radp.spring.data.redis.core.CustomRedisTemplateImpl;
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

    @ConditionalOnMissingBean
    @Bean
    public CustomRedisTemplate customRedisTemplate(StringRedisTemplate stringRedisTemplate) {
        log.debug(AUTOWIRED_CUSTOM_REDIS_TEMPLATE);
        return new CustomRedisTemplateImpl(stringRedisTemplate);
    }
}
