package space.x9x.radp.spring.framework.error;

import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.PropertyKey;

import java.io.Serial;

/**
 * @author x9x
 * @since 2024-09-26 23:46
 */
@EqualsAndHashCode(callSuper = true)
public class ServerException extends BaseException {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new server exception with the specified error code and cause.
     *
     * @param errCode the error code from the resource bundle
     * @param t       the cause of this exception
     */
    public ServerException(@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Throwable t) {
        super(errCode, t);
    }

    /**
     * Constructs a new server exception with the specified error code, message pattern, and parameters.
     *
     * @param errCode the error code from the resource bundle
     * @param messagePattern the pattern for the error message
     * @param params the parameters to be used in the message pattern
     */
    public ServerException(@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, String messagePattern, Object... params) {
        super(errCode, messagePattern, params);
    }

    /**
     * Constructs a new server exception with the specified error code and parameters.
     *
     * @param errCode the error code from the resource bundle
     * @param params the parameters to be used in the error message
     */
    public ServerException(@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... params) {
        super(errCode, params);
    }

    /**
     * Constructs a new server exception with the specified error code.
     *
     * @param errorCode the error code object
     */
    public ServerException(ErrorCode errorCode) {
        super(errorCode);
    }

    /**
     * Constructs a new server exception with the specified error code and parameters.
     *
     * @param errorCode the error code object
     * @param params the parameters to be used in the error message
     */
    public ServerException(ErrorCode errorCode, Object... params) {
        super(errorCode, params);
    }
}
