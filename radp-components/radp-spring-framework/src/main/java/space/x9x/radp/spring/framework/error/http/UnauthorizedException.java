package space.x9x.radp.spring.framework.error.http;

import java.io.Serial;

import lombok.EqualsAndHashCode;

import space.x9x.radp.spring.framework.error.BaseException;

/**
 * Exception thrown when a user attempts to access a resource without proper
 * authentication. This exception corresponds to HTTP 401 Unauthorized status.
 *
 * @author IO x9x
 * @since 2024-09-27 11:20
 */
@EqualsAndHashCode(callSuper = true)
public class UnauthorizedException extends BaseException {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new UnauthorizedException with the specified error message and optional parameters.
     *
     * @param errMessage the error message describing the unauthorized access
     * @param params     optional parameters to be used in formatting the error message
     */
    public UnauthorizedException(String errMessage, Object... params) {
        super("401", errMessage, params);
    }
}
