package space.x9x.radp.spring.framework.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import space.x9x.radp.spring.framework.error.ErrorCode;
import space.x9x.radp.spring.framework.error.GlobalResponseCode;

import java.io.Serial;
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
@SuppressWarnings("java:S1948")
public class MultiResult<T> extends Result {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * The collection of data items returned in the response
     */
    private Collection<T> data;

    /**
     * Creates a successful MultiResult with an empty collection.
     *
     * @param <T> the type of elements in the result collection
     * @return a new MultiResult instance with success status and empty data
     */
    public static <T> MultiResult<T> build() {
        return build(Collections.emptyList());
    }

    /**
     * Creates a successful MultiResult with the specified collection of data.
     *
     * @param <T>  the type of elements in the result collection
     * @param data the collection of data to include in the result
     * @return a new MultiResult instance with success status and the provided data
     */
    public static <T> MultiResult<T> build(Collection<T> data) {
        return MultiResult.<T>multiResultBuilder()
                .success(true)
                .code(GlobalResponseCode.SUCCESS.code())
                .msg(GlobalResponseCode.SUCCESS.message())
                .data(data)
                .build();
    }

    /**
     * Creates a failure MultiResult with the specified error code.
     *
     * @param <T> the type of elements in the result collection (will be empty for failure)
     * @param errorCode the error code containing code and message for the failure
     * @return a new MultiResult instance with failure status and the provided error details
     */
    public static <T> MultiResult<T> buildFailure(ErrorCode errorCode) {
        return MultiResult.<T>multiResultBuilder()
                .success(false)
                .code(errorCode.getCode())
                .msg(errorCode.getMessage())
                .build();
    }
}
