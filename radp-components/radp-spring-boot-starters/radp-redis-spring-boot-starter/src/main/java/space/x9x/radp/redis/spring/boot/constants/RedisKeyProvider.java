package space.x9x.radp.redis.spring.boot.constants;

/**
 * Interface for Redis key providers that standardizes key management across different modules.
 * <p>
 * Implementing this interface allows different modules to define their own Redis key formats
 * while maintaining a consistent approach to key generation throughout the application.
 *
 * @author x9x
 * @since 2024-10-30
 */
public interface RedisKeyProvider {

    /**
     * Gets the prefix for all keys managed by this provider.
     * <p>
     * The prefix typically includes the application and module name,
     * for example: "radp:module-name"
     *
     * @return the key prefix
     */
    String getPrefix();

    /**
     * Builds a complete Redis key using this provider's prefix and the specified parts.
     *
     * @param parts the parts of the key to append after the prefix
     * @return the complete Redis key
     */
    String buildKey(String... parts);
}