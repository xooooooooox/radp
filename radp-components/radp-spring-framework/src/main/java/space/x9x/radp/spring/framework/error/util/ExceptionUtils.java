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

    public static ServerException serverException(Throwable t) {
        return new ServerException(GlobalResponseCode.UNKNOWN.getErrorCode(), t);
    }

    public static ServerException serverException(ErrorCode errorCode, Throwable t) {
        return new ServerException(errorCode, t);
    }

    public static ServerException serverException0(String messagePattern, Object... params) {
        return new ServerException(GlobalResponseCode.UNKNOWN.code(), messagePattern, params);
    }

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

    public static ServerException serverException(@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String code, Throwable t) {
        return new ServerException(code, t);
    }

    // ================== ClientException ==================

    public static ClientException clientException(ErrorCode errorCode) {
        return new ClientException(errorCode);
    }

    public static ClientException clientException(ErrorCode errorCode, Object... params) {
        return new ClientException(errorCode, params);
    }

    public static ClientException clientException(Throwable t) {
        return new ClientException(GlobalResponseCode.UNKNOWN.getErrorCode(), t);
    }

    public static ClientException clientException(ErrorCode errorCode, Throwable t) {
        return new ClientException(errorCode, t);
    }

    public static ClientException clientException0(String messagePattern, Object... params) {
        return new ClientException(GlobalResponseCode.UNKNOWN.code(), messagePattern, params);
    }

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
