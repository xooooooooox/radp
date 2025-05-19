package space.x9x.radp.spring.test.embedded.redis;

import lombok.extern.slf4j.Slf4j;
import redis.embedded.RedisServer;
import redis.embedded.core.RedisServerBuilder;
import space.x9x.radp.spring.test.embedded.EmbeddedServer;

import java.io.IOException;

/**
 * @author x9x
 * @since 2024-09-23 15:02
 */
@Slf4j
public class EmbeddedRedisServer implements EmbeddedServer {

    /**
     * Default bind address for the embedded Redis server.
     * This constant defines the IP address to which the Redis server will bind.
     * The value "0.0.0.0" means the server will accept connections on all available network interfaces.
     */
    public static final String DEFAULT_BIND = "0.0.0.0";

    /**
     * Default port for the embedded Redis server.
     * This constant defines the port on which the Redis server will listen for connections.
     * Value 6379 is the standard port used by Redis servers.
     */
    public static final int DEFAULT_PORT = 6379;

    /**
     * Default maximum heap setting for the embedded Redis server.
     * This constant defines the maximum amount of memory that the Redis server can use.
     * The value "maxheap 64MB" limits the Redis server to using at most 64MB of memory.
     */
    public static final String DEFAULT_MAX_HEAP = "maxheap 64MB";

    private final RedisServerBuilder redisServerBuilder;
    private RedisServer redisServer;
    private boolean isRunning = false;
    private int port = DEFAULT_PORT;

    /**
     * Constructs a new EmbeddedRedisServer with default settings.
     * This constructor initializes the Redis server builder with the default bind address,
     * port, and maximum heap size. The server is not started until the startup() method is called.
     */
    public EmbeddedRedisServer() {
        this.redisServerBuilder = new RedisServerBuilder()
                .bind(DEFAULT_BIND)
                .port(DEFAULT_PORT)
                .setting(DEFAULT_MAX_HEAP)
                .setting("daemonize no");
    }


    @Override
    public EmbeddedServer password(String password) {
        this.redisServerBuilder.setting("requirepass " + password);
        return this;
    }

    @Override
    public EmbeddedServer port(int port) {
        this.port = port;
        this.redisServerBuilder.port(port);
        return this;
    }

    @Override
    public void startup() {
        try {
            this.redisServer = redisServerBuilder.build();
            this.redisServer.start();
            this.isRunning = true;
            log.info("Embedded Redis server started on port {}", port);
        } catch (IOException e) {
            this.isRunning = false;
            log.error("Failed to start embedded Redis server on port {}", port, e);
            throw new RuntimeException("Failed to start embedded Redis server on port " + port, e);
        }
    }

    @Override
    public void shutdown() {
        if (!isRunning) {
            return;
        }
        try {
            this.redisServer.stop();
            this.isRunning = false;
            log.info("Embedded Redis server stopped");
        } catch (IOException e) {
            log.error("Failed to stop embedded Redis server", e);
            throw new RuntimeException("Failed to stop embedded Redis server", e);
        }
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }
}
