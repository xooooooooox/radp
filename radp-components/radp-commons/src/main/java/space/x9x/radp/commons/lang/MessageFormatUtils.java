package space.x9x.radp.commons.lang;

import lombok.experimental.UtilityClass;
import space.x9x.radp.commons.lang.format.MessageFormatter;

/**
 * @author IO x9x
 * @since 2024-09-26 20:29
 */
@UtilityClass
public class MessageFormatUtils {
    /**
     * Formats a message by replacing placeholders with the provided values.
     * <p>
     * This method is a convenience wrapper around {@link MessageFormatter#arrayFormat}
     * that directly returns the formatted message string.
     *
     * @param message      the message pattern containing placeholders (e.g., "Hello, {}!")
     * @param placeholders the values to replace the placeholders with
     * @return the formatted message with placeholders replaced by the provided values
     */
    public static String format(String message,
                                Object... placeholders) {
        return MessageFormatter.arrayFormat(message, placeholders).getMessage();
    }
}
