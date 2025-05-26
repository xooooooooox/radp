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

package space.x9x.radp.commons.regex;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import space.x9x.radp.commons.regex.pattern.RegexCache;

/**
 * Utility class for regular expression operations.
 *
 * <p>
 * <strong>使用示例：</strong>
 * </p>
 * <pre>{@code
 * public class Demo {
 *     public static void main(String[] args) {
 *         // 1) 判断整个字符串是否匹配正则
 *         boolean isMatch = RegexUtils.isMatch("\\d+", "12345");
 *         System.out.println(isMatch); // true
 *
 *         // 2) 查找目标字符串中是否存在匹配
 *         boolean found = RegexUtils.find("abc", "xxx abc yyy");
 *         System.out.println(found); // true
 *
 *         // 3) 分组匹配示例
 *         List<String> groups = RegexUtils.group("([a-z]+)", "abc def ghi");
 *         System.out.println(groups); // [abc, def, ghi]
 *
 *         // 4) 替换所有匹配项
 *         String replaced = RegexUtils.replaceAll("\\s+", "abc def", "_");
 *         System.out.println(replaced); // "abc_def"
 *     }
 * }
 * }</pre>
 *
 * @author IO x9x
 * @see space.x9x.radp.commons.regex.pattern.Regex
 * @since 2024-12-27 12:50
 */
@UtilityClass
public class RegexUtils {

	/**
	 * Checks if the entire input sequence matches the regex pattern.
	 * @param regex the regular expression pattern
	 * @param input the character sequence to be matched
	 * @return true if the entire input sequence matches the pattern
	 */
	public static boolean isMatch(@NonNull String regex, @NonNull CharSequence input) {
		return Pattern.matches(regex, input);
	}

	/**
	 * Searches for the first occurrence of the regex pattern in the input sequence.
	 * @param regex the regular expression pattern
	 * @param input the character sequence to be searched
	 * @return true if the pattern is found in the input sequence
	 */
	public static boolean find(@NonNull String regex, @NonNull CharSequence input) {
		Pattern pattern = RegexCache.get(regex, Pattern.CASE_INSENSITIVE);
		return pattern.matcher(input).find();
	}

	/**
	 * Extracts all capturing groups from the input sequence that match the regex pattern.
	 * @param regex the regular expression pattern with capturing groups
	 * @param input the character sequence to be searched
	 * @return a list of all captured group values
	 */
	public static List<String> group(@NonNull String regex, @NonNull CharSequence input) {
		List<String> matches = new ArrayList<>();
		Pattern pattern = RegexCache.get(regex, Pattern.DOTALL);
		Matcher matcher = pattern.matcher(input);
		int i = 1;
		while (matcher.find()) {
			matches.add(matcher.group(i));
			i++;
		}
		return matches;
	}

	/**
	 * Extracts the first capturing group from the first match of the regex pattern in the
	 * input sequence.
	 * @param regex the regular expression pattern with capturing groups
	 * @param input the character sequence to be searched
	 * @return the value of the first capturing group, or null if no match is found
	 */
	public static String groupFirst(@NonNull String regex, @NonNull CharSequence input) {
		return group(regex, input, 1);
	}

	/**
	 * Extracts a specific capturing group from the first match of the regex pattern in
	 * the input sequence.
	 * @param regex the regular expression pattern with capturing groups
	 * @param input the character sequence to be searched
	 * @param groupIndex the index of the capturing group to extract
	 * @return the value of the specified capturing group, or null if no match is found
	 */
	public static String group(@NonNull String regex, @NonNull CharSequence input, int groupIndex) {
		Pattern pattern = RegexCache.get(regex, Pattern.DOTALL);
		Matcher matcher = pattern.matcher(input);
		if (matcher.find()) {
			return matcher.group(groupIndex);
		}
		return null;
	}

	/**
	 * Replaces all occurrences of the regex pattern in the input sequence with the
	 * specified replacement.
	 * @param regex the regular expression pattern
	 * @param input the character sequence to be processed
	 * @param replacement the string to replace each match with
	 * @return the resulting string after replacement
	 */
	public static String replaceAll(@NonNull String regex, @NonNull CharSequence input, @NonNull String replacement) {
		return Pattern.compile(regex).matcher(input).replaceAll(replacement);
	}

	/**
	 * Replaces the first occurrence of the regex pattern in the input sequence with the
	 * specified replacement.
	 * @param regex the regular expression pattern
	 * @param input the character sequence to be processed
	 * @param replacement the string to replace the first match with
	 * @return the resulting string after replacement
	 */
	public static String replaceFirst(@NonNull String regex, @NonNull CharSequence input, @NonNull String replacement) {
		Pattern pattern = RegexCache.get(regex, Pattern.DOTALL);
		return pattern.matcher(input).replaceFirst(replacement);
	}

}
