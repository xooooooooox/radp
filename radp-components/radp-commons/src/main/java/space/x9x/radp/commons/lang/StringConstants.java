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

package space.x9x.radp.commons.lang;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

/**
 * String constants for common use throughout the application. This utility class provides
 * a centralized repository of string constants to avoid duplication and ensure
 * consistency.
 *
 * @author RADP x9x
 * @since 2024-09-28 21:00
 */
@UtilityClass
public class StringConstants {

	/**
	 * Empty string constant. Used when an empty string is needed.
	 */
	public static final String EMPTY = StringUtils.EMPTY;

	/**
	 * Space character constant. Used for string formatting and whitespace replacement.
	 */
	public static final String SPACE = StringUtils.SPACE;

	/**
	 * Single quote character constant. Used for quoting strings in SQL or other contexts.
	 */
	public static final String HARD_QUOTE = "'";

	/**
	 * Double quote character constant. Used for quoting strings in JSON or other
	 * contexts.
	 */
	public static final String SOFT_QUOTE = "\"";

	/**
	 * Forward slash character constant. Used for path separators and URL formatting.
	 */
	public static final String SLASH = "/";

	/**
	 * Backslash character constant. Used for escaping characters in strings.
	 */
	public static final String ESCAPE = "\\";

	/**
	 * Comma character constant. Used for separating elements in lists or CSV data.
	 */
	public static final String COMMA = ",";

	/**
	 * Dot character constant. Used for joining class and method names or decimal points.
	 */
	public static final String DOT = ".";

	/**
	 * Colon character constant. Used for key-value separators, especially in Redis keys.
	 */
	public static final String COLON = ":";

	/**
	 * Left square bracket character constant. Used for array or list notation.
	 */
	public static final String BRACE_PREFIX = "[";

	/**
	 * Right square bracket character constant. Used for array or list notation.
	 */
	public static final String BRACE_SUFFIX = "]";

	/**
	 * Plus sign character constant. Used for addition operations or string concatenation.
	 */
	public static final String PLUS = "+";

	/**
	 * Minus sign character constant. Used for subtraction operations or negative numbers.
	 */
	public static final String MINUS = "-";

	/**
	 * Underscore character constant. Used for naming conventions or separating words in
	 * identifiers.
	 */
	public static final String UNDERLINE = "_";

	/**
	 * Equals sign character constant. Used for assignment or equality comparison.
	 */
	public static final String EQ = "=";

	/**
	 * Greater than sign character constant. Used for comparison operations.
	 */
	public static final String GT = ">";

	/**
	 * Less than sign character constant. Used for comparison operations.
	 */
	public static final String LT = "<";

	/**
	 * Null string representation. Used to represent null values as a string.
	 */
	public static final String NULL = "null";

	/**
	 * String format placeholder. Used with String.format() for message formatting.
	 */
	public static final String MSG_REPLACE = "%s";

	/**
	 * Question mark character constant. Used as a placeholder in SQL prepared statements.
	 */
	public static final String PLACEHOLDER = "?";

	/**
	 * Ampersand character constant. Used for logical AND operations or URL parameter
	 * concatenation.
	 */
	public static final String AND = "&";

	/**
	 * Hash/pound sign character constant. Used as a separator or for comments in various
	 * contexts.
	 */
	public static final String HASH = "#";

}
