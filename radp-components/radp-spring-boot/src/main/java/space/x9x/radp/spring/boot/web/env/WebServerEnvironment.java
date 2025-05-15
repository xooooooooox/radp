package space.x9x.radp.spring.boot.web.env;

import lombok.experimental.UtilityClass;

/**
 * Constants for web server environment properties.
 * This utility class provides string constants for common web server configuration
 * properties used in Spring Boot applications.
 *
 * @author x9x
 * @since 2024-09-30 09:28
 */
@UtilityClass
public class WebServerEnvironment {
    /**
     * Property key for the server port configuration.
     * Used to specify the port on which the web server should listen.
     */
    public static final String SERVER_PORT = "server.port";

    /**
     * Property key for the servlet context path configuration.
     * Used to specify the base path for the application's endpoints.
     */
    public static final String SERVER_SERVLET_CONTEXT_PATH = "server.servlet.context-path";

    /**
     * Property key for the SSL key store configuration.
     * Used to specify the location of the SSL key store for HTTPS connections.
     */
    public static final String SERVER_SSL_KEY_STORE = "server.ssl.key-store";
}
