package space.x9x.radp.spring.framework.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;
import space.x9x.radp.spring.framework.error.ErrorCode;
import space.x9x.radp.spring.framework.error.GlobalResponseCode;

import java.io.Serial;
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

    @Serial
    private static final long serialVersionUID = 1L;

    private Collection<T> data;

    @Builder.Default
    private Long total = 0L;

    /**
     * Gets the data collection from this page result.
     * If the data is null, returns an empty collection.
     * If the data is not already a List, converts it to an ArrayList.
     *
     * @return the collection of data items, never null
     */
    public Collection<T> getData() {
        if (data == null) {
            return Collections.emptyList();
        }
        if (data instanceof List) {
            return data;
        }
        return new ArrayList<>(data);
    }

    /**
     * Creates a successful PageResult with the specified collection of data and total count.
     *
     * @param <T>   the type of elements in the result collection
     * @param data  the collection of data to include in the result
     * @param total the total number of items (across all pages)
     * @return a new PageResult instance with success status and the provided data and total
     */
    public static <T> PageResult<T> build(Collection<T> data, Long total) {
        return PageResult.<T>pageResultBuilder()
                .success(true)
                .code(GlobalResponseCode.SUCCESS.code())
                .msg(GlobalResponseCode.SUCCESS.message())
                .data(data)
                .total(total)
                .build();
    }

    /**
     * Creates a failure PageResult with the specified error code.
     *
     * @param <T> the type of elements in the result collection (will be empty for failure)
     * @param errorCode the error code containing code and message for the failure
     * @return a new PageResult instance with failure status and the provided error details
     */
    public static <T> PageResult<T> buildFailure(ErrorCode errorCode) {
        return PageResult.<T>pageResultBuilder()
                .success(false)
                .code(errorCode.getCode())
                .msg(errorCode.getMessage())
                .build();
    }
}
