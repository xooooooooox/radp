package space.x9x.radp.spring.boot.actuate.env;

import lombok.experimental.UtilityClass;

/**
 * @author x9x
 * @since 2024-09-30 09:21
 */
@UtilityClass
public class ManagementServerEnvironment {
    /**
     * Property key for the management server port.
     * This constant represents the Spring configuration property that defines
     * the port on which the management server (actuator endpoints) will listen.
     */
    public static final String PORT = "management.server.port";

    /**
     * Property key for the management endpoints web base path.
     * This constant represents the Spring configuration property that defines
     * the base path for all web-exposed actuator endpoints.
     */
    public static final String ENDPOINTS_WEB_BASE_PATH = "management.endpoints.web.base-path";
}
