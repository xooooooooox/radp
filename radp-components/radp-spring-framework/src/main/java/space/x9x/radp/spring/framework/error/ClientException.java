package space.x9x.radp.spring.framework.error;

import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.PropertyKey;

/**
 * @author x9x
 * @since 2024-09-26 23:31
 */
@EqualsAndHashCode(callSuper = true)
public class ClientException extends BaseException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new ClientException with the specified error code and cause.
     *
     * @param errCode the error code from the resource bundle
     * @param t       the cause of this exception
     */
    public ClientException(@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Throwable t) {
        super(errCode, t);
    }

    /**
     * Constructs a new ClientException with the specified error code, custom error message, and parameters.
     *
     * @param errCode    the error code from the resource bundle
     * @param errMessage the custom error message
     * @param params     the parameters to be used for message formatting
     */
    public ClientException(@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, String errMessage, Object... params) {
        super(errCode, errMessage, params);
    }

    /**
     * Constructs a new ClientException with the specified error code and parameters.
     * The error message will be loaded from the resource bundle using the error code.
     *
     * @param errCode the error code from the resource bundle
     * @param params the parameters to be used for message formatting
     */
    public ClientException(@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... params) {
        super(errCode, params);
    }

    /**
     * Constructs a new ClientException with the specified ErrorCode object.
     * This constructor uses the error code and message from the ErrorCode object.
     *
     * @param errorCode the ErrorCode object containing the error code and message
     */
    public ClientException(ErrorCode errorCode) {
        super(errorCode);
    }

    /**
     * Constructs a new ClientException with the specified ErrorCode object and parameters.
     * This constructor uses the error code from the ErrorCode object and formats the message
     * with the provided parameters.
     *
     * @param errorCode the ErrorCode object containing the error code and message template
     * @param params the parameters to be used for message formatting
     */
    public ClientException(ErrorCode errorCode, Object... params) {
        super(errorCode, params);
    }
}
