package space.x9x.radp.commons.lang.format;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author IO x9x
 * @since 2024-09-26 19:24
 */
@Getter
@AllArgsConstructor
public class FormattingTuple {
    /**
     * A constant representing a null formatting tuple.
     * This can be used as a placeholder or default value when no formatting is needed.
     */
    public static final FormattingTuple NULL = new FormattingTuple(null);

    private final String message;
    private final Object[] argArray;
    private final Throwable throwable;

    /**
     * Constructs a FormattingTuple with only a message.
     * <p>
     * This constructor creates a tuple with the specified message and sets
     * both the argument array and throwable to null.
     *
     * @param message the formatted message
     */
    public FormattingTuple(String message) {
        this(message, null, null);
    }
}
