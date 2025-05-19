package space.x9x.radp.spring.framework.error;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.jetbrains.annotations.PropertyKey;
import space.x9x.radp.commons.lang.format.MessageFormatter;

/**
 * Base exception class for application-specific exceptions.
 * This class extends RuntimeException and provides additional functionality for
 * handling error codes, error messages, and parameters for message formatting.
 * It supports various ways of creating exceptions with error codes and messages.
 *
 * @author x9x
 * @since 2024-09-26 23:04
 */
@Getter
@EqualsAndHashCode(callSuper = true)
public class BaseException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final String errCode;
    private final String errMessage;
    private Object[] params;

    /**
     * Constructs a new BaseException with the specified error code and cause.
     * The error message is loaded from the resource bundle using the error code.
     *
     * @param errCode the error code that identifies the error message in the resource bundle
     * @param t       the cause of the exception
     */
    public BaseException(@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode,
                         Throwable t) {
        super(ErrorCodeLoader.getErrMessage(errCode), t);
        // The super constructor takes both message and cause, ensuring the exception's stack trace is preserved
        // We get the error message without trying to replace placeholders with the exception message
        this.errCode = errCode;
        this.errMessage = ErrorCodeLoader.getErrMessage(errCode);
    }

    /**
     * Constructs a new BaseException with the specified error code, message pattern, and parameters.
     * The message pattern can contain placeholders that will be replaced by the parameters.
     * If the last parameter is a Throwable, it will be set as the cause of the exception.
     *
     * @param errCode the error code that identifies the error
     * @param messagePattern the message pattern with placeholders
     * @param params the parameters to replace placeholders in the message pattern
     */
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

    /**
     * Constructs a new BaseException with the specified error code and parameters.
     * The error message is loaded from the resource bundle using the error code,
     * and the parameters are used to replace placeholders in the message.
     * If the last parameter is a Throwable, it will be set as the cause of the exception.
     *
     * @param errCode the error code that identifies the error message in the resource bundle
     * @param params the parameters to replace placeholders in the error message
     */
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

    /**
     * Constructs a new BaseException with the specified ErrorCode object.
     * The error code and message are extracted from the ErrorCode object.
     *
     * @param errorCode the ErrorCode object containing the error code and message
     */
    public BaseException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errCode = errorCode.getCode();
        this.errMessage = errorCode.getMessage();
    }

    /**
     * Constructs a new BaseException with the specified ErrorCode object and parameters.
     * The error code and message are extracted from the ErrorCode object,
     * and the parameters can be used for additional context.
     * If the last parameter is a Throwable, it will be set as the cause of the exception.
     *
     * @param errorCode the ErrorCode object containing the error code and message
     * @param params the parameters for additional context or to replace placeholders
     */
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

    /**
     * Constructs a new BaseException with the specified ErrorCode object and cause.
     * The error code is extracted from the ErrorCode object, and the error message
     * is formatted by combining the ErrorCode message with the cause's message.
     *
     * @param errorCode the ErrorCode object containing the error code and message template
     * @param t the cause of the exception, whose message will be included in the formatted message
     */
    public BaseException(ErrorCode errorCode, Throwable t) {
        super(MessageFormatter.format(errorCode.getMessage(), t.getMessage()).getMessage());
        this.errCode = errorCode.getCode();
        this.errMessage = MessageFormatter.format(errorCode.getMessage(), t.getMessage()).getMessage();
    }
}
