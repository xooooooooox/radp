package space.x9x.radp.spring.framework.error.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.PropertyKey;

import space.x9x.radp.spring.framework.error.ClientException;
import space.x9x.radp.spring.framework.error.ErrorCode;
import space.x9x.radp.spring.framework.error.ErrorCodeLoader;
import space.x9x.radp.spring.framework.error.GlobalResponseCode;
import space.x9x.radp.spring.framework.error.ServerException;
import space.x9x.radp.spring.framework.error.ThirdServiceException;

/**
 * @author IO x9x
 * @since 2024-09-27 11:56
 */
@Slf4j
@UtilityClass
public class ExceptionUtils {


    // ================== ServerException ==================

    /**
     * Creates a ServerException with the specified ErrorCode.
     * This method creates a new ServerException using the provided ErrorCode object,
     * which contains both the error code and message.
     *
     * @param errorCode the ErrorCode object containing the error code and message
     * @return a new ServerException with the specified error code and message
     */
    public static ServerException serverException(ErrorCode errorCode) {
        return new ServerException(errorCode);
    }

    /**
     * Creates a ServerException with the specified ErrorCode and parameters.
     * This method creates a new ServerException using the provided ErrorCode object
     * and parameters for message formatting.
     *
     * @param errorCode the ErrorCode object containing the error code and message template
     * @param params    the parameters to be substituted in the message template
     * @return a new ServerException with the specified error code and formatted message
     */
    public static ServerException serverException(ErrorCode errorCode, Object... params) {
        return new ServerException(errorCode, params);
    }

    /**
     * Creates a ServerException from a Throwable.
     * This method creates a new ServerException with an UNKNOWN error code
     * and the provided Throwable as the cause.
     *
     * @param t the Throwable to wrap in a ServerException
     * @return a new ServerException with an UNKNOWN error code and the specified cause
     */
    public static ServerException serverException(Throwable t) {
        return new ServerException(GlobalResponseCode.UNKNOWN.getErrorCode(), t);
    }

    /**
     * Creates a ServerException with the specified ErrorCode and cause.
     * This method creates a new ServerException using the provided ErrorCode object
     * and the specified Throwable as the cause.
     *
     * @param errorCode the ErrorCode object containing the error code and message
     * @param t the Throwable to set as the cause of the exception
     * @return a new ServerException with the specified error code, message, and cause
     */
    public static ServerException serverException(ErrorCode errorCode, Throwable t) {
        return new ServerException(errorCode, t);
    }

    /**
     * Creates a ServerException with a custom message pattern and parameters.
     * This method creates a new ServerException with an UNKNOWN error code
     * and a message formatted using the provided pattern and parameters.
     * The '0' suffix in the method name indicates this is a direct message formatting method.
     *
     * @param messagePattern the pattern for formatting the error message
     * @param params the parameters to be substituted in the message pattern
     * @return a new ServerException with an UNKNOWN error code and the formatted message
     */
    public static ServerException serverException0(String messagePattern, Object... params) {
        return new ServerException(GlobalResponseCode.UNKNOWN.code(), messagePattern, params);
    }

    /**
     * Creates a ServerException with the specified error code and parameters.
     * This method creates a new ServerException using an error code from the resource bundle
     * and parameters for message formatting.
     *
     * @param code the error code that identifies the error message in the resource bundle
     * @param params the parameters to be substituted in the error message
     * @return a new ServerException with the specified error code and formatted message
     */
    public static ServerException serverException(@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String code, Object... params) {
        return new ServerException(code, params);
    }

    /**
     * Create a ServerException with a pre-formatted message.
     * This method is used when the message is already formatted and should not be formatted again.
     *
     * @param code             the error code
     * @param formattedMessage the pre-formatted message
     * @return the created exception
     */
    public static ServerException serverExceptionWithFormattedMessage(@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String code, String formattedMessage) {
        return new ServerException(code, formattedMessage);
    }

