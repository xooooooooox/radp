package space.x9x.radp.spring.framework.error;

import lombok.Getter;
import org.jetbrains.annotations.PropertyKey;

/**
 * 全局响应码
 * <p>
 * 使用 0-999 错误码段，和 HTTP 响应状态码 (opens new window)对应 <br/>
 * 对于项目组自定义错误码, 建议单独定义一个枚举类 {@code xx/types/enum/ResponseCode},且使用的错误码不要与这里的全局错误码重复 <br/>
 * 在定义错误码时, 你可以像 {@code GlobalResponseCode} 这样, 定义在 {@code META-INF/error/message.properties}中,
 * 也可以不定义在 {@code META-INF/error/message.properties}, 直接定义在枚举类中
 *
 * @author x9x
 * @since 2024-10-24 13:27
 */
@Getter
public enum GlobalResponseCode {

    SUCCESS("0"),

    // ========== 4xx ==========
    /**
     * Bad Request (HTTP 400).
     * The server cannot or will not process the request due to a client error,
     * such as malformed request syntax, invalid request message framing, or deceptive request routing.
     */
    BAD_REQUEST("400"),
    UNAUTHORIZED("401"),
    /**
     * Forbidden (HTTP 403).
     * The server understood the request but refuses to authorize it.
     * Unlike 401 Unauthorized, re-authenticating will make no difference.
     */
    FORBIDDEN("403"),
    NOT_FOUND("404"),
    METHOD_NOT_ALLOWED("405"),
    NOT_ACCEPTABLE("406"),
    REQUEST_TIMEOUT("408"),
    LOCKED("423"),
    TOO_MANY_REQUESTS("429"),

    // ========== 5xx ==========
    /**
     * Internal Server Error (HTTP 500).
     * The server has encountered a situation it doesn't know how to handle.
     * This is a generic server error response when no more specific message is suitable.
     */
    INTERNAL_SERVER_ERROR("500"),
    NOT_IMPLEMENTED("501"),
    SERVICE_UNAVAILABLE("503"),
    /**
     * Gateway Timeout (HTTP 504).
     * The server, while acting as a gateway or proxy, did not receive a timely response
     * from an upstream server it needed to access in order to complete the request.
     */
    GATEWAY_TIMEOUT("504"),

    UNKNOWN("999")
    ;

    private final ErrorCode errorCode;


    /**
     * 构造函数，用于创建带有指定错误代码和消息的GlobalResponseCode对象
     *
     * @param code    错误代码字符串
     * @param message 错误消息字符串
     */
    GlobalResponseCode(String code, String message) {
        this.errorCode = new ErrorCode(code, message);
    }

    /**
     * 构造函数，用于创建带有参数化消息的GlobalResponseCode对象
     *
     * @param errCode 错误代码，通过资源文件加载对应的错误消息
     * @param params  可变参数数组，用于格式化错误消息
     */
    GlobalResponseCode(@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... params) {
        this.errorCode = new ErrorCode(errCode, params);
    }

    /**
     * Returns the code of this response code.
     * This method provides access to the numeric code associated with this response.
     *
     * @return the string representation of the response code
     */
    public String code() {
        return errorCode.getCode();
    }

    /**
     * Returns the message of this response code.
     * This method provides access to the descriptive message associated with this response.
     *
     * @return the message describing this response code
     */
    public String message() {
        return errorCode.getMessage();
    }
}
