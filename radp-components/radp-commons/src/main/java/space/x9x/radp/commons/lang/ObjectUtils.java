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
 * @author IO x9x
 * @since 2024-09-30 10:34
 */
@UtilityClass
public class ObjectUtils extends org.apache.commons.lang3.ObjectUtils {

	/**
	 * Converts an object to a string and trims any whitespace.
	 * <p>
	 * This method safely handles null objects by returning an empty string. For non-null
	 * objects, it converts them to a string using String.valueOf() and then trims any
	 * leading or trailing whitespace.
	 * @param object the object to convert to a trimmed string
	 * @return the trimmed string representation of the object, or an empty string if the
	 * object is null
	 */
	public static String trimToString(Object object) {
		if (object == null) {
			return Strings.EMPTY;
		}
		return StringUtils.trimToEmpty(String.valueOf(object));
	}

}
