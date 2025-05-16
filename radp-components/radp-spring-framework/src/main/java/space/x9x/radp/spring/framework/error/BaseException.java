package space.x9x.radp.spring.framework.error;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.jetbrains.annotations.PropertyKey;
import space.x9x.radp.commons.lang.format.MessageFormatter;

import java.io.Serial;

/**
 * @author x9x
 * @since 2024-09-26 23:04
 */
@Getter
@EqualsAndHashCode(callSuper = true)
public class BaseException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    private final String errCode;
    private final String errMessage;
    private Object[] params;

    public BaseException(@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode,
                         Throwable t) {
        super(ErrorCodeLoader.getErrMessage(errCode), t);
        // The super constructor takes both message and cause, ensuring the exception's stack trace is preserved
        // We get the error message without trying to replace placeholders with the exception message
        this.errCode = errCode;
        this.errMessage = ErrorCodeLoader.getErrMessage(errCode);
    }

    public BaseException(@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode,
                         String messagePattern,
                         Object... params) {
        super(messagePattern);
        this.errCode = errCode;
        this.errMessage = messagePattern;
        this.params = params;

        // Check if the last parameter is a Throwable and set it as the cause
        Throwable throwableCandidate = MessageFormatter.getThrowableCandidate(params);
        if (throwableCandidate != null) {
            this.initCause(throwableCandidate);
        }
    }

    public BaseException(@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... params) {
        super(ErrorCodeLoader.getErrMessage(errCode, params));
        this.errCode = errCode;
        this.errMessage = ErrorCodeLoader.getErrMessage(errCode, params);
        this.params = params;

        // Check if the last parameter is a Throwable and set it as the cause
        Throwable throwableCandidate = MessageFormatter.getThrowableCandidate(params);
        if (throwableCandidate != null) {
            this.initCause(throwableCandidate);
        }
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

        // Check if the last parameter is a Throwable and set it as the cause
        Throwable throwableCandidate = MessageFormatter.getThrowableCandidate(params);
        if (throwableCandidate != null) {
            this.initCause(throwableCandidate);
        }
    }

    public BaseException(ErrorCode errorCode, Throwable t) {
        super(MessageFormatter.format(errorCode.getMessage(), t.getMessage()).getMessage());
        this.errCode = errorCode.getCode();
        this.errMessage = MessageFormatter.format(errorCode.getMessage(), t.getMessage()).getMessage();
    }
}
