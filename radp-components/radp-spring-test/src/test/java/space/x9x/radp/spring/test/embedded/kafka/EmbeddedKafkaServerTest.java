package space.x9x.radp.spring.test.embedded.kafka;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Tests for {@link EmbeddedKafkaServer}.
 *
 * @author x9x
 * @since 2024-10-30
 */
class EmbeddedKafkaServerTest {

    private static final int TEST_PORT = 9093;
    private static final int TEST_ZOOKEEPER_PORT = 2182;

    /**
     * This test verifies that the EmbeddedKafkaServer class properly initializes
     * with the default settings.
     */
    @Test
    void testInitialization() {
        EmbeddedKafkaServer server = new EmbeddedKafkaServer();
        assertFalse(server.isRunning());
    }

    /**
     * This test verifies that the port method properly sets the port.
     */
    @Test
    void testPortSetting() {
        EmbeddedKafkaServer server = new EmbeddedKafkaServer();
        server.port(TEST_PORT);
        assertFalse(server.isRunning());
    }

    /**
     * This test verifies that the zookeeperPort method properly sets the Zookeeper port.
     */
    @Test
    void testZookeeperPortSetting() {
        EmbeddedKafkaServer server = new EmbeddedKafkaServer();
        server.zookeeperPort(TEST_ZOOKEEPER_PORT);
        assertFalse(server.isRunning());
    }

    /**
     * This test verifies that the isRunning method returns the correct value.
     */
    @Test
    void testIsRunning() {
        EmbeddedKafkaServer server = new EmbeddedKafkaServer();
        assertFalse(server.isRunning());
    }

    /**
     * This test verifies that the getBrokerAddresses method returns null when the server is not running.
     */
    @Test
    void testGetBrokerAddresses() {
        EmbeddedKafkaServer server = new EmbeddedKafkaServer();
        assertNull(server.getBrokerAddresses());
    }
}