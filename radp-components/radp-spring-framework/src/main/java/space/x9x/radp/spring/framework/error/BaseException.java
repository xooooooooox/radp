/*
 * Copyright 2012-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package space.x9x.radp.spring.framework.error;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.jetbrains.annotations.PropertyKey;
import space.x9x.radp.commons.lang.format.MessageFormatter;

/**
 * Base exception class for all application-specific exceptions.
 * This class provides standardized error handling with error codes, messages, and optional parameters.
 * It serves as the parent class for more specific exception types.
 *
 * @author x9x
 * @since 2024-09-26 23:04
 */
@Getter
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("java:S1948")
public class BaseException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * The error code that identifies this exception type
     */
    private final String errCode;

    /**
     * The human-readable error message
     */
    private final String errMessage;

    /**
     * Optional parameters used for message formatting or additional context
     */
    private Object[] params;

    /**
     * Constructs a new BaseException with the specified error code and throwable cause.
     * The error message is loaded from the resource bundle using the error code.
     *
     * @param errCode the error code that identifies the error message in the resource bundle
     * @param t       the throwable cause of this exception
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
     * The message pattern is used directly as the error message, and the parameters can be used for
     * message formatting or to provide a cause for the exception.
     *
     * @param errCode        the error code that identifies the error
     * @param messagePattern the message pattern to use as the error message
     * @param params         the parameters to be used for message formatting or to provide a cause
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
     * and the parameters are used for message formatting.
     *
     * @param errCode the error code that identifies the error message in the resource bundle
     * @param params  the parameters to be substituted in the error message
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
     * and the parameters are used for message formatting.
     *
     * @param errorCode the ErrorCode object containing the error code and message
     * @param params    the parameters to be substituted in the error message
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
     * Constructs a new BaseException with the specified ErrorCode object and throwable cause.
     * The error code is extracted from the ErrorCode object, and the error message is formatted
     * with the throwable's message.
     *
     * @param errorCode the ErrorCode object containing the error code and message template
     * @param t         the throwable cause of this exception
     */
    public BaseException(ErrorCode errorCode, Throwable t) {
        super(MessageFormatter.format(errorCode.getMessage(), t.getMessage()).getMessage());
        this.errCode = errorCode.getCode();
        this.errMessage = MessageFormatter.format(errorCode.getMessage(), t.getMessage()).getMessage();
    }
}
