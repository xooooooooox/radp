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
 * Utility class for string operations, extending Apache Commons Lang StringUtils.
 * Provides additional string manipulation methods not available in the parent class.
 *
 * @author RADP x9x
 * @since 2024-09-23 13:57
 */
@UtilityClass
public class StringUtil {

	/**
	 * Converts camel-case text to lower-case words separated by {@code split}.
	 * <p>
	 * Examples:
	 * <ul>
	 * <li>{@code "camelCase" → "camel-case"}</li>
	 * <li>{@code "camelCaseURL" → "camel-case-url"}</li>
	 * <li>{@code "CamelCase" → "camel-case"}</li>
	 * </ul>
	 * @param camelName the camel case string to convert
	 * @param split the separator to use between words
	 * @return the converted string, or the original string if it's empty or has no
	 * uppercase letters
	 */
	public static String camelToSplitName(String camelName, String split) {
		if (StringUtils.isEmpty(camelName) || split == null) {
			return camelName;
		}

		StringBuilder out = new StringBuilder(camelName.length() + 8);
		boolean prevWasLowerOrDigit = false; // tracks lowercase / digit just seen

		for (char ch : camelName.toCharArray()) {
			if (Character.isUpperCase(ch)) {
				if (prevWasLowerOrDigit && out.length() > 0) {
					out.append(split); // add separator only at a true word boundary
				}
				out.append(Character.toLowerCase(ch));
				prevWasLowerOrDigit = false; // consecutive caps belong to same acronym
			}
			else {
				out.append(ch);
				prevWasLowerOrDigit = Character.isLowerCase(ch) || Character.isDigit(ch);
			}
		}
		return out.toString();
	}

	public static boolean isEmpty(final CharSequence cs) {
		return StringUtils.isEmpty(cs);
	}

	public static String trimToEmpty(final String str) {
		return StringUtils.trimToEmpty(str);
	}

	public static boolean isNotEmpty(final CharSequence cs) {
		return StringUtils.isNotEmpty(cs);
	}

	public static boolean isNoneEmpty(final CharSequence... css) {
		return StringUtils.isNoneEmpty(css);
	}

	public static boolean isBlank(final CharSequence cs) {
		return StringUtils.isBlank(cs);
	}

	public static boolean isNotBlank(final CharSequence cs) {
		return StringUtils.isNotBlank(cs);
	}

	public static boolean isNoneBlank(final CharSequence... css) {
		return StringUtils.isNoneBlank(css);
	}

	public static String join(final Object[] array, final char delimiter) {
		return StringUtils.join(array, delimiter);
	}

	public static String join(final Object[] array, final String delimiter) {
		return StringUtils.join(array, delimiter);
	}

	@SafeVarargs
	public static <T> String join(final T... elements) {
		return StringUtils.join(elements);
	}

	public static String[] split(final String str, final String separatorChars) {
		return StringUtils.split(str, separatorChars);
	}

	public static String substringBefore(final String str, final String separator) {
		return StringUtils.substringBefore(str, separator);
	}

	public static String substringAfter(final String str, final String separator) {
		return StringUtils.substringAfter(str, separator);
	}

	public static String substring(final String str, int start, int end) {
		return StringUtils.substring(str, start, end);
	}

	public static String right(final String str, final int len) {
		return StringUtils.right(str, len);
	}

}
