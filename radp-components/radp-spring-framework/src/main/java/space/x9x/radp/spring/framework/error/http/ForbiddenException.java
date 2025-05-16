package space.x9x.radp.spring.framework.error.http;

import space.x9x.radp.spring.framework.error.BaseException;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * @author x9x
 * @since 2024-09-27 11:18
 */
@EqualsAndHashCode(callSuper = true)
public class ForbiddenException extends BaseException {

    @Serial
    private static final long serialVersionUID = 1L;

    public ForbiddenException(String errMessage, Object... params) {
        super("403", errMessage, params);
    }
}
