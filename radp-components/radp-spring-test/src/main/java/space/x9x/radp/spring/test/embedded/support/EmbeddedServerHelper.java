package space.x9x.radp.spring.test.embedded.support;

import space.x9x.radp.extension.ExtensionLoader;
import space.x9x.radp.spring.test.embedded.EmbeddedServer;

/**
 * Helper class for creating and configuring embedded servers.
 * This utility class provides methods to easily create embedded server instances
 * for testing purposes, using the extension loading mechanism.
 *
 * @author x9x
 * @since 2024-10-13 17:52
 */
public class EmbeddedServerHelper {

    /**
     * Creates an embedded server instance with the specified SPI name and port.
     * This method uses the extension loading mechanism to find and instantiate
     * the appropriate embedded server implementation.
     *
     * @param spi  the SPI name of the embedded server implementation to use
     * @param port the port number on which the embedded server should listen
     * @return a configured embedded server instance
     */
    public static EmbeddedServer embeddedServer(String spi, int port) {
        return ExtensionLoader.getExtensionLoader(EmbeddedServer.class).getExtension(spi).port(port);
    }
}
