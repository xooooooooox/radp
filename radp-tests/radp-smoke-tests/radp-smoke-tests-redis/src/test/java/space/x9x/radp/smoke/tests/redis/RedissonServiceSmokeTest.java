package space.x9x.radp.smoke.tests.redis;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import space.x9x.radp.redis.spring.boot.support.RedissonService;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Smoke tests for {@link RedissonService} using Testcontainers.
 * These tests verify that RedissonService works correctly with a real Redis server.
 */
@Testcontainers
public class RedissonServiceSmokeTest {

    private static final String TEST_VALUE = "test-value";
    private static final int REDIS_PORT = 6379; // Default Redis port

    @Container
    private static final GenericContainer<?> redisContainer = new GenericContainer<>(
            DockerImageName.parse("redis:latest"))
            .withExposedPorts(REDIS_PORT);

    private RedissonClient redissonClient;
    private RedissonService redissonService;

    /**
     * Generate a unique key for each test to avoid conflicts.
     * Uses the RedisKeyProvider to create standardized keys with the test name and a UUID.
     *
     * @param testName the name of the test or operation
     * @return a unique Redis key for the test
     */
    private String getUniqueKey(String testName) {
        // Use UUID instead of timestamp for more deterministic and collision-resistant keys
        String uniqueId = UUID.randomUUID().toString().substring(0, 8); // Use the first 8 chars of UUID for brevity
        return TestRedisKeys.buildTestKey(testName, uniqueId);
    }

    @BeforeEach
    void setUp() {
        // Get the mapped port for Redis
        Integer mappedPort = redisContainer.getMappedPort(REDIS_PORT);
        String redisHost = redisContainer.getHost();

        // Configure Redisson client to connect to the Redis container
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://" + redisHost + ":" + mappedPort)
                .setConnectionMinimumIdleSize(1)
                .setConnectionPoolSize(2)
                .setConnectTimeout(10000)
                .setTimeout(3000);

        redissonClient = Redisson.create(config);
        redissonService = new RedissonService(redissonClient);

        // Clear the Redis database before each test
        redissonClient.getKeys().flushall();
    }

    @AfterEach
    void tearDown() {
        // Cleanup resources
        if (redissonClient != null) {
            redissonClient.shutdown();
        }
    }

    @Test
    void testSetAndGetValue() {
        // Generate a unique key for this test
        String testKey = getUniqueKey("setValue");

        // Set a value
        redissonService.setValue(testKey, TEST_VALUE);

        // Get the value and verify it matches what was set
        String retrievedValue = redissonService.getValue(testKey);
        assertEquals(TEST_VALUE, retrievedValue);
    }

    @Test
    void testSetValueWithExpiration() {
        // Generate a unique key for this test
        String testKey = getUniqueKey("setValueWithExpiration");

        // Set a value with a short expiration time
        redissonService.setValue(testKey, TEST_VALUE, 100);

        // Verify the value exists immediately after setting
        assertTrue(redissonService.isExists(testKey));

        // Wait for the value to expire
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Verify the value no longer exists after expiration
        assertFalse(redissonService.isExists(testKey));
    }

    @Test
    void testRemoveValue() {
        // Generate a unique key for this test
        String testKey = getUniqueKey("removeValue");

        // Set a value
        redissonService.setValue(testKey, TEST_VALUE);

        // Verify the value exists
        assertTrue(redissonService.isExists(testKey));

        // Remove the value
        redissonService.remove(testKey);

        // Verify the value no longer exists
        assertFalse(redissonService.isExists(testKey));
    }

    @Test
    void testAtomicOperations() {
        // Generate a unique key for this test
        String testKey = getUniqueKey("atomicOperations");

        // Set an atomic long value
        redissonService.setAtomicLong(testKey, 100L);

        // Get the value and verify it matches what was set
        Long value = redissonService.getAtomicLong(testKey);
        assertEquals(100L, value);

        // Increment the value
        long incrResult = redissonService.incr(testKey);
        assertEquals(101L, incrResult);

        // Increment by a specific amount
        long incrByResult = redissonService.incrBy(testKey, 10L);
        assertEquals(111L, incrByResult);

        // Decrement the value
        long decrResult = redissonService.decr(testKey);
        assertEquals(110L, decrResult);

        // Decrement by a specific amount
        long decrByResult = redissonService.decrBy(testKey, 10L);
        assertEquals(100L, decrByResult);
    }

    @Test
    void testSetOperations() {
        // Generate a unique key for this test
        String testKey = getUniqueKey("setOperations");

        // Add a value to a set
        redissonService.addToSet(testKey, TEST_VALUE);

        // Verify the value is a member of the set
        boolean isMember = redissonService.isSetMember(testKey, TEST_VALUE);
        assertTrue(isMember);

        // Verify a different value is not a member of the set
        boolean isNotMember = redissonService.isSetMember(testKey, "non-existent-value");
        assertFalse(isNotMember);
    }

    @Test
    void testListOperations() {
        // Generate a unique key for this test
        String testKey = getUniqueKey("listOperations");

        // Add a value to a list
        redissonService.addToList(testKey, TEST_VALUE);

        // Get the value from the list and verify it matches what was added
        String retrievedValue = redissonService.getFromList(testKey, 0);
        assertEquals(TEST_VALUE, retrievedValue);
    }

    @Test
    void testMapOperations() {
        // Generate a unique key for this test
        String testKey = getUniqueKey("mapOperations");

        // Add a value to a map
        String field = "field";
        redissonService.addToMap(testKey, field, TEST_VALUE);

        // Get the value from the map and verify it matches what was added
        String retrievedValue = redissonService.getFromMap(testKey, field);
        assertEquals(TEST_VALUE, retrievedValue);
    }

    @Test
    void testSetNx() {
        // Generate a unique key for this test
        String testKey = getUniqueKey("setNx");

        // Set a value using setNx (only if not exists)
        Boolean result = redissonService.setNx(testKey);
        assertTrue(result);

        // Try to set the value again
        Boolean secondResult = redissonService.setNx(testKey);
        assertFalse(secondResult);
    }

    @Test
    void testSetNxWithExpiration() {
        // Generate a unique key for this test
        String testKey = getUniqueKey("setNxWithExpiration");

        // Set a value using setNx with expiration
        Boolean result = redissonService.setNx(testKey, 100, TimeUnit.MILLISECONDS);
        assertTrue(result);

        // Wait for the value to expire
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Try to set the value again after expiration
        Boolean secondResult = redissonService.setNx(testKey);
        assertTrue(secondResult);
    }
}
