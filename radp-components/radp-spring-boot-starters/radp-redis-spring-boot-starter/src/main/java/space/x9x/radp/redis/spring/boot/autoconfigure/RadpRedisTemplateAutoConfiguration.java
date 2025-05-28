package space.x9x.radp.redis.spring.boot.autoconfigure;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Role;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

import cn.hutool.core.util.ReflectUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.redisson.spring.starter.RedissonAutoConfiguration;

/**
 * @author IO x9x
 * @since 2024-10-21 11:00
 */
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@AutoConfiguration(before = RedissonAutoConfiguration.class)
@Slf4j
public class RadpRedisTemplateAutoConfiguration {

    private static final String AUTOWIRED_RADP_REDIS_TEMPLATE = "Autowired redisTemplate";

    /**
     * Creates and configures a RedisTemplate bean.
     * This method sets up a template with string serializers for keys and hash keys,
     * and JSON serializers for values and hash values.
     *
     * @param factory the Redis connection factory to use
     * @return a configured RedisTemplate instance
     */
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

    /**
     * Creates a Redis serializer with support for Java 8 date/time types.
     * This method builds a JSON serializer and enhances its ObjectMapper
     * with the JavaTimeModule to properly handle Java 8 date and time classes.
     *
     * @return a configured RedisSerializer instance
     */
    public static RedisSerializer<?> buildRedisSerializer() {
        RedisSerializer<Object> json = RedisSerializer.json();
        ObjectMapper objectMapper = (ObjectMapper) ReflectUtil.getFieldValue(json, "mapper");
        objectMapper.registerModule(new JavaTimeModule());
        return json;
    }
}
