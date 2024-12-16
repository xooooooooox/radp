package com.x9x.radp.extension.common;

import lombok.experimental.UtilityClass;

import java.util.regex.Pattern;

/**
 * @author x9x
 * @since 2024-09-24 19:31
 */
@UtilityClass
public class Constants {

    public static final Pattern COMMA_SPLIT_PATTERN = Pattern.compile("\\s*[,]+\\s*");
}
