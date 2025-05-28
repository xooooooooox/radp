package space.x9x.radp.spring.framework.error;

import java.io.Serial;

import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.PropertyKey;

/**
 * @author IO x9x
 * @since 2024-09-26 23:49
 */
@EqualsAndHashCode(callSuper = true)
public class ThirdServiceException extends BaseException {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new ThirdServiceException with the specified error code and cause.
     *
     * @param errCode the error code from the resource bundle
     * @param t       the cause of this exception
     */
    public ThirdServiceException(@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Throwable t) {
        super(errCode, t);
    }

    /**
     * Constructs a new ThirdServiceException with the specified error code, error message, and parameters.
     *
     * @param errCode the error code from the resource bundle
     * @param errMessage the error message
     * @param params the parameters to be used in the error message
     */
    public ThirdServiceException(@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, String errMessage, Object... params) {
        super(errCode, errMessage, params);
    }

    /**
     * Constructs a new ThirdServiceException with the specified error code and parameters.
     *
     * @param errCode the error code from the resource bundle
     * @param params the parameters to be used in the error message
     */
    public ThirdServiceException(@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... params) {
        super(errCode, params);
    }

    /**
     * Constructs a new ThirdServiceException with the specified error code.
     *
     * @param errorCode the error code object
     */
    public ThirdServiceException(ErrorCode errorCode) {
        super(errorCode);
    }

    /**
     * Constructs a new ThirdServiceException with the specified error code and parameters.
     *
     * @param errorCode the error code object
     * @param params the parameters to be used in the error message
     */
    public ThirdServiceException(ErrorCode errorCode, Object... params) {
        super(errorCode, params);
    }
}
