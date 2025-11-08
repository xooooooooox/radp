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
		if (isEmpty(camelName) || split == null) {
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

}
