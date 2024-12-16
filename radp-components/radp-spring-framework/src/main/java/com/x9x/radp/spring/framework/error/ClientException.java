package com.x9x.radp.spring.framework.error;

import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.PropertyKey;

/**
 * @author x9x
 * @since 2024-09-26 23:31
 */
@EqualsAndHashCode(callSuper = true)
public class ClientException extends BaseException {

    public ClientException(@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Throwable t) {
        super(errCode, t);
    }

    public ClientException(@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, String errMessage, Object... params) {
        super(errCode, errMessage, params);
    }

    public ClientException(@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... params) {
        super(errCode, params);
    }

    public ClientException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ClientException(ErrorCode errorCode, Object... params) {
        super(errorCode, params);
    }
}
