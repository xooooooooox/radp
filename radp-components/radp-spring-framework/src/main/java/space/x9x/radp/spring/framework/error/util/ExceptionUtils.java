package space.x9x.radp.spring.framework.error.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.PropertyKey;
import space.x9x.radp.spring.framework.error.*;

/**
 * @author x9x
 * @since 2024-09-27 11:56
 */
@Slf4j
@UtilityClass
public class ExceptionUtils {


    // ================== ServerException ==================

    public static ServerException serverException(ErrorCode errorCode) {
        return new ServerException(errorCode);
    }

    public static ServerException serverException(ErrorCode errorCode, Object... params) {
        return new ServerException(errorCode, params);
    }

    /**
     * Creates a ServerException with the UNKNOWN error code and the specified cause.
     * This method is useful when you want to wrap an existing exception as a ServerException.
     *
     * @param t the cause of the exception
     * @return a new ServerException with the UNKNOWN error code and the specified cause
     */
    public static ServerException serverException(Throwable t) {
        return new ServerException(GlobalResponseCode.UNKNOWN.getErrorCode(), t);
    }

    public static ServerException serverException(ErrorCode errorCode, Throwable t) {
        return new ServerException(errorCode, t);
    }

    public static ServerException serverException0(String messagePattern, Object... params) {
        return new ServerException(GlobalResponseCode.UNKNOWN.code(), messagePattern, params);
    }

    /**
     * Creates a ServerException with the specified error code and parameters.
     * The error message is loaded from the resource bundle using the error code and formatted with the parameters.
     *
     * @param code   the error code key in the resource bundle
     * @param params the parameters to be used for message formatting
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
     * The error message is loaded from the resource bundle using the error code.
     * This method is useful when you want to wrap an existing exception with a specific error code.
     *
     * @param code the error code key in the resource bundle
     * @param t the cause of the exception
     * @return a new ServerException with the specified error code and cause
     */
    public static ServerException serverException(@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String code, Throwable t) {
        return new ServerException(code, t);
    }

    // ================== ClientException ==================

    /**
     * Creates a ClientException with the specified error code.
     * This method is useful when you want to create a client-side exception with a predefined error code.
     *
     * @param errorCode the error code object containing code and message
     * @return a new ClientException with the specified error code
     */
    public static ClientException clientException(ErrorCode errorCode) {
        return new ClientException(errorCode);
    }

    /**
     * Creates a ClientException with the specified error code and parameters.
     * The parameters are used to format the error message associated with the error code.
     *
     * @param errorCode the error code object containing code and message template
     * @param params the parameters to be used for message formatting
     * @return a new ClientException with the specified error code and formatted message
     */
    public static ClientException clientException(ErrorCode errorCode, Object... params) {
        return new ClientException(errorCode, params);
    }

    /**
     * Creates a ClientException with the UNKNOWN error code and the specified cause.
     * This method is useful when you want to wrap an existing exception as a ClientException.
     *
     * @param t the cause of the exception
     * @return a new ClientException with the UNKNOWN error code and the specified cause
     */
    public static ClientException clientException(Throwable t) {
        return new ClientException(GlobalResponseCode.UNKNOWN.getErrorCode(), t);
    }

    /**
     * Creates a ClientException with the specified error code and cause.
     * This method is useful when you want to wrap an existing exception with a specific error code.
     *
     * @param errorCode the error code object containing code and message
     * @param t the cause of the exception
     * @return a new ClientException with the specified error code and cause
     */
    public static ClientException clientException(ErrorCode errorCode, Throwable t) {
        return new ClientException(errorCode, t);
    }

    /**
     * Creates a ClientException with the UNKNOWN error code and a custom message pattern.
     * This method allows direct specification of the message pattern without using a resource bundle.
     *
     * @param messagePattern the message pattern to use for the exception message
     * @param params the parameters to be used for message formatting
     * @return a new ClientException with the UNKNOWN error code and the formatted message
     */
    public static ClientException clientException0(String messagePattern, Object... params) {
        return new ClientException(GlobalResponseCode.UNKNOWN.code(), messagePattern, params);
    }

    /**
     * Creates a ClientException with the specified error code and parameters.
     * The error message is loaded from the resource bundle using the error code and formatted with the parameters.
     *
     * @param code the error code key in the resource bundle
     * @param params the parameters to be used for message formatting
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
     * The error message is loaded from the resource bundle using the error code.
     * This method is useful when you want to wrap an existing exception with a specific error code.
     *
     * @param code the error code key in the resource bundle
     * @param t the cause of the exception
     * @return a new ClientException with the specified error code and cause
     */
    public static ClientException clientException(@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String code, Throwable t) {
        return new ClientException(code, t);
    }

    // ================== ThirdServiceException ==================

    public static ThirdServiceException thirdServiceException(ErrorCode errorCode) {
        return new ThirdServiceException(errorCode);
    }

    public static ThirdServiceException thirdServiceException(ErrorCode errorCode, Object... params) {
        return new ThirdServiceException(errorCode, params);
    }

    public static ThirdServiceException thirdServiceException(Throwable t) {
        return new ThirdServiceException(GlobalResponseCode.UNKNOWN.getErrorCode(), t);
    }

    public static ThirdServiceException thirdServiceException(ErrorCode errorCode, Throwable t) {
        return new ThirdServiceException(errorCode, t);
    }

    public static ThirdServiceException thirdServiceException0(String messagePattern, Object... params) {
        return new ThirdServiceException(GlobalResponseCode.UNKNOWN.code(), messagePattern, params);
    }

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

    public static ThirdServiceException thirdServiceException(@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String code, Throwable t) {
        return new ThirdServiceException(code, t);
    }
}
