package space.x9x.radp.spring.test.embedded.support;

import space.x9x.radp.extension.ExtensionLoader;
import space.x9x.radp.spring.test.embedded.EmbeddedServer;

/**
 * @author x9x
 * @since 2024-10-13 17:52
 */
public class EmbeddedServerHelper {

    /**
     * Creates and configures an embedded server instance.
     * This method loads an EmbeddedServer implementation using the extension mechanism,
     * based on the provided SPI name, and configures it with the specified port.
     *
     * @param spi  the SPI name of the embedded server implementation to load
     * @param port the port on which the embedded server should listen
     * @return a configured EmbeddedServer instance ready to be started
     */
    public static EmbeddedServer embeddedServer(String spi, int port) {
        return ExtensionLoader.getExtensionLoader(EmbeddedServer.class).getExtension(spi).port(port);
    }
}
