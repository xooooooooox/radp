package com.x9x.radp.spring.framework.error.http;

import com.x9x.radp.spring.framework.error.BaseException;
import lombok.EqualsAndHashCode;

/**
 * @author x9x
 * @since 2024-09-27 11:08
 */
@EqualsAndHashCode(callSuper = true)
public class BadRequestException extends BaseException {

    public BadRequestException(String errMessage, Object... params) {
        super("400", errMessage, params);
    }
}
