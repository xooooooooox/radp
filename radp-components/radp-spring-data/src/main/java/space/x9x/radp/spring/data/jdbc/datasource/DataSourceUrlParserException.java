package space.x9x.radp.spring.data.jdbc.datasource;

import java.io.Serial;

/**
 * @author x9x
 * @since 2024-09-30 13:52
 */
public class DataSourceUrlParserException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public DataSourceUrlParserException(String message, Throwable cause) {
        super(message, cause);
    }
}
