package com.x9x.radp.spring.test.embedded.support;

import com.x9x.radp.extension.ExtensionLoader;
import com.x9x.radp.spring.test.embedded.EmbeddedServer;

/**
 * @author x9x
 * @since 2024-10-13 17:52
 */
public class EmbeddedServerHelper {

    public static EmbeddedServer embeddedServer(String spi, int port) {
        return ExtensionLoader.getExtensionLoader(EmbeddedServer.class).getExtension(spi).port(port);
    }
}
