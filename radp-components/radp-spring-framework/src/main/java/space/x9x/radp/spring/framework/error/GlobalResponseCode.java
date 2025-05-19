package space.x9x.radp.spring.framework.error;

import lombok.Getter;
import org.jetbrains.annotations.PropertyKey;

/**
 * 全局响应码
 * <p>
 * 使用 0-999 错误码段，和 HTTP 响应状态码 (opens new window)对应
 * <p>
 * 对于项目组自定义错误码, 建议单独定义一个枚举类 {@code xx/types/enum/ResponseCode},且使用的错误码不要与这里的全局错误码重复
 * <p>
 * 在定义错误码时, 你可以像 {@code GlobalResponseCode} 这样, 定义在 {@code META-INF/error/message.properties}中,
 * 也可以不定义在 {@code META-INF/error/message.properties}, 直接定义在枚举类中
 *
 * @author x9x
 * @since 2024-10-24 13:27
 */
@Getter
public enum GlobalResponseCode {

    /**
     * 成功响应码
     * 表示请求已成功处理
     */
    SUCCESS("0"),

    // ========== 4xx ==========
    /**
     * 错误请求
     * 表示由于语法无效，服务器无法理解该请求
     */
    BAD_REQUEST("400"),

    /**
     * 未授权
     * 表示请求需要用户认证
     */
    UNAUTHORIZED("401"),

    /**
     * 禁止访问
     * 表示服务器理解请求但拒绝执行
     */
    FORBIDDEN("403"),

    /**
     * 未找到
     * 表示服务器找不到请求的资源
     */
    NOT_FOUND("404"),

    /**
     * 方法不允许
     * 表示请求方法不被允许
     */
    METHOD_NOT_ALLOWED("405"),

    /**
     * 不可接受
     * 表示服务器无法根据请求的内容特性完成请求
     */
    NOT_ACCEPTABLE("406"),

    /**
     * 请求超时
     * 表示服务器等待客户端发送请求时超时
     */
    REQUEST_TIMEOUT("408"),

    /**
     * 资源被锁定
     * 表示请求的资源被锁定
     */
    LOCKED("423"),

    /**
     * 请求过多
     * 表示用户在给定的时间内发送了太多请求
     */
    TOO_MANY_REQUESTS("429"),

    // ========== 5xx ==========
    /**
     * 内部服务器错误
     * 表示服务器遇到了不知道如何处理的情况
     */
    INTERNAL_SERVER_ERROR("500"),

    /**
     * 未实现
     * 表示服务器不支持请求的功能
     */
    NOT_IMPLEMENTED("501"),

    /**
     * 服务不可用
     * 表示服务器暂时不可用（过载或维护）
     */
    SERVICE_UNAVAILABLE("503"),

    /**
     * 网关超时
     * 表示作为网关或代理的服务器未及时从上游服务器接收请求
     */
    GATEWAY_TIMEOUT("504"),

    /**
     * 未知错误
     * 表示发生了未分类的错误
     */
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
     * 获取错误代码
     *
     * @return 返回当前响应码对应的错误代码字符串
     */
    public String code() {
        return errorCode.getCode();
    }

    /**
     * 获取错误消息
     *
     * @return 返回当前响应码对应的错误消息字符串
     */
    public String message() {
        return errorCode.getMessage();
    }
}
