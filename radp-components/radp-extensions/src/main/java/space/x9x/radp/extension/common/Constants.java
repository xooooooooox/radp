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

package space.x9x.radp.extension.common;

import java.util.regex.Pattern;

import lombok.experimental.UtilityClass;

/**
 * 常量类，用于存储扩展系统中使用的常量值.
 * <p>
 * The Constants class for storing constant values used in the extension system. This
 * utility class provides common constants that are used throughout the extension
 * framework to ensure consistency and avoid duplication.
 *
 * @author x9x
 * @since 2024-09-24 19:31
 */
@UtilityClass
public class Constants {

	/**
	 * Regular expression pattern for splitting strings by commas. This pattern matches
	 * one or more commas with optional whitespace before and after. It is used throughout
	 * the extension system to parse comma-separated lists of values, such as extension
	 * names, configuration values, etc.
	 */
	public static final Pattern COMMA_SPLIT_PATTERN = Pattern.compile("\\s*[,]+\\s*");

}
