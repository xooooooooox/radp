package space.x9x.radp.common.enums;

import space.x9x.radp.spring.framework.error.ErrorCode;
import space.x9x.radp.spring.framework.error.ErrorCodeLoader;
import lombok.Getter;
import org.jetbrains.annotations.PropertyKey;
import space.x9x.radp.spring.framework.error.GlobalResponseCode;

/**
 * 业务返回码枚举
 *
 * @author x9x
 * @see GlobalResponseCode
 * @since 2024-10-24 14:08
 */
@Getter
public enum ResponseCode {

    ;

    private final ErrorCode errorCode;

    ResponseCode(String code, String message) {
        this.errorCode = new ErrorCode(code, message);
    }

    ResponseCode(@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... params) {
        this.errorCode = new ErrorCode(errCode, params);
    }

    public String code() {
        return errorCode.getCode();
    }

    public String msg() {
        return errorCode.getMessage();
    }
}