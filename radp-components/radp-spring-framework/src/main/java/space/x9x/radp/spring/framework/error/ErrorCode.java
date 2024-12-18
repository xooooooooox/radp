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

    public ErrorCode(@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... params) {
        this.code = errCode;
        this.message = ErrorCodeLoader.getErrMessage(errCode, params);
    }
}
