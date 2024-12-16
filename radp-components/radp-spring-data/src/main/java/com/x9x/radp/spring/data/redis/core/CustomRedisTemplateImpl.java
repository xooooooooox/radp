package com.x9x.radp.spring.data.redis.core;

import com.x9x.radp.commons.collections.CollectionUtils;
import com.x9x.radp.commons.lang.ObjectUtils;
import com.x9x.radp.commons.lang.StringUtils;
import com.x9x.radp.spring.framework.json.support.JSONHelper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author x9x
 * @since 2024-10-19 16:33
 */
@RequiredArgsConstructor
@Slf4j
@Getter
public class CustomRedisTemplateImpl implements CustomRedisTemplate {

    private final StringRedisTemplate redisTemplate;

    // ---------------------------------- RedisObject 操作 ----------------------------------

    @Override
    public boolean hasKey(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    @Override
    public boolean delete(String key) {
        return Boolean.TRUE.equals(redisTemplate.delete(key));
    }

    @Override
    public boolean expire(String key, long timeout) {
        return Boolean.TRUE.equals(redisTemplate.expire(key, timeout, TimeUnit.SECONDS));
    }

    // ---------------------------------- String 操作 ----------------------------------

    @Override
    public <T> Optional<T> get(String key, Class<T> clazz) {
        String value = redisTemplate.opsForValue().get(key);
        return toObject(value, clazz);
    }

    @Override
    public Optional<String> get(String key) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(key));
    }

    @Override
    public <T> Optional<List<T>> getForList(String key, Class<T> clazz) {
        String value = redisTemplate.opsForValue().get(key);
        if (StringUtils.isBlank(value)) {
            return Optional.empty();
        }
        return Optional.of(JSONHelper.json().parseList(value, clazz));
    }

    @Override
    public <T> void set(String key, T value) {
        redisTemplate.opsForValue().set(key, toString(value));
    }

    @Override
    public <T> void set(String key, T data, long timeout) {
        this.set(key, data, timeout, TimeUnit.SECONDS);
    }

    @Override
    public <T> void set(String key, T data, long timeout, TimeUnit unit) {
        String value = toString(data);
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    // ---------------------------------- Hash 操作 ----------------------------------

    @Override
    public <T> Optional<T> hget(String key, String hashKey, Class<T> clazz) {
        Object hashValue = redisTemplate.opsForHash().get(key, hashKey);
        return toObject(ObjectUtils.trimToString(hashValue), clazz);
    }

    @Override
    public Optional<Map<Object, Object>> hgetAll(String key) {
        return Optional.of(redisTemplate.opsForHash().entries(key));
    }

    @Override
    public <T> void hset(String key, String hashKey, T data) {
        redisTemplate.opsForHash().put(key, hashKey, toString(data));
    }

    @Override
    public void hset(String key, String hashKey, String value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    @Override
    public void hset(String key, Map<?, ?> map) {
        redisTemplate.opsForHash().putAll(key, map);
    }

    @Override
    public void hDelete(String key, Object... hashKeys) {
        redisTemplate.opsForHash().delete(key, hashKeys);
    }

    // ---------------------------------- List 操作 ----------------------------------

    @Override
    public <T> Optional<List<T>> range(String key, long start, long end, Class<T> clazz) {
        List<String> list = redisTemplate.opsForList().range(key, start, end);
        if (CollectionUtils.isEmpty(list)) {
            return Optional.empty();
        }
        return Optional.of(list.stream()
                .map(str -> JSONHelper.json().parseObject(str, clazz))
                .collect(Collectors.toList()));
    }

    @Override
    public <T> void rightPushAll(String key, List<T> data) {
        String[] values = new String[data.size()];
        for (int i = 0; i < values.length; i++) {
            values[i] = toString(data.get(i));
        }
        redisTemplate.opsForList().rightPushAll(key, values);
    }

    // ---------------------------------- Set 操作 ----------------------------------


    @Override
    public Optional<Set<String>> members(String key) {
        return Optional.ofNullable(redisTemplate.opsForSet().members(key));
    }

    @Override
    public <T> void add(String key, List<T> data) {
        String[] values = new String[data.size()];
        for (int i = 0; i < values.length; i++) {
            values[i] = toString(data.get(i));
        }
        redisTemplate.opsForSet().add(key, values);
    }

    // ------------------------------------------------------------------------------

    /**
     * Converts a JSON string to an object of the specified class.
     *
     * @param <T>   the type of the object to be returned
     * @param value the JSON string to be converted
     * @param clazz the class of the object to be returned
     * @return an Optional containing the converted object if the JSON string is not blank, otherwise an empty Optional
     */
    private <T> Optional<T> toObject(String value, Class<T> clazz) {
        return Optional.ofNullable(value)
                .filter(StringUtils::isNoneBlank)
                .map(s -> JSONHelper.json().parseObject(s, clazz));
    }


    /**
     * Converts the given data object to its String representation. If the data
     * is a character or a CharSequence, it returns the data's toString() value.
     * For other types of objects, it converts the data to a JSON string.
     *
     * @param <T>  The type of the data object to be converted.
     * @param data The data object to be converted to a String. This can be of any type.
     * @return The String representation of the data object. Returns null if the input data is null.
     */
    private <T> String toString(T data) {
        if (data == null) {
            return null;
        }
        String value;
        if (data instanceof CharSequence || data instanceof Character) {
            value = data.toString();
        } else {
            value = JSONHelper.json().toJSONString(data);
        }
        return value;
    }
}
