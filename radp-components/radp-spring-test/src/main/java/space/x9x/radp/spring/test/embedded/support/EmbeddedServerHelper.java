package space.x9x.radp.spring.test.embedded.support;

import lombok.extern.slf4j.Slf4j;
import space.x9x.radp.extension.ExtensionLoader;
import space.x9x.radp.spring.test.embedded.EmbeddedServer;
import space.x9x.radp.spring.test.embedded.kafka.EmbeddedKafkaServer;
import space.x9x.radp.spring.test.embedded.redis.EmbeddedRedisServer;
import space.x9x.radp.spring.test.embedded.zookeeper.EmbeddedZookeeperServer;

/**
 * Helper class for working with embedded servers.
 * This class provides utility methods for creating, configuring, and managing embedded servers for testing.
 * It supports various server types including Redis, Zookeeper, and Kafka.
 *
 * @author x9x
 * @since 2024-10-13 17:52
 */
@Slf4j
public class EmbeddedServerHelper {

    // Private constructor to prevent instantiation
    private EmbeddedServerHelper() {
        throw new UnsupportedOperationException("Utility class should not be instantiated");
    }

    /**
     * Creates and configures an embedded server instance using the extension mechanism.
     * This method loads an EmbeddedServer implementation based on the provided SPI name,
     * and configures it with the specified port.
     *
     * @param spi  the SPI name of the embedded server implementation to load
     * @param port the port on which the embedded server should listen
     * @return a configured EmbeddedServer instance ready to be started
     */
    public static EmbeddedServer embeddedServer(String spi, int port) {
        try {
            return ExtensionLoader.getExtensionLoader(EmbeddedServer.class).getExtension(spi).port(port);
        } catch (Exception e) {
            log.error("Failed to create embedded server with SPI: {}", spi, e);
            throw new RuntimeException("Failed to create embedded server: " + spi, e);
        }
    }

    /**
     * Creates a new embedded Redis server with the default port.
     *
     * @return a new embedded Redis server
     */
    public static EmbeddedRedisServer redisServer() {
        return redisServer(EmbeddedRedisServer.DEFAULT_PORT);
    }

    /**
     * Creates a new embedded Redis server with the specified port.
     *
     * @param port the port on which the Redis server should listen
     * @return a new embedded Redis server
     */
    public static EmbeddedRedisServer redisServer(int port) {
        EmbeddedRedisServer server = new EmbeddedRedisServer();
        server.port(port);
        return server;
    }

    /**
     * Creates a new embedded Redis server with the specified port and password.
     *
     * @param port     the port on which the Redis server should listen
     * @param password the password for the Redis server
     * @return a new embedded Redis server
     */
    public static EmbeddedRedisServer redisServer(int port, String password) {
        EmbeddedRedisServer server = new EmbeddedRedisServer();
        server.port(port).password(password);
        return server;
    }

    /**
     * Creates a new embedded Zookeeper server with the default port.
     *
     * @return a new embedded Zookeeper server
     */
    public static EmbeddedZookeeperServer zookeeperServer() {
        return new EmbeddedZookeeperServer();
    }

    /**
     * Creates a new embedded Kafka server with the default port.
     *
     * @return a new embedded Kafka server
     */
    public static EmbeddedKafkaServer kafkaServer() {
        return new EmbeddedKafkaServer();
    }

    /**
     * Creates a new embedded Kafka server with the specified port.
     *
     * @param port the port on which the Kafka server should listen
     * @return a new embedded Kafka server
     */
    public static EmbeddedKafkaServer kafkaServer(int port) {
        EmbeddedKafkaServer server = new EmbeddedKafkaServer();
        server.port(port);
        return server;
    }

    /**
     * Creates a new embedded Kafka server with the specified Kafka port and Zookeeper port.
     *
     * @param kafkaPort     the port on which the Kafka server should listen
     * @param zookeeperPort the port on which the embedded Zookeeper server should listen
     * @return a new embedded Kafka server
     */
    public static EmbeddedKafkaServer kafkaServer(int kafkaPort, int zookeeperPort) {
        EmbeddedKafkaServer server = new EmbeddedKafkaServer();
        server.port(kafkaPort);
        server.zookeeperPort(zookeeperPort);
        return server;
    }

    /**
     * Starts the embedded server if it's not already running.
     * This method handles exceptions that might occur during server startup.
     *
     * @param server the embedded server to start
     * @return the started server
     * @throws RuntimeException if the server fails to start
     */
    public static EmbeddedServer startServer(EmbeddedServer server) {
        if (server == null) {
            throw new IllegalArgumentException("Server cannot be null");
        }

        if (!server.isRunning()) {
            try {
                log.info("Starting embedded server");
                server.startup();
                log.info("Embedded server started successfully");
            } catch (Exception e) {
                log.error("Failed to start embedded server", e);
                throw new RuntimeException("Failed to start embedded server", e);
            }
        } else {
            log.debug("Embedded server already running");
        }
        return server;
    }

    /**
     * Stops the embedded server if it's running.
     * This method handles exceptions that might occur during server shutdown.
     *
     * @param server the embedded server to stop
     */
    public static void stopServer(EmbeddedServer server) {
        if (server != null && server.isRunning()) {
            try {
                log.info("Stopping embedded server");
                server.shutdown();
                log.info("Embedded server stopped successfully");
            } catch (Exception e) {
                log.error("Failed to stop embedded server", e);
            }
        } else if (server != null) {
            log.debug("Embedded server not running");
        }
    }
}
