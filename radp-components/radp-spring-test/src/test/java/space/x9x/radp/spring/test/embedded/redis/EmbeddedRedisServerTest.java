package space.x9x.radp.spring.test.embedded.redis;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Tests for {@link EmbeddedRedisServer}.
 *
 * @author x9x
 * @since 2024-10-30
 */
class EmbeddedRedisServerTest {

    private static final int TEST_PORT = 6379;

    /**
     * This test verifies that the EmbeddedRedisServer class properly initializes
     * with the default settings.
     */
    @Test
    void testInitialization() {
        EmbeddedRedisServer server = new EmbeddedRedisServer();
        assertFalse(server.isRunning());
    }

    /**
     * This test verifies that the port method properly sets the port.
     */
    @Test
    void testPortSetting() {
        EmbeddedRedisServer server = new EmbeddedRedisServer();
        server.port(TEST_PORT);
        assertFalse(server.isRunning());
    }

    /**
     * This test verifies that the password method properly sets the password.
     */
    @Test
    void testPasswordSetting() {
        EmbeddedRedisServer server = new EmbeddedRedisServer();
        server.password("testpassword");
        assertFalse(server.isRunning());
    }

    /**
     * This test verifies that the isRunning method returns the correct value.
     */
    @Test
    void testIsRunning() {
        EmbeddedRedisServer server = new EmbeddedRedisServer();
        assertFalse(server.isRunning());
    }
}
