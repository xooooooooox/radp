package space.x9x.radp.spring.framework.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;
import space.x9x.radp.spring.framework.error.ErrorCode;
import space.x9x.radp.spring.framework.error.GlobalResponseCode;

/**
 * @author x9x
 * @since 2024-09-26 15:57
 */
@SuperBuilder(builderMethodName = "singleResultBuilder")
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Getter
public class SingleResult<T> extends Result {

    private T data;

    public static <T> SingleResult<T> build() {
        return build(null);
    }

    public static <T> SingleResult<T> build(T data) {
        return SingleResult.<T>singleResultBuilder()
                .success(true)
                .code(GlobalResponseCode.SUCCESS.code())
                .msg(GlobalResponseCode.SUCCESS.message())
                .data(data)
                .build();
    }

    public static <T> SingleResult<T> buildFailure(ErrorCode errorCode) {
        return SingleResult.<T>singleResultBuilder()
                .success(false)
                .code(errorCode.getCode())
                .msg(errorCode.getMessage())
                .build();
    }
}
