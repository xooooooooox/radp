package space.x9x.radp.spring.framework.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import space.x9x.radp.spring.framework.error.ErrorCode;
import space.x9x.radp.spring.framework.error.GlobalResponseCode;

import java.util.Collection;
import java.util.Collections;

/**
 * @author x9x
 * @since 2025-03-18 13:16
 */
@SuperBuilder(builderMethodName = "multiResultBuilder")
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Getter
public class MultiResult<T> extends Result {

    private Collection<T> data;

    public static <T> MultiResult<T> build() {
        return build(Collections.emptyList());
    }

    public static <T> MultiResult<T> build(Collection<T> data) {
        return MultiResult.<T>multiResultBuilder()
                .success(true)
                .code(GlobalResponseCode.SUCCESS.code())
                .msg(GlobalResponseCode.SUCCESS.message())
                .data(data)
                .build();
    }

    public static <T> MultiResult<T> buildFailure(ErrorCode errorCode) {
        return MultiResult.<T>multiResultBuilder()
                .success(false)
                .code(errorCode.getCode())
                .msg(errorCode.getMessage())
                .build();
    }
}
