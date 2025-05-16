package space.x9x.radp.spring.framework.error.http;

import lombok.EqualsAndHashCode;
import space.x9x.radp.spring.framework.error.BaseException;

import java.io.Serial;

/**
 * @author x9x
 * @since 2024-09-27 11:18
 */
@EqualsAndHashCode(callSuper = true)
public class ForbiddenException extends BaseException {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new ForbiddenException with the specified error message and parameters.
     * This exception is used to indicate that the server understood the request but refuses to authorize it (HTTP 403).
     *
     * @param errMessage the error message pattern
     * @param params     the parameters to be substituted in the error message
     */
    public ForbiddenException(String errMessage, Object... params) {
        super("403", errMessage, params);
    }
}
