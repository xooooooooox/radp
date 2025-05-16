package space.x9x.radp.spring.framework.error.http;

import space.x9x.radp.spring.framework.error.BaseException;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * @author x9x
 * @since 2024-09-27 11:08
 */
@EqualsAndHashCode(callSuper = true)
public class BadRequestException extends BaseException {

    @Serial
    private static final long serialVersionUID = 1L;

    public BadRequestException(String errMessage, Object... params) {
        super("400", errMessage, params);
    }
}
