package space.x9x.radp.spring.framework.error.http;

import lombok.EqualsAndHashCode;
import space.x9x.radp.spring.framework.error.BaseException;

/**
 * @author x9x
 * @since 2024-09-27 11:18
 */
@EqualsAndHashCode(callSuper = true)
public class ForbiddenException extends BaseException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new ForbiddenException with the specified error message and formatting parameters.
     * This exception represents an HTTP 403 Forbidden error.
     *
     * @param errMessage the error message pattern
     * @param params     the parameters to be used for message formatting
     */
    public ForbiddenException(String errMessage, Object... params) {
        super("403", errMessage, params);
    }
}
