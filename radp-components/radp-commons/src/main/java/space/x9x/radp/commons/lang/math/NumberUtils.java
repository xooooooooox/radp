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

import java.util.concurrent.ThreadLocalRandom;

import lombok.experimental.UtilityClass;

/**
 * Numeric utilities extending Apache Commons NumberUtils with handy helpers for
 * generating numeric-only strings.
 * <p>
 * This class provides:
 * <ul>
 * <li>Generating N-digit numeric strings whose first digit is not zero</li>
 * <li>Quick generation of an 11-digit number</li>
 * <li>Creation of a plausible Mainland China 11-digit mobile number for testing</li>
 * </ul>
 * Thread-safety: All methods are stateless and thread-safe.
 * </p>
 *
 * @author IO x9x
 * @since 2024-09-27 21:11
 * @see org.apache.commons.lang3.math.NumberUtils
 */
@UtilityClass
public class NumberUtils extends org.apache.commons.lang3.math.NumberUtils {

	/**
	 * 数字字符集.
	 */
	private static final char[] DIGITS = "0123456789".toCharArray();

	/**
	 * Generate a numeric string with the specified length where the first digit is
	 * non-zero.
	 * <p>
	 * For example, length=4 may yield "1234" or "9087".
	 * </p>
	 * @param length total number of digits; must be >= 1
	 * @return a numeric string of the given length; its first digit is in [1-9]
	 * @throws IllegalArgumentException if length < 1
	 */
	public static String generateNDigitNumber(int length) {
		if (length < 1) {
			throw new IllegalArgumentException("length must be >= 1");
		}
		// 使用线程安全的随机数：ThreadLocalRandom
		ThreadLocalRandom random = ThreadLocalRandom.current();
		StringBuilder sb = new StringBuilder(length);
		// 首位 [1-9]
		sb.append((char) ('1' + random.nextInt(9))); // 1..9
		for (int i = 1; i < length; i++) {
			sb.append(DIGITS[random.nextInt(10)]);
		}
		return sb.toString();
	}

	/**
	 * Generate an 11-digit numeric string whose first digit is non-zero.
	 * @return an 11-digit numeric string
	 * @see #generateNDigitNumber(int)
	 */
	public static String generate11DigitNumber() {
		return generateNDigitNumber(11);
	}

	/**
	 * Generate a plausible Mainland China 11-digit mobile number for testing purposes.
	 * <p>
	 * Rules:
	 * <ul>
	 * <li>Starts with '1'</li>
	 * <li>The second digit is randomly chosen from common segments [3,4,5,6,7,8,9]</li>
	 * <li>The remaining 9 digits are random [0-9]</li>
	 * </ul>
	 * Note: The result is not guaranteed to correspond to an actually assigned/valid
	 * number; it is intended for demos/tests only.
	 * </p>
	 * @return a string containing 11 digits representing a plausible mobile number
	 */
	public static String generateChineseMobile() {
		ThreadLocalRandom random = ThreadLocalRandom.current();
		// 常见第二位号段
		int[] secondDigits = { 3, 4, 5, 6, 7, 8, 9 };
		StringBuilder sb = new StringBuilder(11);
		sb.append('1');
		sb.append(secondDigits[random.nextInt(secondDigits.length)]);
		// 第三位也给 0-9 随机，后续 8 位同理
		for (int i = 0; i < 9; i++) {
			sb.append(DIGITS[random.nextInt(10)]);
		}
		return sb.toString();
	}

}
