package space.x9x.radp.spring.data.jdbc.datasource;

import java.io.Serial;

/**
 * @author x9x
 * @since 2024-09-30 13:52
 */
public class DataSourceUrlParserException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new DataSourceUrlParserException with the specified detail message and cause.
     *
     * @param message the detail message (which is saved for later retrieval by the getMessage() method)
     * @param cause   the cause (which is saved for later retrieval by the getCause() method)
     */
    public DataSourceUrlParserException(String message, Throwable cause) {
        super(message, cause);
    }
}
