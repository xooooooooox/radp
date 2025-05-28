package space.x9x.radp.spring.framework.error;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.PropertyKey;

/**
 * @author IO x9x
 * @since 2024-10-24 10:31
 */
@Data
@AllArgsConstructor
public class ErrorCode {

    private final String code;
    private final String message;

    /**
     * Constructs a new ErrorCode with the specified error code and parameters.
     * The error message is loaded from the resource bundle using the error code,
     * and the parameters are used for message formatting.
     *
     * @param errCode the error code that identifies the error message in the resource bundle
     * @param params  the parameters to be substituted in the error message
     */
    public ErrorCode(@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... params) {
        this.code = errCode;
        this.message = ErrorCodeLoader.getErrMessage(errCode, params);
    }
}
