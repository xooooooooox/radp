package space.x9x.radp.spring.test.embedded.redis;

import space.x9x.radp.spring.test.embedded.EmbeddedServer;
import lombok.extern.slf4j.Slf4j;
import redis.embedded.RedisServer;
import redis.embedded.core.RedisServerBuilder;

import java.io.IOException;

/**
 * @author x9x
 * @since 2024-09-23 15:02
 */
@Slf4j
public class EmbeddedRedisServer implements EmbeddedServer {

    public static final String DEFAULT_BIND = "0.0.0.0";
    public static final int DEFAULT_PORT = 6379;
    public static final String DEFAULT_MAX_HEAP = "maxheap 64MB";

    private final RedisServerBuilder redisServerBuilder;
    private RedisServer redisServer;
    private boolean isRunning = false;

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
