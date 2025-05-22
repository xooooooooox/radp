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

package space.x9x.radp.spring.test.container.redis;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

/**
 * Redis container for testing.
 * This class provides a convenient way to start and manage a Redis container for testing.
 *
 * @author x9x
 * @since 2024-10-30
 */
public class RedisContainer extends GenericContainer<RedisContainer> {

    /**
     * Default Redis port.
     */
    public static final int REDIS_PORT = 6379;

    /**
     * Default Redis image name.
     */
    public static final String DEFAULT_IMAGE_NAME = "redis:latest";

    /**
     * Creates a new Redis container with the default image and exposed port.
     */
    public RedisContainer() {
        this(DEFAULT_IMAGE_NAME);
    }

    /**
     * Creates a new Redis container with the specified image and default exposed port.
     *
     * @param dockerImageName the Docker image name to use
     */
    public RedisContainer(String dockerImageName) {
        super(DockerImageName.parse(dockerImageName));
        withExposedPorts(REDIS_PORT);
    }

    /**
     * Gets the Redis connection URL for the container.
     *
     * @return the Redis connection URL
     */
    public String getRedisConnectionUrl() {
        return String.format("redis://%s:%d", getHost(), getMappedPort(REDIS_PORT));
    }
}