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

package space.x9x.radp.commons.env;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import lombok.experimental.UtilityClass;

/**
 * Utility class providing commonly used character encodings. This class defines constants
 * for standard character sets and their names, including UTF-8, UTF-16, ISO-8859-1,
 * US-ASCII, and GBK. It provides both the Charset objects and their corresponding name
 * strings.
 *
 * @author RADP x9x
 * @since 2024-09-30 13:03
 */
@UtilityClass
public class Charsets {

	/**
	 * GBK charset encoding.
	 */
	public static final Charset GBK = Charset.forName("GBK");

	/**
	 * ISO-8859-1 charset encoding.
	 */
	public static final Charset ISO_8859_1 = StandardCharsets.ISO_8859_1;

	/**
	 * US-ASCII charset encoding.
	 */
	public static final Charset US_ASCII = StandardCharsets.US_ASCII;

	/**
	 * UTF-8 charset encoding.
	 */
	public static final Charset UTF_8 = StandardCharsets.UTF_8;

	/**
	 * UTF-16 charset encoding.
	 */
	public static final Charset UTF_16 = StandardCharsets.UTF_16;

	/**
	 * UTF-16BE (Big Endian) charset encoding.
	 */
	public static final Charset UTF_16BE = StandardCharsets.UTF_16BE;

	/**
	 * UTF-16LE (Little Endian) charset encoding.
	 */
	public static final Charset UTF_16LE = StandardCharsets.UTF_16LE;

	/**
	 * Name of the GBK charset.
	 */
	public static final String GBK_NAME = GBK.name();

	/**
	 * Name of the ISO-8859-1 charset.
	 */
	public static final String ISO_8859_1_NAME = ISO_8859_1.name();

	/**
	 * Name of the US-ASCII charset.
	 */
	public static final String US_ASCII_NAME = US_ASCII.name();

	/**
	 * Name of the UTF-8 charset.
	 */
	public static final String UTF_8_NAME = UTF_8.name();

	/**
	 * Name of the UTF-16 charset.
	 */
	public static final String UTF_16_NAME = UTF_16.name();

	/**
	 * Name of the UTF-16BE (Big Endian) charset.
	 */
	public static final String UTF_16BE_NAME = UTF_16BE.name();

	/**
	 * Name of the UTF-16LE (Little Endian) charset.
	 */
	public static final String UTF_16LE_NAME = UTF_16LE.name();

}
