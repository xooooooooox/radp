package space.x9x.radp.redis.spring.boot.support;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.redisson.api.*;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link RedissonService}.
 *
 * @author x9x
 * @since 2024-10-28 15:41
 */
class RedissonServiceTest {

    @Mock
    private RedissonClient redissonClient;

    @Mock
    private RBucket<String> stringBucket;

    @Mock
    private RBucket<Integer> integerBucket;

    @Mock
    private RAtomicLong atomicLong;

    @Mock
    private RQueue<String> queue;

    @Mock
    private RBlockingQueue<String> blockingQueue;

    @Mock
    private RDelayedQueue<String> delayedQueue;

    @Mock
    private RSet<String> set;

    @Mock
    private RList<String> list;

    @Mock
    private RMap<String, String> map;

    @Mock
    private RSortedSet<String> sortedSet;

    @Mock
    private RLock lock;

    @Mock
    private RReadWriteLock readWriteLock;

    @Mock
    private RSemaphore semaphore;

    @Mock
    private RPermitExpirableSemaphore permitExpirableSemaphore;

    @Mock
    private RCountDownLatch countDownLatch;

    @Mock
    private RBloomFilter<String> bloomFilter;

    @Mock
    private RBitSet bitSet;

    private RedissonService redissonService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        redissonService = new RedissonService(redissonClient);

