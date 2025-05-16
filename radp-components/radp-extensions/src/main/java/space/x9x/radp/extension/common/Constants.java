package space.x9x.radp.extension.common;

import lombok.experimental.UtilityClass;

import java.util.regex.Pattern;

/**
 * @author x9x
 * @since 2024-09-24 19:31
 */
@UtilityClass
public class Constants {

    /**
     * Regular expression pattern for splitting strings by commas.
     * This pattern matches one or more commas with optional whitespace before and after.
     * It is used throughout the extension system to parse comma-separated lists of values,
     * such as extension names, configuration values, etc.
     */
    public static final Pattern COMMA_SPLIT_PATTERN = Pattern.compile("\\s*[,]+\\s*");
}
