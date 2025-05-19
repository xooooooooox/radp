package space.x9x.radp.commons.lang;

import lombok.experimental.UtilityClass;

/**
 * Utility class for string operations, extending Apache Commons Lang StringUtils.
 * Provides additional string manipulation methods not available in the parent class.
 *
 * @author x9x
 * @since 2024-09-23 13:57
 */
@UtilityClass
public class StringUtils extends org.apache.commons.lang3.StringUtils {
    /**
     * Converts a camel case string to a string with a specified separator.
     * For example, "camelCase" with a separator of "-" becomes "camel-case".
     *
     * @param camelName the camel case string to convert
     * @param split     the separator to use between words
     * @return the converted string, or the original string if it's empty or has no uppercase letters
     */
    public static String camelToSplitName(String camelName, String split) {
        if (isEmpty(camelName)) {
            return camelName;
        }
        StringBuilder buf = null;
        for (int i = 0; i < camelName.length(); i++) {
            char ch = camelName.charAt(i);
            if (ch >= 'A' && ch <= 'Z') {
                if (buf == null) {
                    buf = new StringBuilder();
                    if (i > 0) {
                        buf.append(camelName, 0, i);
                    }
                }
                if (i > 0) {
                    buf.append(split);
                }
                buf.append(Character.toLowerCase(ch));
            } else if (buf != null) {
                buf.append(ch);
            }
        }
        return buf == null ? camelName : buf.toString();
    }
}
