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

    private static final long serialVersionUID = 1L;

    /**
     * The collection of data items in the result.
     */
    private Collection<T> data;

    /**
     * Creates a successful MultiResult with an empty collection.
     * 
     * @param <T> The type of elements in the collection
     * @return A successful MultiResult with an empty collection
     */
    public static <T> MultiResult<T> build() {
        return build(Collections.emptyList());
    }

    /**
     * Creates a successful MultiResult with the specified collection of data.
     * 
     * @param <T> The type of elements in the collection
     * @param data The collection of data to include in the result
     * @return A successful MultiResult containing the provided data
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
     * @param <T> The type of elements in the collection (not used in failure case)
     * @param errorCode The error code containing code and message for the failure
     * @return A failure MultiResult with the error information
     */
    public static <T> MultiResult<T> buildFailure(ErrorCode errorCode) {
        return MultiResult.<T>multiResultBuilder()
                .success(false)
                .code(errorCode.getCode())
                .msg(errorCode.getMessage())
                .build();
    }
}
