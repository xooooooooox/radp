package space.x9x.radp.spring.framework.error;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.PropertyKey;

/**
 * @author x9x
 * @since 2024-10-24 10:31
 */
@Data
@AllArgsConstructor
public class ErrorCode {

    private final String code;
    private final String message;

    /**
     * Constructs an ErrorCode with the specified error code and parameters.
     * The message is loaded from the resource bundle using the error code and formatted with the parameters.
     *
     * @param errCode the error code key in the resource bundle
     * @param params  the parameters to be used for message formatting
     */
    public ErrorCode(@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... params) {
        this.code = errCode;
        this.message = ErrorCodeLoader.getErrMessage(errCode, params);
    }
}
