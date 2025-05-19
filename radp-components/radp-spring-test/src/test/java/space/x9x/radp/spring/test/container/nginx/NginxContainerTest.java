package space.x9x.radp.spring.test.container.nginx;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for {@link NginxContainer}.
 *
 * @author x9x
 * @since 2024-10-30
 */
class NginxContainerTest {

    private NginxContainer nginxContainer;

    @BeforeEach
    void setUp() {
        // Create and start the Nginx container
        nginxContainer = new NginxContainer();
        nginxContainer.start();
    }

    @AfterEach
    void tearDown() {
        // Stop the container
        if (nginxContainer != null && nginxContainer.isRunning()) {
            nginxContainer.stop();
        }
    }

    @Test
    void testContainerIsRunning() {
        // Verify that the container is running
        assertTrue(nginxContainer.isRunning());
    }

    @Test
    void testHttpConnection() throws Exception {
        // Get the HTTP URL from the container
        String httpUrl = nginxContainer.getHttpUrl();

        // Create a connection to the Nginx server
        URL url = new URL(httpUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        // Verify that we can connect to the server
        int responseCode = connection.getResponseCode();
        assertTrue(responseCode >= 200 && responseCode < 300, "Expected successful HTTP response");
    }

    @Test
    void testGetHttpUrl() {
        // Verify that getHttpUrl returns a non-empty string
        String httpUrl = nginxContainer.getHttpUrl();
        assertTrue(httpUrl != null && !httpUrl.isEmpty());

        // Verify that the URL contains the host and port
        assertTrue(httpUrl.contains(nginxContainer.getHost()));
        assertTrue(httpUrl.contains(String.valueOf(
                nginxContainer.getMappedPort(NginxContainer.NGINX_PORT))));
    }
}