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

package space.x9x.radp.spring.test.embedded.support;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import space.x9x.radp.extension.ExtensionLoader;
import space.x9x.radp.spring.test.embedded.IEmbeddedServer;
import space.x9x.radp.spring.test.embedded.redis.EmbeddedRedisServer;

/**
 * Helper class for working with embedded servers.
 * This class provides utility methods for creating, configuring, and managing embedded servers for testing.
 * It supports various server types including Redis, Zookeeper, and Kafka.
 *
 * @author x9x
 * @since 2024-10-13 17:52
 */
@Slf4j
@UtilityClass
public class EmbeddedServerHelper {

    /**
     * Creates and configures an embedded server instance using the extension mechanism.
     * This method loads an EmbeddedServer implementation based on the provided SPI name,
     * and configures it with the specified port.
     *
     * @param spi  the SPI name of the embedded server implementation to load
     * @param port the port on which the embedded server should listen
     * @return a configured EmbeddedServer instance ready to be started
     */
    public static IEmbeddedServer embeddedServer(String spi, int port) {
        try {
            return ExtensionLoader.getExtensionLoader(IEmbeddedServer.class).getExtension(spi).port(port);
        } catch (Exception e) {
            log.error("Failed to create embedded server with SPI: {}", spi, e);
            // TODO 2025/5/23: 调整 ExceptionUtils
//            throw ExceptionUtils.serverException0("Failed to create embedded server with SPI: {}", spi, e);
            throw new RuntimeException("Failed to create embedded server with API: " + spi, e);
        }
    }

    public static IEmbeddedServer embeddedServer(EmbeddedServerType type) {
        return embeddedServer(type.getSpi(), type.port);
    }

    @RequiredArgsConstructor
    @Getter
    public enum EmbeddedServerType {
        REDIS("redis", EmbeddedRedisServer.DEFAULT_PORT),
        ;

        private final String spi;
        private final Integer port;
    }
}
