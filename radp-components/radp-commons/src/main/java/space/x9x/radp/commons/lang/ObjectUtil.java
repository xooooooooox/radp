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
import org.apache.commons.lang3.ObjectUtils;

/**
 * Utility class for object operations. This class extends Apache Commons Lang's
 * ObjectUtils to provide additional utility methods for working with objects, including
 * null-safe operations and string conversions.
 *
 * @author x9x
 * @since 2024-09-30 10:34
 */
@UtilityClass
public class ObjectUtil {

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
			return StringConstants.EMPTY;
		}
		return StringUtil.trimToEmpty(String.valueOf(object));
	}

	public static boolean isEmpty(final Object object) {
		return ObjectUtils.isEmpty(object);
	}

	public static boolean isNotEmpty(final Object object) {
		return ObjectUtils.isNotEmpty(object);
	}

}
