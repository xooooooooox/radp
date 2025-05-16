package space.x9x.radp.types.exception;

import org.jetbrains.annotations.PropertyKey;
import space.x9x.radp.spring.framework.error.ErrorCode;
import space.x9x.radp.spring.framework.error.ServerException;

/**
 * @author x9x
 * @since 2025-01-14 23:18
 */
public class AppException extends ServerException {

    public AppException(@PropertyKey(resourceBundle = "META-INF.error.message") String errCode, Throwable t) {
        super(errCode, t);
    }

    public AppException(@PropertyKey(resourceBundle = "META-INF.error.message") String errCode, String messagePattern, Object... params) {
        super(errCode, messagePattern, params);
    }

    public AppException(@PropertyKey(resourceBundle = "META-INF.error.message") String errCode, Object... params) {
        super(errCode, params);
    }

    public AppException(ErrorCode errorCode) {
        super(errorCode);
    }

    public AppException(ErrorCode errorCode, Object... params) {
        super(errorCode, params);
    }
}
