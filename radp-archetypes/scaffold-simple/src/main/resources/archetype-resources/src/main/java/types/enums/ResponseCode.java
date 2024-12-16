#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.types.enums;

import com.x9x.radp.spring.framework.error.ErrorCode;
import com.x9x.radp.spring.framework.error.ErrorCodeLoader;
import lombok.Getter;
import org.jetbrains.annotations.PropertyKey;

/**
 * 业务返回码枚举
 *
 * @author x9x
 * @see com.x9x.radp.spring.framework.error.GlobalResponseCode
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