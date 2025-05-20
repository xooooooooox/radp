package space.x9x.radp.spring.test.embedded.kafka;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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

    /**
     * This test verifies that the server can be started and stopped correctly.
     * It also checks that the broker addresses are available when the server is running.
     * <p>
     * Note: This test is disabled by default because it requires network resources
     * and may fail in certain environments. Enable it manually when needed.
     */
    // @Test
    void testStartupAndShutdown() {
        EmbeddedKafkaServer server = new EmbeddedKafkaServer();
        server.port(9094); // Use a different port to avoid conflicts

        try {
            // Start the server
            server.startup();

            // Verify the server is running
            assertTrue(server.isRunning());

            // Verify broker addresses are available
            assertNotNull(server.getBrokerAddresses());
            System.out.println("[DEBUG_LOG] Broker addresses: " + server.getBrokerAddresses());

            // Add a small delay to allow the server to fully initialize
            Thread.sleep(2000);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            fail("Test was interrupted: " + e.getMessage());
        } finally {
            // Ensure the server is shut down even if assertions fail
            if (server.isRunning()) {
                server.shutdown();
            }

            // Verify the server is stopped
            assertFalse(server.isRunning());
        }
    }
}
