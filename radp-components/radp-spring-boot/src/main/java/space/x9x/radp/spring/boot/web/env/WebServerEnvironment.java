package space.x9x.radp.spring.boot.web.env;

import lombok.experimental.UtilityClass;

/**
 * @author IO x9x
 * @since 2024-09-30 09:28
 */
@UtilityClass
public class WebServerEnvironment {
    /**
     * Property name for the web server port configuration.
     * This constant defines the Spring Boot property name used to configure the port
     * on which the embedded web server listens for incoming requests.
     */
    public static final String SERVER_PORT = "server.port";

    /**
     * Property name for the servlet context path configuration.
     * This constant defines the Spring Boot property name used to configure the context path
     * of the web application, which is the prefix added to all URL paths.
     */
    public static final String SERVER_SERVLET_CONTEXT_PATH = "server.servlet.context-path";

    /**
     * Property name for the SSL key store configuration.
     * This constant defines the Spring Boot property name used to configure the path
     * to the key store file containing the SSL certificate.
     */
    public static final String SERVER_SSL_KEY_STORE = "server.ssl.key-store";
}
