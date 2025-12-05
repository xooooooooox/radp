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

package space.x9x.radp.commons.regex.pattern;

import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import lombok.experimental.UtilityClass;

/**
 * Regular expression pattern cache. This utility class provides pre-compiled Pattern
 * objects for commonly used regular expressions and a caching mechanism to improve
 * performance when compiling frequently used patterns.
 *
 * @author RADP x9x
 * @since 2024-12-27 12:55
 */
@UtilityClass
public class RegexCache {

	/**
	 * Alphabetic characters pattern. Matches one or more letters (a-z, A-Z).
	 */
	public static final Pattern WORD = Pattern.compile(Regex.WORD);

	/**
	 * Chinese characters pattern. Matches one or more Chinese characters in the Unicode
	 * range.
	 */
	public static final Pattern CHINESE = Pattern.compile(Regex.CHINESE);

	/**
	 * Username pattern. Matches usernames containing a-z, A-Z, 0-9, underscore, and
	 * Chinese characters. Cannot end with an underscore. Length must be between 1 and 18
	 * characters.
	 */
	public static final Pattern USERNAME = Pattern.compile(Regex.USERNAME);

	/**
	 * Email address pattern. Compliant with RFC 5322 standard for email validation.
	 */
	public static final Pattern EMAIL = Pattern.compile(Regex.EMAIL);

	/**
	 * Mobile phone number pattern for Mainland China. Matches Chinese mobile phone
	 * numbers with optional country code (+86).
	 */
	public static final Pattern MOBILE = Pattern.compile(Regex.MOBILE);

	/**
	 * Mobile phone number pattern for Hong Kong. <br>
	 * Country codes reference: <br>
	 * Mainland China: +86 <br>
	 * Hong Kong: +852 <br>
	 * Macao: +853 <br>
	 * Taiwan: +886
	 */
	public static final Pattern MOBILE_HK = Pattern.compile(Regex.MOBILE_HK);

	/**
	 * Mobile phone number pattern for Taiwan. Matches Taiwanese mobile phone numbers with
	 * optional country code (+886).
	 */
	public static final Pattern MOBILE_TW = Pattern.compile(Regex.MOBILE_TW);

	/**
	 * Mobile phone number pattern for Macao. Matches Macanese mobile phone numbers with
	 * optional country code (+853).
	 */
	public static final Pattern MOBILE_MO = Pattern.compile(Regex.MOBILE_MO);

	/**
	 * Landline telephone number pattern for China. Matches Chinese landline phone numbers
	 * with area code and local number.
	 */
	public static final Pattern TELEPHONE = Pattern.compile(Regex.TELEPHONE);

	/**
	 * Service telephone number pattern. Matches Chinese service numbers including 400 and
	 * 800 toll-free numbers.
	 */
	public static final Pattern TELEPHONE_400_800 = Pattern.compile(Regex.TELEPHONE_400_800);

	/**
	 * Chinese ID card number pattern (15-digit format). Matches the older 15-digit format
	 * of Chinese national ID cards.
	 */
	public static final Pattern ID_CARD_15 = Pattern.compile(Regex.ID_CARD_15);

	/**
	 * Chinese ID card number pattern (18-digit format). Matches the current 18-digit
	 * format of Chinese national ID cards, including the check digit which can be a
	 * number or 'X'.
	 */
	public static final Pattern ID_CARD_18 = Pattern.compile(Regex.ID_CARD_18);

	/**
	 * Unified Social Credit Code pattern for Chinese organizations. Matches the
	 * 18-character code used to identify registered organizations in China.
	 */
	public static final Pattern CREDIT_CODE = Pattern.compile(Regex.CREDIT_CODE);

	/**
	 * Chinese postal code pattern. Matches the 6-digit postal codes used in China.
	 */
	public static final Pattern ZIP_CODE = Pattern.compile(Regex.ZIP_CODE);

	/**
	 * Birthday date pattern. Matches various date formats for birthdays, supporting
	 * different separators.
	 */
	public static final Pattern BIRTHDAY = Pattern.compile(Regex.BIRTHDAY);

	/**
	 * Tencent QQ number pattern. Matches QQ numbers, which must start with a non-zero
	 * digit and have at least 5 digits.
	 */
	public static final Pattern TENCENT_QQ = Pattern.compile(Regex.TENCENT_QQ);

	/**
	 * Chinese vehicle license plate number pattern. Matches the various formats of
	 * license plates used in mainland China, including special plates for different
	 * regions and purposes.
	 */
	public static final Pattern PLATE_NUMBER_CN = Pattern.compile(Regex.PLATE_NUMBER_CN);

	/**
	 * Vehicle Identification Number (VIN) pattern. Matches the 17-character VIN used to
	 * uniquely identify motor vehicles.
	 */
	public static final Pattern CAR_VIN = Pattern.compile(Regex.CAR_VIN);

	/**
	 * Chinese driving license number pattern. Matches the 12-digit format of Chinese
	 * driving license numbers.
	 */
	public static final Pattern CAR_DRIVING_LICENCE = Pattern.compile(Regex.CAR_DRIVING_LICENCE);

