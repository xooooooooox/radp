package space.x9x.radp.spring.boot.actuate.env;

import lombok.experimental.UtilityClass;

/**
 * @author x9x
 * @since 2024-09-30 09:21
 */
@UtilityClass
public class ManagementServerEnvironment {
    /**
     * Property name for the management server port configuration.
     * This constant defines the Spring Boot property name used to configure the port
     * on which the Spring Boot Actuator management server runs.
     */
    public static final String PORT = "management.server.port";

    /**
     * Property name for the web endpoints base path configuration.
     * This constant defines the Spring Boot property name used to configure the base path
     * for all web endpoints exposed by Spring Boot Actuator.
     */
    public static final String ENDPOINTS_WEB_BASE_PATH = "management.endpoints.web.base-path";
}
