package space.x9x.radp.spring.test.embedded.support;

import org.junit.jupiter.api.Test;
import space.x9x.radp.spring.test.embedded.EmbeddedServer;
import space.x9x.radp.spring.test.embedded.kafka.EmbeddedKafkaServer;
import space.x9x.radp.spring.test.embedded.redis.EmbeddedRedisServer;
import space.x9x.radp.spring.test.embedded.zookeeper.EmbeddedZookeeperServer;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link EmbeddedServerHelper}.
 *
 * @author x9x
 * @since 2024-10-30
 */
class EmbeddedServerHelperTest {

    @Test
    void testRedisServer() {
        // Test creating a Redis server with default port
        EmbeddedRedisServer server = EmbeddedServerHelper.redisServer();
        assertNotNull(server);

        // Test creating a Redis server with custom port
        int customPort = 6380;
        EmbeddedRedisServer portServer = EmbeddedServerHelper.redisServer(customPort);
        assertNotNull(portServer);

        // Test creating a Redis server with custom port and password
        String password = "testpassword";
        EmbeddedRedisServer passwordServer = EmbeddedServerHelper.redisServer(customPort, password);
        assertNotNull(passwordServer);
    }

    @Test
    void testZookeeperServer() {
        // Test creating a Zookeeper server
        EmbeddedZookeeperServer server = EmbeddedServerHelper.zookeeperServer();
        assertNotNull(server);
    }

    @Test
    void testKafkaServer() {
        // Test creating a Kafka server with default port
        EmbeddedKafkaServer server = EmbeddedServerHelper.kafkaServer();
        assertNotNull(server);

        // Test creating a Kafka server with custom port
        int customPort = 9093;
        EmbeddedKafkaServer portServer = EmbeddedServerHelper.kafkaServer(customPort);
        assertNotNull(portServer);

        // Test creating a Kafka server with custom Kafka port and Zookeeper port
        int kafkaPort = 9093;
        int zookeeperPort = 2182;
        EmbeddedKafkaServer dualPortServer = EmbeddedServerHelper.kafkaServer(kafkaPort, zookeeperPort);
        assertNotNull(dualPortServer);
    }

    @Test
    void testEmbeddedServer() {
        // Test creating an embedded server using the extension mechanism
        // Note: This requires the "redis" SPI to be properly registered
        EmbeddedServer server = EmbeddedServerHelper.embeddedServer("redis", 6380);
        assertNotNull(server);
    }

    // Note: We're not testing startServer and stopServer methods directly
    // because they require actual server instances to be started and stopped.
    // These methods are tested indirectly through the server-specific tests
    // in other test classes.

    @Test
    void testPrivateConstructor() {
        // Test that the private constructor throws an exception when called via reflection
        Exception exception = assertThrows(Exception.class, () -> {
            var constructor = EmbeddedServerHelper.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            constructor.newInstance();
        });

        // Verify that the exception message indicates that the class should not be instantiated
        assertTrue(exception.getCause().getMessage().contains("should not be instantiated"));
    }
}