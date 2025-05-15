package space.x9x.radp.commons.json.jackson.exception;

import java.io.Serial;

/**
 * @author x9x
 * @since 2024-09-23 13:54
 */
public class JacksonException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 9041976807836061564L;

    /**
     * Constructs a new JacksonException with the specified cause.
     * <p>
     * This exception is typically used to wrap Jackson-related exceptions
     * while preserving the original cause for debugging purposes.
     *
     * @param cause the cause of this exception
     */
    public JacksonException(Throwable cause) {
        super(cause);
    }
}
