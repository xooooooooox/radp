package space.x9x.radp.spring.framework.error.http;

import space.x9x.radp.spring.framework.error.BaseException;
import lombok.EqualsAndHashCode;

/**
 * @author x9x
 * @since 2024-09-27 11:18
 */
@EqualsAndHashCode(callSuper = true)
public class ForbiddenException extends BaseException {
    public ForbiddenException(String errMessage, Object... params) {
        super("403", errMessage, params);
    }
}
