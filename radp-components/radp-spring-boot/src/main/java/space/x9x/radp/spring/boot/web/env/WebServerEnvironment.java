package space.x9x.radp.spring.boot.web.env;

import lombok.experimental.UtilityClass;

/**
 * @author x9x
 * @since 2024-09-30 09:28
 */
@UtilityClass
public class WebServerEnvironment {
    public static final String SERVER_PORT = "server.port";
    public static final String SERVER_SERVLET_CONTEXT_PATH = "server.servlet.context-path";
    public static final String SERVER_SSL_KEY_STORE = "server.ssl.key-store";
}
