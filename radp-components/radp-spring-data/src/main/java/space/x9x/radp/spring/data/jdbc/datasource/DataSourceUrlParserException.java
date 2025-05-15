package space.x9x.radp.spring.data.jdbc.datasource;

/**
 * Exception thrown when there is an error parsing a data source URL.
 * This exception is used to wrap the original exception with additional context information.
 *
 * @author x9x
 * @since 2024-09-30 13:52
 */
public class DataSourceUrlParserException extends RuntimeException {
    /**
     * Constructs a new DataSourceUrlParserException with the specified detail message and cause.
     *
     * @param message the detail message (which is saved for later retrieval by the {@link #getMessage()} method)
     * @param cause   the cause (which is saved for later retrieval by the {@link #getCause()} method)
     */
    public DataSourceUrlParserException(String message, Throwable cause) {
        super(message, cause);
    }
}
