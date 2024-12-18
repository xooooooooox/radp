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
    BAD_REQUEST("400"),
    UNAUTHORIZED("401"),
    FORBIDDEN("403"),
    NOT_FOUND("404"),
    METHOD_NOT_ALLOWED("405"),
    NOT_ACCEPTABLE("406"),
    REQUEST_TIMEOUT("408"),
    LOCKED("423"),
    TOO_MANY_REQUESTS("429"),

    // ========== 5xx ==========
    INTERNAL_SERVER_ERROR("500"),
    NOT_IMPLEMENTED("501"),
    SERVICE_UNAVAILABLE("503"),
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

    public String code() {
        return errorCode.getCode();
    }

    public String message() {
        return errorCode.getMessage();
    }
}
