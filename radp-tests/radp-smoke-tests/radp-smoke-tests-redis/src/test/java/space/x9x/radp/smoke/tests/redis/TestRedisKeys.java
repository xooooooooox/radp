package space.x9x.radp.smoke.tests.redis;

import space.x9x.radp.redis.spring.boot.constants.RedisKeyConstants;
import space.x9x.radp.redis.spring.boot.constants.RedisKeyProvider;

/**
 * Example implementation of RedisKeyProvider for test module.
 * <p>
 * This class demonstrates how scaffold projects can implement their own
 * Redis key management strategy while leveraging the centralized utilities.
 *
 * @author x9x
 * @since 2024-10-30
 */
public enum TestRedisKeys implements RedisKeyProvider {
    INSTANCE;

    private static final String PREFIX = "radp:test";

    // Module-specific key constants
    public static final String USER_PROFILE = createKey("user:profile");
    public static final String SESSION_DATA = createKey("session");
    public static final String CACHE_DATA = createKey("cache");

    @Override
    public String getPrefix() {
        return PREFIX;
    }

    @Override
    public String buildKey(String... parts) {
        return RedisKeyConstants.buildRedisKeyWithPrefix(getPrefix(), parts);
    }

    /**
     * Static utility method to create a key using the singleton instance.
     *
     * @param parts the parts to append after the prefix
     * @return the complete Redis key
     */
    public static String createKey(String... parts) {
        return INSTANCE.buildKey(parts);
    }

    /**
     * Builds a key for a specific test case.
     *
     * @param testName the test name
     * @param uniqueId a unique identifier (optional)
     * @return the complete Redis key
     */
    public static String buildTestKey(String testName, String uniqueId) {
        return uniqueId != null
                ? createKey(testName, uniqueId)
                : createKey(testName);
    }
}
