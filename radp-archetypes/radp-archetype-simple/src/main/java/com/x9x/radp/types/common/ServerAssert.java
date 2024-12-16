package com.x9x.radp.types.common;

import com.x9x.radp.spring.framework.error.ErrorCode;
import com.x9x.radp.spring.framework.error.ServerException;
import com.x9x.radp.spring.framework.error.asserts.BaseServerAssert;
import lombok.experimental.UtilityClass;

/**
 * @author x9x
 * @since 2024-10-24 21:54
 */
@UtilityClass
public class ServerAssert implements BaseServerAssert {

    public static void notNull(Object object, ErrorCode errorCode) {
        try {
            AssertUtils.notNull(object, errorCode);
        } catch (IllegalArgumentException e) {
            throw new ServerException(errorCode);
        }
    }
}
