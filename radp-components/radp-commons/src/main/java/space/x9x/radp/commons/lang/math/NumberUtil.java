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

package space.x9x.radp.commons.lang.math;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * @author RADP x9x
 * @since 2024-09-27 21:11
 * @see org.apache.commons.lang3.math.NumberUtils
 */
@UtilityClass
public class NumberUtil {

	public static long toLong(final String str) {
		return NumberUtils.toLong(str);
	}

	public static long toLong(final String str, final long defaultValue) {
		return NumberUtils.toLong(str, defaultValue);
	}

	public static int toInt(final String str) {
		return NumberUtils.toInt(str);
	}

	public static int toInt(final String str, final int defaultValue) {
		return NumberUtils.toInt(str, defaultValue);
	}

}
