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

import lombok.experimental.UtilityClass;

/**
 * Commonly used regular expressions. This utility class provides a collection of
 * pre-defined regular expression patterns for validating common data formats such as
 * email addresses, phone numbers, IP addresses, and more.
 *
 * @author IO x9x
 * @since 2024-12-27 12:50
 */
@UtilityClass
public class Regex {

	/**
	 * Alphabetic characters. Matches one or more letters (a-z, A-Z).
	 */
	public static final String WORD = "[a-zA-Z]+";

	/**
	 * Chinese characters. Matches one or more Chinese characters in the Unicode range.
	 */
	public static final String CHINESE = "^[\\u4e00-\\u9fa5]+$";

	/**
	 * Username pattern. Matches usernames containing a-z, A-Z, 0-9, underscore, and
	 * Chinese characters. Cannot end with an underscore. Length must be between 1 and 18
	 * characters.
	 */
	public static final String USERNAME = "^[\\w\\u4e00-\\u9fa5]{1,18}(?<!_)$";

	/**
	 * Email address pattern. Compliant with RFC 5322 standard for email validation.
	 */
	public static final String EMAIL = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)])";

	/**
	 * Mobile phone number pattern for Mainland China. Matches Chinese mobile phone
	 * numbers with optional country code (+86).
	 */
	public static final String MOBILE = "(?:0|86|\\+86)?1[3-9]\\d{9}";

	/**
	 * Mobile phone number pattern for Hong Kong. <br>
	 * Country codes reference: <br>
	 * Mainland China: +86 <br>
	 * Hong Kong: +852 <br>
	 * Macao: +853 <br>
	 * Taiwan: +886
	 */
	public static final String MOBILE_HK = "(?:0|852|\\+852)?\\d{8}";

	/**
	 * Mobile phone number pattern for Taiwan. Matches Taiwanese mobile phone numbers with
	 * optional country code (+886).
	 */
	public static final String MOBILE_TW = "(?:0|886|\\+886)?(?:|-)09\\d{8}";

	/**
	 * Mobile phone number pattern for Macao. Matches Macanese mobile phone numbers with
	 * optional country code (+853).
	 */
	public static final String MOBILE_MO = "(?:0|853|\\+853)?(?:|-)6\\d{7}";

	/**
	 * Landline telephone number pattern for China. Matches Chinese landline phone numbers
	 * with area code and local number.
	 */
	public static final String TELEPHONE = "(010|02\\d|0[3-9]\\d{2})-?(\\d{6,8})";

	/**
	 * Service telephone number pattern. Matches Chinese service numbers including 400 and
	 * 800 toll-free numbers.
	 */
	public static final String TELEPHONE_400_800 = "0\\d{2,3}[\\- ]?[1-9]\\d{6,7}|[48]00[\\- ]?[1-9]\\d{6}";

	/**
	 * Chinese ID card number pattern (15-digit format). Matches the older 15-digit format
	 * of Chinese national ID cards.
	 */
	public static final String ID_CARD_15 = "^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$";

	/**
	 * Chinese ID card number pattern (18-digit format). Matches the current 18-digit
	 * format of Chinese national ID cards, including the check digit which can be a
	 * number or 'X'.
	 */
	public static final String ID_CARD_18 = "[1-9]\\d{5}[1-2]\\d{3}((0\\d)|(1[0-2]))(([012]\\d)|3[0-1])\\d{3}(\\d|X|x)";

	/**
	 * Unified Social Credit Code pattern for Chinese organizations. Matches the
	 * 18-character code used to identify registered organizations in China.
	 */
	public static final String CREDIT_CODE = "^[0-9A-HJ-NPQRTUWXY]{2}\\d{6}[0-9A-HJ-NPQRTUWXY]{10}$";

	/**
	 * Chinese postal code pattern. Matches the 6-digit postal codes used in China.
	 */
	public static final String ZIP_CODE = "^(0[1-7]|1[0-356]|2[0-7]|3[0-6]|4[0-7]|5[0-7]|6[0-7]|7[0-5]|8[0-9]|9[0-8])\\d{4}|99907[78]$";

	/**
	 * Birthday date pattern. Matches various date formats for birthdays, supporting
	 * different separators.
	 */
	public static final String BIRTHDAY = "^(\\d{2,4})([/\\-.年]?)(\\d{1,2})([/\\-.月]?)(\\d{1,2})日?$";

	/**
	 * Tencent QQ number pattern. Matches QQ numbers, which must start with a non-zero
	 * digit and have at least 5 digits.
	 */
	public static final String TENCENT_QQ = "[1-9][0-9]{4,}";

	/**
	 * Chinese vehicle license plate number pattern. Matches the various formats of
	 * license plates used in mainland China, including special plates for different
	 * regions and purposes.
	 */
	public static final String PLATE_NUMBER_CN = "^(([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领]"
			+ "[A-Z](([0-9]{5}[ABCDEFGHJK])|([ABCDEFGHJK]([A-HJ-NP-Z0-9])[0-9]{4})))|"
			+ "([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领]\\d{3}\\d{1,3}[领])|"
			+ "([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领][A-Z][A-HJ-NP-Z0-9]{4}[A-HJ-NP-Z0-9挂学警港澳使领]))$";

	/**
	 * Vehicle Identification Number (VIN) pattern. Matches the 17-character VIN used to
	 * uniquely identify motor vehicles.
	 */
	public static final String CAR_VIN = "^[A-Za-z0-9]{17}$";

	/**
	 * Chinese driving license number pattern. Matches the 12-digit format of Chinese
	 * driving license numbers.
	 */
	public static final String CAR_DRIVING_LICENCE = "^[0-9]{12}$";

	/**
	 * IPv4 address pattern. Matches valid IPv4 addresses with proper octet range
	 * validation (0-255).
	 */
	public static final String IPV4 = "\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)"
			+ "\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)"
			+ "\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b";

	/**
	 * IPv6 address pattern. Matches valid IPv6 addresses in various formats, including
	 * compressed notation.
	 */
	public static final String IPV6 = "(([0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,7}:|"
			+ "([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|"
			+ "([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|"
			+ "([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}|[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})|:"
			+ "((:[0-9a-fA-F]{1,4}){1,7}|:)|fe80:(:[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]+|::(ffff(:0{1,4})?:)?"
			+ "((25[0-5]|(2[0-4]|1?[0-9])?[0-9])\\.){3}(25[0-5]|(2[0-4]|1?[0-9])?[0-9])|([0-9a-fA-F]{1,4}:)"
			+ "{1,4}:((25[0-5]|(2[0-4]|1?[0-9])?[0-9])\\.){3}(25[0-5]|(2[0-4]|1?[0-9])?[0-9]))";

	/**
	 * URL pattern. Matches URLs with various protocols (http, https, ftp, etc.).
	 */
	public static final String URL = "[a-zA-z]+://[^\\s]*";

	/**
	 * HTTP URL pattern. Matches HTTP and HTTPS URLs with optional protocol prefix.
	 */
	public static final String HTTP = "(https://|http://)?([\\w-]+\\.)+[\\w-]+(:\\d+)*(/[\\w- ./?%&=]*)?";

	/**
	 * Currency amount pattern. Matches numeric values representing currency amounts, with
	 * optional decimal places.
	 */
	public static final String MONEY = "^(\\d+(?:\\.\\d+)?)$";

	/**
	 * ISO date pattern (YYYY-MM-DD). Matches ISO 8601 date format with validation for
	 * valid dates, including leap year validation for February 29.
	 */
	public static final String DATE = "^(?:(?!0000)[0-9]{4}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1[0-9]|2[0-8])|"
			+ "(?:0[13-9]|1[0-2])-(?:29|30)|(?:0[13578]|1[02])-31)|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|"
			+ "(?:0[48]|[2468][048]|[13579][26])00)-02-29)$";

	/**
	 * Time pattern (HH:MM[:SS]). Matches time format with hours, minutes, and optional
	 * seconds.
	 */
	public static final String TIME = "\\d{1,2}:\\d{1,2}(:\\d{1,2})?";

	/**
	 * UUID pattern. Matches standard UUID format (8-4-4-4-12 hexadecimal digits).
	 */
	public static final String UUID = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$";

	/**
	 * MAC address pattern. Matches MAC addresses in various formats, including colon and
	 * hyphen separators.
	 */
	public static final String MAC_ADDRESS = "((?:[a-fA-F0-9]{1,2}[:-]){5}[a-fA-F0-9]{1,2})|0x(\\d{12}).+ETHER";

	/**
	 * Hexadecimal string pattern. Matches strings containing only hexadecimal characters
	 * (0-9, a-f, A-F).
	 */
	public static final String HEX = "^[a-fA-F0-9]+$";

	/**
	 * Integer pattern. Matches positive or negative integers (excluding zero with a
	 * sign).
	 */
	public static final String INTEGER = "^-?[1-9]\\d*$";

	/**
	 * Positive integer pattern. Matches integers greater than zero.
	 */
	public static final String INTEGER_POSITIVE = "^[1-9]\\d*$";

	/**
	 * Non-positive integer pattern. Matches integers less than or equal to zero.
	 */
	public static final String INTEGER_POSITIVE_REVERSE = "^-[1-9]\\d*|0$";

	/**
	 * Negative integer pattern. Matches integers less than zero.
	 */
	public static final String INTEGER_NEGATIVE = "^-[1-9]\\d*$";

	/**
	 * Non-negative integer pattern. Matches integers greater than or equal to zero.
	 */
	public static final String INTEGER_NEGATIVE_REVERSE = "^[1-9]\\d*|0$";

	/**
	 * Floating-point number pattern. Matches positive or negative floating-point numbers.
	 */
	public static final String FLOAT = "^-?[1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*$";

	/**
	 * Positive floating-point number pattern. Matches floating-point numbers greater than
	 * zero.
	 */
	public static final String FLOAT_POSITIVE = "^[1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*$";

	/**
	 * Negative floating-point number pattern. Matches floating-point numbers less than
	 * zero.
	 */
	public static final String FLOAT_NEGATIVE = "^-[1-9]\\d*\\.\\d*|-0\\.\\d*[1-9]\\d*$";

}
