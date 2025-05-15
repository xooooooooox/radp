package space.x9x.radp.commons.lang;

import lombok.experimental.UtilityClass;

/**
 * @author x9x
 * @since 2024-09-30 10:34
 */
@UtilityClass
public class ObjectUtils extends org.apache.commons.lang3.ObjectUtils {

    /**
     * Converts an object to a string and trims any whitespace.
     * <p>
     * This method safely handles null objects by returning an empty string.
     * For non-null objects, it converts them to a string using String.valueOf()
     * and then trims any leading or trailing whitespace.
     *
     * @param object the object to convert to a trimmed string
     * @return the trimmed string representation of the object, or an empty string if the object is null
     */
    public static String trimToString(Object object) {
        if (object == null) {
            return Strings.EMPTY;
        }
        return StringUtils.trimToEmpty(String.valueOf(object));
    }
}
