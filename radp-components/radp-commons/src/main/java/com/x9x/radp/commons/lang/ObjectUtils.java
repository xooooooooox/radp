package com.x9x.radp.commons.lang;

import lombok.experimental.UtilityClass;

/**
 * @author x9x
 * @since 2024-09-30 10:34
 */
@UtilityClass
public class ObjectUtils extends org.apache.commons.lang3.ObjectUtils {

    public static String trimToString(Object object) {
        if (object == null) {
            return Strings.EMPTY;
        }
        return StringUtils.trimToEmpty(String.valueOf(object));
    }
}
