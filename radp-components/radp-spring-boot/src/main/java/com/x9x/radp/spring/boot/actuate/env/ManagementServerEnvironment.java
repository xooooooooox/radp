package com.x9x.radp.spring.boot.actuate.env;

import lombok.experimental.UtilityClass;

/**
 * @author x9x
 * @since 2024-09-30 09:21
 */
@UtilityClass
public class ManagementServerEnvironment {
    public static final String PORT = "management.server.port";
    public static final String ENDPOINTS_WEB_BASE_PATH = "management.endpoints.web.base-path";
}
