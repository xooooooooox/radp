package space.x9x.radp.spring.data.redis.core;

import space.x9x.radp.commons.lang.StringUtils;
import space.x9x.radp.commons.lang.Strings;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 自定义 Redis 模板接口
 *
 * @author x9x
 * @since 2024-10-19 16:18
 */
public interface CustomRedisTemplate {

    /**
     * 默认过期时间(一天)
     */
    long DEFAULT_EXPIRE = 86400L;

    // ---------------------------------- RedisObject 操作 ----------------------------------

    /**
     * 判断 key 是否存在
     *
     * @param key 要检查的 key
     * @return 如果 key 存在返回 true, 否则返回 false
     */
    boolean hasKey(String key);

    /**
     * 删除 key
     *
     * @param key 要删除的 key
     */
    boolean delete(String key);

    /**
     * 设置过期时间, 注意元执行操作
     *
     * @param key     要设置过期时间的 key
     * @param timeout 过期时间(单位秒)
     */
    boolean expire(String key, long timeout);

    // ---------------------------------- String 操作 ----------------------------------

    /**
     * 根据 key 获取值对象, 并反序列化为对应的类类型
     * Retrieves an object from the cache by its key and deserializes it to the specified class type.
     *
     * @param <T>   the type of the object to be returned
     * @param key   the key to look up in the cache
     * @param clazz the class of the object to be returned
     * @return an Optional containing the object if found, otherwise an empty Optional
     */
    <T> Optional<T> get(String key, Class<T> clazz);

    /**
     * Retrieves a value from the cache by its key.
     *
     * @param key the key to look up in the cache
     * @return an Optional containing the value if found, otherwise an empty Optional
     */
    Optional<String> get(String key);

    /**
     * Retrieves a List of objects from the cache by its key and deserializes
     * them to the specified class type.
     *
     * @param <T>   the type of the objects to be returned
     * @param key   the key to look up in the cache
     * @param clazz the class of the objects to be returned
     * @return an Optional containing the List of objects if found, otherwise an empty Optional
     */
    <T> Optional<List<T>> getForList(String key, Class<T> clazz);

    <T> void set(String key, T value);

    /**
     * Sets a value in the cache with the specified key and timeout.
     *
     * @param <T>     the type of the data to be stored
     * @param key     the key under which the data is to be stored
     * @param data    the data to be stored in the cache
     * @param timeout the timeout duration in seconds after which the data will expire
     */
    <T> void set(String key, T data, long timeout);

    /**
     * Sets a value in the cache with the specified key, timeout, and time unit.
     *
     * @param <T>     the type of the data to be stored
     * @param key     the key under which the data is to be stored
     * @param data    the data to be stored in the cache
     * @param timeout the timeout duration after which the data will expire
     * @param unit    the unit of time for the timeout
     */
    <T> void set(String key, T data, long timeout, TimeUnit unit);

    // ---------------------------------- Hash 操作 ----------------------------------

    /**
     * Retrieves a value from a hash map in the cache by its key and hash key,
     * deserializing it to the specified class type.
     *
     * @param <T>     the type of the object to be returned
     * @param key     the key of the hash map in the cache
     * @param hashKey the key of the value within the hash map
     * @param clazz   the class of the object to be returned
     * @return an Optional containing the object if found, otherwise an empty Optional
     */
    <T> Optional<T> hget(String key, String hashKey, Class<T> clazz);

    /**
     * Retrieves all the key-value pairs from the hash stored at the specified key.
     *
     * @param key the key of the hash map in the cache
     * @return an Optional containing a Map of all the key-value pairs if found, otherwise an empty Optional
     */
    Optional<Map<Object, Object>> hgetAll(String key);

    /**
     * Sets a value in the hash stored at the specified key and hash key.
     *
     * @param <T>     the type of the data to be stored
     * @param key     the key under which the hash is stored
     * @param hashKey the key within the hash to store the data
     * @param data    the data to be stored in the hash
     */
    <T> void hset(String key, String hashKey, T data);

    /**
     * Sets a value in the hash stored at the specified key and hash key.
     *
     * @param key the key under which the hash is stored
     * @param hashKey the key within the hash to store the value
     * @param value the value to be stored in the hash
     */
    void hset(String key, String hashKey, String value);

    /**
     * Sets multiple key-value pairs in the hash stored at the specified key.
     *
     * @param key the key under which the hash is stored
     * @param map the map containing key-value pairs to be stored in the hash
     */
    void hset(String key, Map<?, ?> map);

    /**
     * Deletes one or more hash keys from the hash stored at the specified key in the cache.
     *
     * @param key the key of the hash map in the cache
     * @param hashKeys the hash keys to delete from the hash map
     */
    void hDelete(String key, Object... hashKeys);

    // ---------------------------------- List 操作 ----------------------------------

    /**
     * Retrieves a sublist of elements from a list stored in the cache, within the specified range, and deserializes them to the specified class type.
     *
     * @param <T>   the type of the objects to be returned
     * @param key   the key to look up in the cache
     * @param start the start index of the range (inclusive)
     * @param end   the end index of the range (exclusive)
     * @param clazz the class of the objects to be returned
     * @return an Optional containing the List of objects if found, otherwise an empty Optional
     */
    <T> Optional<List<T>> range(String key, long start, long end, Class<T> clazz);

    /**
     * Pushes all elements of the provided list to the right end of the list stored at the specified key in the cache.
     *
     * @param <T>  the type of the elements in the list
     * @param key  the key of the list in the cache
     * @param data the list of elements to be pushed to the cache
     */
    <T> void rightPushAll(String key, List<T> data);

    // ---------------------------------- Set 操作 ----------------------------------

    /**
     * Retrieves a set of members associated with the specified key from the cache.
     *
     * @param key the key to look up in the cache
     * @return an Optional containing the Set of members if found, otherwise an empty Optional
     */
    Optional<Set<String>> members(String key);

    /**
     * Adds a list of values associated with the given key in the cache.
     * This method stores the provided list of values in the cache using the specified key.
     *
     * @param <T>    the type of the values to be added
     * @param key    the key under which the list of values is to be stored
     * @param data the list of values to be stored in the cache
     */
    <T> void add(String key, List<T> data);

    // ------------------------------------------------------------------------------

    /**
     * Constructs a Redis key using the provided key name and identifier.
     *
     * @param keyPrefix the base key name to be used in the Redis key
     * @param key the unique identifier to be appended to the key name
     * @return a string representing the complete Redis key
     */
    default <T> String buildRedisKey(String keyPrefix, T key) {
        return StringUtils.join(keyPrefix, Strings.COLON, String.valueOf(key));
    }
}
