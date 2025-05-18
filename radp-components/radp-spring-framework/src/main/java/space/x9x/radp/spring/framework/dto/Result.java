package space.x9x.radp.spring.framework.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;
import space.x9x.radp.spring.framework.error.GlobalResponseCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * Base class for API response objects that provides common result information.
 * This class includes status, code, and message fields to indicate the outcome of an operation.
 * It serves as the parent class for more specific result types like SingleResult and MultiResult.
 *
 * @author x9x
 * @since 2024-09-26 15:53
 */
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Getter
public class Result implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Indicates whether the operation was successful
     */
    private boolean success;
    /** The response code, typically used for error identification */
    private String code;
    /** The human-readable message describing the result */
    private String msg;

    /**
     * Creates a successful Result with standard success code and message.
     *
     * @return a new Result instance with success status
     */
    public static Result buildSuccess() {
        return Result.builder()
                .code(GlobalResponseCode.SUCCESS.code())
                .msg(GlobalResponseCode.SUCCESS.message())
                .success(true)
                .build();
    }
}