        // Setup of common mocks
        when(redissonClient.<String>getBucket(anyString())).thenReturn(stringBucket);
        when(redissonClient.getAtomicLong(anyString())).thenReturn(atomicLong);
        when(redissonClient.<String>getQueue(anyString())).thenReturn(queue);
        when(redissonClient.<String>getBlockingQueue(anyString())).thenReturn(blockingQueue);
        when(redissonClient.<String>getDelayedQueue(any())).thenReturn(delayedQueue);
        when(redissonClient.<String>getSet(anyString())).thenReturn(set);
        when(redissonClient.<String>getList(anyString())).thenReturn(list);
        when(redissonClient.<String, String>getMap(anyString())).thenReturn(map);
        when(redissonClient.<String>getSortedSet(anyString())).thenReturn(sortedSet);
        when(redissonClient.getLock(anyString())).thenReturn(lock);
        when(redissonClient.getFairLock(anyString())).thenReturn(lock);
        when(redissonClient.getReadWriteLock(anyString())).thenReturn(readWriteLock);
        when(redissonClient.getSemaphore(anyString())).thenReturn(semaphore);
        when(redissonClient.getPermitExpirableSemaphore(anyString())).thenReturn(permitExpirableSemaphore);
        when(redissonClient.getCountDownLatch(anyString())).thenReturn(countDownLatch);
        when(redissonClient.<String>getBloomFilter(anyString())).thenReturn(bloomFilter);
        when(redissonClient.getBitSet(anyString())).thenReturn(bitSet);
    }

    @Test
    void testSetValue() {
        // Test setValue(String key, T value)
        redissonService.setValue("testKey", "testValue");
        verify(redissonClient).getBucket("testKey");
        verify(stringBucket).set("testValue");
    }

    @Test
    void testSetValueWithExpiration() {
        // Test setValue(String key, T value, long expired)
        long expiration = 1000L;
        redissonService.setValue("testKey", "testValue", expiration);
        verify(redissonClient).getBucket("testKey");
        verify(stringBucket).set("testValue", Duration.ofMillis(expiration));
    }

    @Test
    void testGetValue() {
        // Setup
        when(stringBucket.get()).thenReturn("testValue");

        // Test getValue(String key)
        String result = redissonService.getValue("testKey");

        // Verify
        verify(redissonClient).getBucket("testKey");
        verify(stringBucket).get();
        assertEquals("testValue", result);
    }

    @Test
    void testGetQueue() {
        // Test getQueue(String key)
        RQueue<String> result = redissonService.getQueue("testKey");

        // Verify
        verify(redissonClient).getQueue("testKey");
        assertEquals(queue, result);
    }

    @Test
    void testGetBlockingQueue() {
        // Test getBlockingQueue(String key)
        RBlockingQueue<String> result = redissonService.getBlockingQueue("testKey");

        // Verify
        verify(redissonClient).getBlockingQueue("testKey");
        assertEquals(blockingQueue, result);
    }

    @Test
    void testGetDelayedQueue() {
        // Test getDelayedQueue(RBlockingQueue<T> rBlockingQueue)
        RDelayedQueue<String> result = redissonService.getDelayedQueue(blockingQueue);

        // Verify
        verify(redissonClient).getDelayedQueue(blockingQueue);
        assertEquals(delayedQueue, result);
    }

    @Test
    void testSetAndGetAtomicLong() {
        // Setup
        when(atomicLong.get()).thenReturn(100L);

        // Test setAtomicLong(String key, long value)
        redissonService.setAtomicLong("testKey", 100L);

        // Verify
        verify(redissonClient).getAtomicLong("testKey");
        verify(atomicLong).set(100L);

        // Test getAtomicLong(String key)
        Long result = redissonService.getAtomicLong("testKey");

        // Verify
        verify(redissonClient, times(2)).getAtomicLong("testKey");
        verify(atomicLong).get();
        assertEquals(100L, result);
    }

    @Test
    void testIncrAndDecr() {
        // Setup
        when(atomicLong.incrementAndGet()).thenReturn(101L);
        when(atomicLong.decrementAndGet()).thenReturn(99L);
        when(atomicLong.addAndGet(10L)).thenReturn(110L);
        when(atomicLong.addAndGet(-10L)).thenReturn(90L);

        // Test incr(String key)
        long incrResult = redissonService.incr("testKey");

        // Verify
        verify(redissonClient).getAtomicLong("testKey");
        verify(atomicLong).incrementAndGet();
        assertEquals(101L, incrResult);

        // Test incrBy(String key, long delta)
        long incrByResult = redissonService.incrBy("testKey", 10L);

        // Verify
        verify(redissonClient, times(2)).getAtomicLong("testKey");
        verify(atomicLong).addAndGet(10L);
        assertEquals(110L, incrByResult);

        // Test decr(String key)
        long decrResult = redissonService.decr("testKey");

        // Verify
        verify(redissonClient, times(3)).getAtomicLong("testKey");
        verify(atomicLong).decrementAndGet();
        assertEquals(99L, decrResult);

        // Test decrBy(String key, long delta)
        long decrByResult = redissonService.decrBy("testKey", 10L);

        // Verify
        verify(redissonClient, times(4)).getAtomicLong("testKey");
        verify(atomicLong).addAndGet(-10L);
        assertEquals(90L, decrByResult);
    }

    @Test
    void testRemoveAndExists() {
        // Setup
        when(stringBucket.isExists()).thenReturn(true, false);

        // Test isExists(String key) - should return true
        boolean existsResult = redissonService.isExists("testKey");

        // Verify
        verify(redissonClient).getBucket("testKey");
        verify(stringBucket).isExists();
        assertTrue(existsResult);

        // Test remove(String key)
        redissonService.remove("testKey");

        // Verify
        verify(redissonClient, times(2)).getBucket("testKey");
        verify(stringBucket).delete();

        // Test isExists(String key) after removal - should return false
        existsResult = redissonService.isExists("testKey");

        // Verify
        verify(redissonClient, times(3)).getBucket("testKey");
        verify(stringBucket, times(2)).isExists();
        assertFalse(existsResult);
    }

    @Test
    void testSetOperations() {
        // Setup
        when(set.contains("testValue")).thenReturn(true);

        // Test addToSet(String key, String value)
        redissonService.addToSet("testKey", "testValue");

        // Verify
        verify(redissonClient).getSet("testKey");
        verify(set).add("testValue");

        // Test isSetMember(String key, String value)
        boolean isMember = redissonService.isSetMember("testKey", "testValue");

        // Verify
        verify(redissonClient, times(2)).getSet("testKey");
        verify(set).contains("testValue");
        assertTrue(isMember);
    }

    @Test
    void testListOperations() {
        // Setup
        when(list.get(0)).thenReturn("testValue");

        // Test addToList(String key, String value)
        redissonService.addToList("testKey", "testValue");

        // Verify
        verify(redissonClient).getList("testKey");
        verify(list).add("testValue");

        // Test getFromList(String key, int index)
        String value = redissonService.getFromList("testKey", 0);

        // Verify
        verify(redissonClient, times(2)).getList("testKey");
        verify(list).get(0);
        assertEquals("testValue", value);
    }

    @Test
    void testMapOperations() {
        // Setup
        when(map.get("field")).thenReturn("testValue");

        // Test addToMap(String key, String field, String value)
        redissonService.addToMap("testKey", "field", "testValue");

        // Verify
        verify(redissonClient).getMap("testKey");
        verify(map).put("field", "testValue");

        // Test getFromMap(String key, String field)
        String value = redissonService.getFromMap("testKey", "field");

        // Verify
        verify(redissonClient, times(2)).getMap("testKey");
        verify(map).get("field");
        assertEquals("testValue", value);
    }

    @Test
    void testSortedSetOperations() {
        // Test addToSortedSet(String key, String value)
        redissonService.addToSortedSet("testKey", "testValue");

        // Verify
        verify(redissonClient).getSortedSet("testKey");
        verify(sortedSet).add("testValue");
    }

    @Test
    void testLockOperations() {
        // Test getLock(String key)
        RLock lockResult = redissonService.getLock("testKey");

        // Verify
        verify(redissonClient).getLock("testKey");
        assertEquals(lock, lockResult);

        // Test getFairLock(String key)
        RLock fairLockResult = redissonService.getFairLock("testKey");

        // Verify
        verify(redissonClient).getFairLock("testKey");
        assertEquals(lock, fairLockResult);

        // Test getReadWriteLock(String key)
        RReadWriteLock rwLockResult = redissonService.getReadWriteLock("testKey");

        // Verify
        verify(redissonClient).getReadWriteLock("testKey");
        assertEquals(readWriteLock, rwLockResult);
    }

    @Test
    void testSemaphoreOperations() {
        // Test getSemaphore(String key)
        RSemaphore semaphoreResult = redissonService.getSemaphore("testKey");

        // Verify
        verify(redissonClient).getSemaphore("testKey");
        assertEquals(semaphore, semaphoreResult);

        // Test getPermitExpirableSemaphore(String key)
        RPermitExpirableSemaphore permitSemaphoreResult = redissonService.getPermitExpirableSemaphore("testKey");

        // Verify
        verify(redissonClient).getPermitExpirableSemaphore("testKey");
        assertEquals(permitExpirableSemaphore, permitSemaphoreResult);
    }

    @Test
    void testCountDownLatchOperations() {
        // Test getCountDownLatch(String key)
        RCountDownLatch latchResult = redissonService.getCountDownLatch("testKey");

        // Verify
        verify(redissonClient).getCountDownLatch("testKey");
        assertEquals(countDownLatch, latchResult);
    }

    @Test
    void testBloomFilterOperations() {
        // Test getBloomFilter(String key)
        RBloomFilter<String> filterResult = redissonService.getBloomFilter("testKey");

        // Verify
        verify(redissonClient).getBloomFilter("testKey");
        assertEquals(bloomFilter, filterResult);
    }

    @Test
    void testSetNx() {
        // Setup
        when(stringBucket.setIfAbsent(eq("lock"))).thenReturn(true);

        // Test setNx(String key)
        Boolean result = redissonService.setNx("testKey");

        // Verify
        verify(redissonClient).getBucket("testKey");
        verify(stringBucket).setIfAbsent("lock");
        assertTrue(result);
    }

    @Test
    void testSetNxWithExpiration() {
        // Setup
        when(stringBucket.setIfAbsent(eq("lock"), any(Duration.class))).thenReturn(true);

        // Test setNx(String key, long expired, TimeUnit timeUnit)
        Boolean result = redissonService.setNx("testKey", 1000L, TimeUnit.MILLISECONDS);

        // Verify
        verify(redissonClient).getBucket("testKey");
        verify(stringBucket).setIfAbsent(eq("lock"), any(Duration.class));
        assertTrue(result);
    }

    @Test
    void testGetBitSet() {
        // Test getBitSet(String key)
        RBitSet bitSetResult = redissonService.getBitSet("testKey");

        // Verify
        verify(redissonClient).getBitSet("testKey");
        assertEquals(bitSet, bitSetResult);
    }
}
