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

    public ServerException(@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Throwable t) {
        super(errCode, t);
    }

    public ServerException(@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, String messagePattern, Object... params) {
        super(errCode, messagePattern, params);
    }

    public ServerException(@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... params) {
        super(errCode, params);
    }

    public ServerException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ServerException(ErrorCode errorCode, Object... params) {
        super(errorCode, params);
    }
}
