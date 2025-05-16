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
     * Default binding address for the embedded Redis server.
     * This constant defines the network interface that Redis will bind to.
     * The value "0.0.0.0" means Redis will listen on all available network interfaces.
     */
    public static final String DEFAULT_BIND = "0.0.0.0";

    /**
     * Default port for the embedded Redis server.
     * This constant defines the standard Redis port (6379) that the embedded server
     * will listen on by default.
     */
    public static final int DEFAULT_PORT = 6379;

    /**
     * Default maximum heap setting for the embedded Redis server.
     * This constant limits the maximum memory usage of the Redis server to 64MB,
     * which is suitable for most testing scenarios.
     */
    public static final String DEFAULT_MAX_HEAP = "maxheap 64MB";

    private final RedisServerBuilder redisServerBuilder;
    private RedisServer redisServer;
    private boolean isRunning = false;

    /**
     * Constructs a new EmbeddedRedisServer with default settings.
     * This constructor initializes the Redis server builder with the default
     * binding address, port, and maximum heap settings. The server is not started
     * until the {@link #startup()} method is called.
     */
    public EmbeddedRedisServer() {
        this.redisServerBuilder = new RedisServerBuilder()
                .bind(DEFAULT_BIND)
                .port(DEFAULT_PORT)
                .setting(DEFAULT_MAX_HEAP);
    }


    @Override
    public EmbeddedServer password(String password) {
        this.redisServerBuilder.setting("requirepass " + password);
        return this;
    }

    @Override
    public EmbeddedServer port(int port) {
        this.redisServerBuilder.port(port);
        return this;
    }

    @Override
    public void startup() {
        try {
            this.redisServer = redisServerBuilder.build();
            this.redisServer.start();
            this.isRunning = true;
        } catch (IOException e) {
            log.error("redis server startup failed", e);
        }
    }

    @Override
    public void shutdown() {
        if (!isRunning) {
            return;
        }
        try {
            this.redisServer.stop();
        } catch (IOException e) {
            log.error("redis server shutdown failed", e);
        }
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }
}
