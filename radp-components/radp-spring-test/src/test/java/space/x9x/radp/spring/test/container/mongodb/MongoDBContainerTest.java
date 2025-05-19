package space.x9x.radp.spring.test.container.mongodb;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.Socket;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for {@link MongoDBContainer}.
 *
 * @author x9x
 * @since 2024-10-30
 */
class MongoDBContainerTest {

    private MongoDBContainer mongoDBContainer;

    @BeforeEach
    void setUp() {
        // Create and start the MongoDB container
        mongoDBContainer = new MongoDBContainer();
        mongoDBContainer.start();
    }

    @AfterEach
    void tearDown() {
        // Stop the container
        if (mongoDBContainer != null && mongoDBContainer.isRunning()) {
            mongoDBContainer.stop();
        }
    }

    @Test
    void testContainerIsRunning() {
        // Verify that the container is running
        assertTrue(mongoDBContainer.isRunning());
    }

    @Test
    void testSocketConnection() throws Exception {
        // Get the host and port from the container
        String host = mongoDBContainer.getHost();
        Integer port = mongoDBContainer.getMappedPort(MongoDBContainer.MONGODB_PORT);

        // Create a socket connection to the MongoDB server
        try (Socket socket = new Socket(host, port)) {
            // Verify that the connection is established
            assertTrue(socket.isConnected());
        }
    }

    @Test
    void testGetConnectionString() {
        // Verify that getConnectionString returns a non-empty string
        String connectionString = mongoDBContainer.getConnectionString();
        assertTrue(connectionString != null && !connectionString.isEmpty());

        // Verify that the connection string contains the host and port
        assertTrue(connectionString.contains(mongoDBContainer.getHost()));
        assertTrue(connectionString.contains(String.valueOf(
                mongoDBContainer.getMappedPort(MongoDBContainer.MONGODB_PORT))));
    }
}