    /**
     * Creates a ServerException with the specified error code and cause.
     * This method creates a new ServerException using an error code from the resource bundle
     * and the specified Throwable as the cause.
     *
     * @param code the error code that identifies the error message in the resource bundle
     * @param t the Throwable to set as the cause of the exception
     * @return a new ServerException with the specified error code, message, and cause
     */
    public static ServerException serverException(@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String code, Throwable t) {
        return new ServerException(code, t);
    }

    // ================== ClientException ==================

    /**
     * Creates a ClientException with the specified ErrorCode.
     * This method creates a new ClientException using the provided ErrorCode object,
     * which contains both the error code and message.
     *
     * @param errorCode the ErrorCode object containing the error code and message
     * @return a new ClientException with the specified error code and message
     */
    public static ClientException clientException(ErrorCode errorCode) {
        return new ClientException(errorCode);
    }

    /**
     * Creates a ClientException with the specified ErrorCode and parameters.
     * This method creates a new ClientException using the provided ErrorCode object
     * and parameters for message formatting.
     *
     * @param errorCode the ErrorCode object containing the error code and message template
     * @param params the parameters to be substituted in the message template
     * @return a new ClientException with the specified error code and formatted message
     */
    public static ClientException clientException(ErrorCode errorCode, Object... params) {
        return new ClientException(errorCode, params);
    }

    /**
     * Creates a ClientException from a Throwable.
     * This method creates a new ClientException with an UNKNOWN error code
     * and the provided Throwable as the cause.
     *
     * @param t the Throwable to wrap in a ClientException
     * @return a new ClientException with an UNKNOWN error code and the specified cause
     */
    public static ClientException clientException(Throwable t) {
        return new ClientException(GlobalResponseCode.UNKNOWN.getErrorCode(), t);
    }

    /**
     * Creates a ClientException with the specified ErrorCode and cause.
     * This method creates a new ClientException using the provided ErrorCode object
     * and the specified Throwable as the cause.
     *
     * @param errorCode the ErrorCode object containing the error code and message
     * @param t the Throwable to set as the cause of the exception
     * @return a new ClientException with the specified error code, message, and cause
     */
    public static ClientException clientException(ErrorCode errorCode, Throwable t) {
        return new ClientException(errorCode, t);
    }

    /**
     * Creates a ClientException with a custom message pattern and parameters.
     * This method creates a new ClientException with an UNKNOWN error code
     * and a message formatted using the provided pattern and parameters.
     * The '0' suffix in the method name indicates this is a direct message formatting method.
     *
     * @param messagePattern the pattern for formatting the error message
     * @param params the parameters to be substituted in the message pattern
     * @return a new ClientException with an UNKNOWN error code and the formatted message
     */
    public static ClientException clientException0(String messagePattern, Object... params) {
        return new ClientException(GlobalResponseCode.UNKNOWN.code(), messagePattern, params);
    }

    /**
     * Creates a ClientException with the specified error code and parameters.
     * This method creates a new ClientException using an error code from the resource bundle
     * and parameters for message formatting.
     *
     * @param code the error code that identifies the error message in the resource bundle
     * @param params the parameters to be substituted in the error message
     * @return a new ClientException with the specified error code and formatted message
     */
    public static ClientException clientException(@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String code, Object... params) {
        return new ClientException(code, params);
    }

    /**
     * Create a ClientException with a pre-formatted message.
     * This method is used when the message is already formatted and should not be formatted again.
     *
     * @param code             the error code
     * @param formattedMessage the pre-formatted message
     * @return the created exception
     */
    public static ClientException clientExceptionWithFormattedMessage(@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String code, String formattedMessage) {
        return new ClientException(code, formattedMessage);
    }

    /**
     * Creates a ClientException with the specified error code and cause.
     * This method creates a new ClientException using an error code from the resource bundle
     * and the specified Throwable as the cause.
     *
     * @param code the error code that identifies the error message in the resource bundle
     * @param t the Throwable to set as the cause of the exception
     * @return a new ClientException with the specified error code, message, and cause
     */
    public static ClientException clientException(@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String code, Throwable t) {
        return new ClientException(code, t);
    }

    // ================== ThirdServiceException ==================

