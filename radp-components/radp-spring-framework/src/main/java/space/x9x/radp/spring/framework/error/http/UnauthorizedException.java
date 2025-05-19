package space.x9x.radp.spring.framework.error.http;

import lombok.EqualsAndHashCode;
import space.x9x.radp.spring.framework.error.BaseException;

/**
 * @author x9x
 * @since 2024-09-27 11:20
 */
@EqualsAndHashCode(callSuper = true)
public class UnauthorizedException extends BaseException {

    private static final long serialVersionUID = 1L;

    public UnauthorizedException(String errMessage, Object... params) {
        super("401", errMessage, params);
    }
}
