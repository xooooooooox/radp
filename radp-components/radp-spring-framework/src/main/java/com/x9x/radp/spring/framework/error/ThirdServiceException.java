package com.x9x.radp.spring.framework.error;

import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.PropertyKey;

/**
 * @author x9x
 * @since 2024-09-26 23:49
 */
@EqualsAndHashCode(callSuper = true)
public class ThirdServiceException extends BaseException {

    public ThirdServiceException(@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Throwable t) {
        super(errCode, t);
    }

    public ThirdServiceException(@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, String errMessage, Object... params) {
        super(errCode, errMessage, params);
    }

    public ThirdServiceException(@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... params) {
        super(errCode, params);
    }

    public ThirdServiceException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ThirdServiceException(ErrorCode errorCode, Object... params) {
        super(errorCode, params);
    }
}
