/*
 * Copyright 2012-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package space.x9x.radp.spring.test.embedded.redis;

import java.io.IOException;

import lombok.extern.slf4j.Slf4j;
import redis.embedded.RedisServer;
import redis.embedded.core.RedisServerBuilder;

import space.x9x.radp.spring.framework.error.util.ExceptionUtils;
import space.x9x.radp.spring.test.embedded.IEmbeddedServer;

/**
 * @author IO x9x
 * @since 2024-09-23 15:02
 */
@Slf4j
public class EmbeddedRedisServer implements IEmbeddedServer {

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
     * Default maximum memory setting for the embedded Redis server.
     * This constant defines the maximum amount of memory that the Redis server can use.
     * The value "max-memory 64mb" limits the Redis server to using at most 64MB of memory.
     */
    public static final String DEFAULT_MAX_MEMORY = "maxmemory 64mb";

    private final RedisServerBuilder redisServerBuilder;
    private RedisServer redisServer;
    private boolean isRunning = false;
    private int port = DEFAULT_PORT;

    /**
     * Constructs a new EmbeddedRedisServer with default settings.
     * This constructor initializes the Redis server builder with the default bind address,
     * port, and maximum memory size. The server is not started until the startup() method is called.
     */
    public EmbeddedRedisServer() {
        this.redisServerBuilder = new RedisServerBuilder()
                .bind(DEFAULT_BIND)
                .port(DEFAULT_PORT)
                .setting(DEFAULT_MAX_MEMORY)
                .setting("daemonize no");
    }


    @Override
    public IEmbeddedServer password(String password) {
        this.redisServerBuilder.setting("requirepass " + password);
        return this;
    }

    @Override
    public IEmbeddedServer port(int port) {
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
            throw ExceptionUtils.serverException0("Failed to stop embedded Redis server", e);
        }
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }
}
