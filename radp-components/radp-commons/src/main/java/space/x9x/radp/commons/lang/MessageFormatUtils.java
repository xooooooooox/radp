package space.x9x.radp.commons.lang;

import space.x9x.radp.commons.lang.format.MessageFormatter;
import lombok.experimental.UtilityClass;

/**
 * @author x9x
 * @since 2024-09-26 20:29
 */
@UtilityClass
public class MessageFormatUtils {
    public static String format(String message,
                                Object... placeholders) {
        return MessageFormatter.arrayFormat(message, placeholders).getMessage();
    }
}
