package com.x9x.radp.spring.framework.error;

import com.x9x.radp.commons.lang.format.MessageFormatter;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.jetbrains.annotations.PropertyKey;

/**
 * @author x9x
 * @since 2024-09-26 23:04
 */
@Getter
@EqualsAndHashCode(callSuper = true)
public class BaseException extends RuntimeException {

    private final String errCode;
    private final String errMessage;
    private Object[] params;

    public BaseException(@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode,
                         Throwable t) {
        super(ErrorCodeLoader.getErrMessage(errCode, t.getMessage()));
        this.errCode = errCode;
        this.errMessage = ErrorCodeLoader.getErrMessage(errCode, t.getMessage());
    }

    public BaseException(@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode,
                         String messagePattern,
                         Object... params) {
        super(messagePattern);
        this.errCode = errCode;
        this.errMessage = messagePattern;
        this.params = params;
    }

    public BaseException(@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... params) {
        super(ErrorCodeLoader.getErrMessage(errCode));
        this.errCode = errCode;
        this.errMessage = ErrorCodeLoader.getErrMessage(errCode, params);
    }

    public BaseException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errCode = errorCode.getCode();
        this.errMessage = errorCode.getMessage();
    }

    public BaseException(ErrorCode errorCode, Object... params) {
        super(errorCode.getMessage());
        this.errCode = errorCode.getCode();
        this.errMessage = errorCode.getMessage();
        this.params = params;
    }

    public BaseException(ErrorCode errorCode, Throwable t) {
        super(MessageFormatter.format(errorCode.getMessage(), t.getMessage()).getMessage());
        this.errCode = errorCode.getCode();
        this.errMessage = MessageFormatter.format(errorCode.getMessage(), t.getMessage()).getMessage();
    }
}
