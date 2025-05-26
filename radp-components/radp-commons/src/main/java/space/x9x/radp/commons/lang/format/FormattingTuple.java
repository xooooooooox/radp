/*
 * Copyright 2012-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
