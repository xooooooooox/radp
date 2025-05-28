package space.x9x.radp.spring.boot.jdbc.env;

import lombok.experimental.UtilityClass;

/**
 * @author IO x9x
 * @since 2024-09-30 09:38
 */
@UtilityClass
public class DatasourceEnvironment {
    /**
     * Property name for the datasource URL configuration.
     * This constant defines the Spring Boot property name used to configure the JDBC datasource URL.
     */
    public static final String URL = "spring.datasource.url";
}