	/**
	 * IPv4 address pattern. Matches valid IPv4 addresses with proper octet range
	 * validation (0-255).
	 */
	public static final Pattern IPV4 = Pattern.compile(Regex.IPV4);

	/**
	 * IPv6 address pattern. Matches valid IPv6 addresses in various formats, including
	 * compressed notation.
	 */
	public static final Pattern IPV6 = Pattern.compile(Regex.IPV6);

	/**
	 * URL pattern. Matches URLs with various protocols (http, https, ftp, etc.).
	 */
	public static final Pattern URL = Pattern.compile(Regex.URL);

	/**
	 * HTTP URL pattern. Matches HTTP and HTTPS URLs with optional protocol prefix.
	 */
	public static final Pattern HTTP = Pattern.compile(Regex.HTTP);

	/**
	 * Currency amount pattern. Matches numeric values representing currency amounts, with
	 * optional decimal places.
	 */
	public static final Pattern MONEY = Pattern.compile(Regex.MONEY);

	/**
	 * ISO date pattern (YYYY-MM-DD). Matches ISO 8601 date format with validation for
	 * valid dates, including leap year validation for February 29.
	 */
	public static final Pattern DATE = Pattern.compile(Regex.DATE);

	/**
	 * Time pattern (HH:MM[:SS]). Matches time format with hours, minutes, and optional
	 * seconds.
	 */
	public static final Pattern TIME = Pattern.compile(Regex.TIME);

	/**
	 * UUID pattern. Matches standard UUID format (8-4-4-4-12 hexadecimal digits).
	 */
	public static final Pattern UUID = Pattern.compile(Regex.UUID);

	/**
	 * MAC address pattern. Matches MAC addresses in various formats, including colon and
	 * hyphen separators.
	 */
	public static final Pattern MAC_ADDRESS = Pattern.compile(Regex.MAC_ADDRESS);

	/**
	 * Hexadecimal string pattern. Matches strings containing only hexadecimal characters
	 * (0-9, a-f, A-F).
	 */
	public static final Pattern HEX = Pattern.compile(Regex.HEX);

	/**
	 * Integer pattern. Matches positive or negative integers (excluding zero with a
	 * sign).
	 */
	public static final Pattern INTEGER = Pattern.compile(Regex.INTEGER);

	/**
	 * Positive integer pattern. Matches integers greater than zero.
	 */
	public static final Pattern INTEGER_POSITIVE = Pattern.compile(Regex.INTEGER_POSITIVE);

	/**
	 * Non-positive integer pattern. Matches integers less than or equal to zero.
	 */
	public static final Pattern INTEGER_POSITIVE_REVERSE = Pattern.compile(Regex.INTEGER_POSITIVE_REVERSE);

	/**
	 * Negative integer pattern. Matches integers less than zero.
	 */
	public static final Pattern INTEGER_NEGATIVE = Pattern.compile(Regex.INTEGER_NEGATIVE);

	/**
	 * Non-negative integer pattern. Matches integers greater than or equal to zero.
	 */
	public static final Pattern INTEGER_NEGATIVE_REVERSE = Pattern.compile(Regex.INTEGER_NEGATIVE_REVERSE);

	/**
	 * Floating-point number pattern. Matches positive or negative floating-point numbers.
	 */
	public static final Pattern FLOAT = Pattern.compile(Regex.FLOAT);

	/**
	 * Positive floating-point number pattern. Matches floating-point numbers greater than
	 * zero.
	 */
	public static final Pattern FLOAT_POSITIVE = Pattern.compile(Regex.FLOAT_POSITIVE);

	/**
	 * Negative floating-point number pattern. Matches floating-point numbers less than
	 * zero.
	 */
	public static final Pattern FLOAT_NEGATIVE = Pattern.compile(Regex.FLOAT_NEGATIVE);

	/**
	 * Regular expression pattern compilation cache. Stores compiled patterns to improve
	 * performance by avoiding repeated compilation.
	 */
	private static final ConcurrentHashMap<String, Pattern> CACHE = new ConcurrentHashMap<>();

	/**
	 * Gets a pattern from the cache, compiling and storing it if not found. This method
	 * improves performance by reusing compiled patterns.
	 * @param regex the regular expression string
	 * @param flags the compilation flags
	 * @return the compiled Pattern object
	 */
	public static Pattern get(String regex, int flags) {
		Pattern pattern = CACHE.get(regex);
		if (pattern == null) {
			pattern = Pattern.compile(regex, flags);
			CACHE.put(regex, pattern);
		}
		return pattern;
	}

	/**
	 * Removes a pattern from the cache. This method can be used to free memory or force
	 * recompilation of a pattern.
	 * @param regex the regular expression string to remove
	 * @return the removed Pattern object, or null if not found
	 */
	public static Pattern remove(String regex) {
		return CACHE.remove(regex);
	}

	/**
	 * Clears all patterns from the cache. This method removes all compiled patterns from
	 * the cache, freeing memory.
	 */
	public static void clear() {
		CACHE.clear();
	}

}
