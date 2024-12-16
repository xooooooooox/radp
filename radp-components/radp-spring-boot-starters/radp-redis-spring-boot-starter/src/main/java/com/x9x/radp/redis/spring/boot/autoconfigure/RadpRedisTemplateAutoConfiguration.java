package com.x9x.radp.redis.spring.boot.autoconfigure;

import cn.hutool.core.util.ReflectUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.redisson.spring.starter.RedissonAutoConfiguration;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Role;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * @author x9x
 * @since 2024-10-21 11:00
 */
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@AutoConfiguration(before = RedissonAutoConfiguration.class)
@Slf4j
public class RadpRedisTemplateAutoConfiguration {

    private static final String AUTOWIRED_RADP_REDIS_TEMPLATE = "Autowired redisTemplate";

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        log.debug(AUTOWIRED_RADP_REDIS_TEMPLATE);
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        template.setKeySerializer(RedisSerializer.string());
        template.setHashKeySerializer(RedisSerializer.string());
        template.setValueSerializer(buildRedisSerializer());
        template.setHashValueSerializer(buildRedisSerializer());
        return template;
    }

    public static RedisSerializer<?> buildRedisSerializer() {
        RedisSerializer<Object> json = RedisSerializer.json();
        ObjectMapper objectMapper = (ObjectMapper) ReflectUtil.getFieldValue(json, "mapper");
        objectMapper.registerModule(new JavaTimeModule());
        return json;
    }
}
