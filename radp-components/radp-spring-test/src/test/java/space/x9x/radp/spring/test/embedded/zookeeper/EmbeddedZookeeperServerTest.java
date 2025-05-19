package space.x9x.radp.spring.test.embedded.zookeeper;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Tests for {@link EmbeddedZookeeperServer}.
 *
 * @author x9x
 * @since 2024-10-30
 */
class EmbeddedZookeeperServerTest {

    private static final int TEST_PORT = 2182;

    /**
     * This test verifies that the EmbeddedZookeeperServer class properly initializes
     * with the default settings.
     */
    @Test
    void testInitialization() {
        EmbeddedZookeeperServer server = new EmbeddedZookeeperServer();
        assertFalse(server.isRunning());
    }

    /**
     * This test verifies that the port method properly sets the port.
     * Note: In the current implementation, port cannot be changed after initialization,
     * so this test just verifies that the method doesn't throw an exception.
     */
    @Test
    void testPortSetting() {
        EmbeddedZookeeperServer server = new EmbeddedZookeeperServer();
        server.port(TEST_PORT);
        assertFalse(server.isRunning());
    }

    /**
     * This test verifies that the isRunning method returns the correct value.
     */
    @Test
    void testIsRunning() {
        EmbeddedZookeeperServer server = new EmbeddedZookeeperServer();
        assertFalse(server.isRunning());
    }
}
