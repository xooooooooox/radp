package space.x9x.radp.spring.boot.jdbc.env;

import lombok.experimental.UtilityClass;

/**
 * @author x9x
 * @since 2024-09-30 09:38
 */
@UtilityClass
public class DatasourceEnvironment {
    /**
     * Property key for the datasource URL.
     * This constant represents the Spring configuration property that defines
     * the JDBC URL for connecting to the database.
     */
    public static final String URL = "spring.datasource.url";
}
