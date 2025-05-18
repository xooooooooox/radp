package space.x9x.radp.spring.framework.error.http;

import lombok.EqualsAndHashCode;
import space.x9x.radp.spring.framework.error.BaseException;

import java.io.Serial;

/**
 * @author x9x
 * @since 2024-09-27 11:08
 */
@EqualsAndHashCode(callSuper = true)
public class BadRequestException extends BaseException {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new BadRequestException with the specified error message and parameters.
     *
     * @param errMessage the error message
     * @param params     the parameters to be used for message formatting
     */
    public BadRequestException(String errMessage, Object... params) {
        super("400", errMessage, params);
    }
}