    /**
     * Creates a ThirdServiceException with the specified ErrorCode.
     * This method creates a new ThirdServiceException using the provided ErrorCode object,
     * which contains both the error code and message.
     *
     * @param errorCode the ErrorCode object containing the error code and message
     * @return a new ThirdServiceException with the specified error code and message
     */
    public static ThirdServiceException thirdServiceException(ErrorCode errorCode) {
        return new ThirdServiceException(errorCode);
    }

    /**
     * Creates a ThirdServiceException with the specified ErrorCode and parameters.
     * This method creates a new ThirdServiceException using the provided ErrorCode object
     * and parameters for message formatting.
     *
     * @param errorCode the ErrorCode object containing the error code and message template
     * @param params    the parameters to be substituted in the message template
     * @return a new ThirdServiceException with the specified error code and formatted message
     */
    public static ThirdServiceException thirdServiceException(ErrorCode errorCode, Object... params) {
        return new ThirdServiceException(errorCode, params);
    }

    /**
     * Creates a ThirdServiceException from a Throwable.
     * This method creates a new ThirdServiceException with an UNKNOWN error code
     * and the provided Throwable as the cause.
     *
     * @param t the Throwable to wrap in a ThirdServiceException
     * @return a new ThirdServiceException with an UNKNOWN error code and the specified cause
     */
    public static ThirdServiceException thirdServiceException(Throwable t) {
        return new ThirdServiceException(GlobalResponseCode.UNKNOWN.getErrorCode(), t);
    }

    /**
     * Creates a ThirdServiceException with the specified ErrorCode and cause.
     * This method creates a new ThirdServiceException using the provided ErrorCode object
     * and the specified Throwable as the cause.
     *
     * @param errorCode the ErrorCode object containing the error code and message
     * @param t the Throwable to set as the cause of the exception
     * @return a new ThirdServiceException with the specified error code, message, and cause
     */
    public static ThirdServiceException thirdServiceException(ErrorCode errorCode, Throwable t) {
        return new ThirdServiceException(errorCode, t);
    }

    /**
     * Creates a ThirdServiceException with a custom message pattern and parameters.
     * This method creates a new ThirdServiceException with an UNKNOWN error code
     * and a message formatted using the provided pattern and parameters.
     * The '0' suffix in the method name indicates this is a direct message formatting method.
     *
     * @param messagePattern the pattern for formatting the error message
     * @param params the parameters to be substituted in the message pattern
     * @return a new ThirdServiceException with an UNKNOWN error code and the formatted message
     */
    public static ThirdServiceException thirdServiceException0(String messagePattern, Object... params) {
        return new ThirdServiceException(GlobalResponseCode.UNKNOWN.code(), messagePattern, params);
    }

    /**
     * Creates a ThirdServiceException with the specified error code and parameters.
     * This method creates a new ThirdServiceException using an error code from the resource bundle
     * and parameters for message formatting.
     *
     * @param code the error code that identifies the error message in the resource bundle
     * @param params the parameters to be substituted in the error message
     * @return a new ThirdServiceException with the specified error code and formatted message
     */
    public static ThirdServiceException thirdServiceException(@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String code, Object... params) {
        return new ThirdServiceException(code, params);
    }

    /**
     * Create a ThirdServiceException with a pre-formatted message.
     * This method is used when the message is already formatted and should not be formatted again.
     *
     * @param code             the error code
     * @param formattedMessage the pre-formatted message
     * @return the created exception
     */
    public static ThirdServiceException thirdServiceExceptionWithFormattedMessage(@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String code, String formattedMessage) {
        return new ThirdServiceException(code, formattedMessage);
    }

    /**
     * Creates a ThirdServiceException with the specified error code and cause.
     * This method creates a new ThirdServiceException using an error code from the resource bundle
     * and the specified Throwable as the cause.
     *
     * @param code the error code that identifies the error message in the resource bundle
     * @param t the Throwable to set as the cause of the exception
     * @return a new ThirdServiceException with the specified error code, message, and cause
     */
    public static ThirdServiceException thirdServiceException(@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String code, Throwable t) {
        return new ThirdServiceException(code, t);
    }
}
