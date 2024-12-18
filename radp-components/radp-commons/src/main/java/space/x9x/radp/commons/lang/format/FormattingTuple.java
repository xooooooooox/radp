package space.x9x.radp.commons.lang.format;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author x9x
 * @since 2024-09-26 19:24
 */
@Getter
@AllArgsConstructor
public class FormattingTuple {
    public static final FormattingTuple NULL = new FormattingTuple(null);

    private final String message;
    private final Object[] argArray;
    private final Throwable throwable;

    public FormattingTuple(String message) {
        this(message, null, null);
    }
}
