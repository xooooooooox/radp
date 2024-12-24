package space.x9x.radp.spring.framework.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;
import space.x9x.radp.spring.framework.error.ErrorCode;
import space.x9x.radp.spring.framework.error.GlobalResponseCode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author x9x
 * @since 2024-09-26 16:02
 */
@SuperBuilder(builderMethodName = "pageResultBuilder")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Getter
@SuppressWarnings("java:S1948")
public class PageResult<T> extends Result {

    private Collection<T> data;
    private Long total = 0L;

    public Collection<T> getData() {
        if (data == null) {
            return Collections.emptyList();
        }
        if (data instanceof List) {
            return data;
        }
        return new ArrayList<>(data);
    }

    public static <T> PageResult<T> build(Collection<T> data, Long total) {
        return PageResult.<T>pageResultBuilder()
                .success(true)
                .code(GlobalResponseCode.SUCCESS.code())
                .msg(GlobalResponseCode.SUCCESS.message())
                .data(data)
                .total(total)
                .build();
    }

    public static <T> PageResult<T> buildFailure(ErrorCode errorCode) {
        return PageResult.<T>pageResultBuilder()
                .success(false)
                .code(errorCode.getCode())
                .msg(errorCode.getMessage())
                .build();
    }
}
