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
 * Container for formatted messages and their associated data. This class holds the result
 * of a message formatting operation, including the formatted message string, the original
 * argument array, and any throwable that might have been extracted from the arguments.
 *
 * @author IO x9x
 * @since 2024-09-26 19:24
 */
@Getter
@AllArgsConstructor
public class FormattingTuple {

	/**
	 * A constant representing a null formatting tuple. This can be used as a placeholder
	 * or default value when no formatting is needed.
	 */
	public static final FormattingTuple NULL = new FormattingTuple(null);

	/**
	 * The formatted message string. This is the result of replacing placeholders in the
	 * message pattern with the corresponding arguments.
	 */
	private final String message;

	/**
	 * The original array of arguments provided for message formatting. This may be null
	 * if no arguments were provided.
	 */
	private final Object[] argArray;

	/**
	 * Any throwable that was extracted from the argument array. This may be null if no
	 * throwable was provided or extracted.
	 */
	private final Throwable throwable;

	/**
	 * Constructs a FormattingTuple with only a message.
	 * <p>
	 * This constructor creates a tuple with the specified message and sets both the
	 * argument array and throwable to null.
	 * @param message the formatted message
	 */
	public FormattingTuple(String message) {
		this(message, null, null);
	}

}
