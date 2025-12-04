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

import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;

import lombok.experimental.UtilityClass;

import space.x9x.radp.commons.regex.pattern.RegexCache;

/**
 * Utility class for generating random strings and common test data. This class extends
 * Apache Commons Lang's RandomStringUtils and adds a set of practical generators and
 * validators used across the project.
 * <p>
 * Enhancements include:
 * <ul>
 * <li>Numeric helpers: generate N-digit numbers with a non-zero first digit and a quick
 * 11-digit generator.</li>
 * <li>Telecom: generate a plausible Mainland China 11-digit mobile number for
 * testing.</li>
 * <li>Usernames: generate valid random usernames (default rule: starts with a letter;
 * uses word characters \w); validate usernames via default regex or custom
 * {@link java.util.regex.Pattern} and a flexible {@code UsernameRule}
 * {@code Builder}.</li>
 * <li>Email: generate RFC 5322–compliant random emails and validate emails via
 * {@link space.x9x.radp.commons.regex.pattern.RegexCache#EMAIL}.</li>
 * <li>IP addresses: generate IPv4/IPv6 strings and validate using
 * {@link space.x9x.radp.commons.regex.pattern.RegexCache#IPV4} and
 * {@link space.x9x.radp.commons.regex.pattern.RegexCache#IPV6}, including rule-based
 * validation with {@code isValidIp(String, boolean, boolean)}.</li>
 * </ul>
 * <p>
 * Notes: Random data produced by these helpers is intended for demos/tests and does not
 * guarantee the existence of real-world resources (e.g., actual phone numbers or emails).
 *
 * @author x9x
 * @since 2024-11-20 16:41
 * @see org.apache.commons.lang3.RandomStringUtils
 * @see space.x9x.radp.commons.regex.pattern.RegexCache
 */
@UtilityClass
public class RandomStrUtils extends org.apache.commons.lang3.RandomStringUtils {

	/**
	 * 数字字符集.
	 */
	private static final char[] DIGITS = "0123456789".toCharArray();

	/**
	 * 字母字符集(用户名首字符使用).
	 */
	private static final char[] LETTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

	/**
	 * 用户名合法字符集: 字母、数字、下划线.
	 */
	private static final char[] USERNAME_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789_"
		.toCharArray();

	/**
	 * Generate a numeric string with the specified length where the first digit is
	 * non-zero.
	 * <p>
	 * For example, length=4 may yield "1234" or "9087".
	 * </p>
	 * @param length total number of digits; must be >= 1
	 * @return a numeric string of the given length; its first digit is in [1-9]
	 * @throws IllegalArgumentException if length &lt; 1
	 */
	public static String generateNDigitNumber(int length) {
		if (length < 1) {
			throw new IllegalArgumentException("length must be >= 1");
		}
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
	 * @return a string containing 11 digits representing a plausible mobile number
	 */
	public static String generateChineseMobile() {
		ThreadLocalRandom random = ThreadLocalRandom.current();
		int[] secondDigits = { 3, 4, 5, 6, 7, 8, 9 };
		StringBuilder sb = new StringBuilder(11);
		sb.append('1');
		sb.append(secondDigits[random.nextInt(secondDigits.length)]);
		for (int i = 0; i < 9; i++) {
			sb.append(DIGITS[random.nextInt(10)]);
		}
		return sb.toString();
	}

	/**
	 * 生成合法的随机用户名. 规则:
	 * <ul>
	 * <li>用户名需以字母开头</li>
	 * <li>允许的字符集: [A-Za-z0-9_]</li>
	 * <li>默认长度范围: 6-16</li>
	 * </ul>
	 * @return 合法的随机用户名
	 */
	public static String generateUsername() {
		return generateUsername(6, 16);
	}

	/**
	 * 生成指定长度的合法随机用户名.
	 * @param length 长度(必须 >= 3)
	 * @return 合法的随机用户名
	 */
	public static String generateUsername(int length) {
		return generateUsername(length, length);
	}

	/**
	 * 生成指定长度范围内的合法随机用户名.
	 * @param minLength 最小长度(必须 >= 3)
	 * @param maxLength 最大长度(必须 >= minLength)
	 * @return 合法的随机用户名
	 */
	public static String generateUsername(int minLength, int maxLength) {
		if (minLength < 3) {
			throw new IllegalArgumentException("minLength must be >= 3");
		}
		if (maxLength < minLength) {
			throw new IllegalArgumentException("maxLength must be >= minLength");
		}
		ThreadLocalRandom random = ThreadLocalRandom.current();
		int len = (minLength == maxLength) ? minLength : random.nextInt(minLength, maxLength + 1);
		StringBuilder sb = new StringBuilder(len);
		// 首字符必须是字母
		sb.append(LETTERS[random.nextInt(LETTERS.length)]);
		for (int i = 1; i < len; i++) {
			sb.append(USERNAME_CHARS[random.nextInt(USERNAME_CHARS.length)]);
		}
		// 尽量避免以下划线结尾(非必须,但更美观)
		if (sb.charAt(sb.length() - 1) == '_') {
			sb.setCharAt(sb.length() - 1, LETTERS[random.nextInt(LETTERS.length)]);
		}
		return sb.toString();
	}

	/**
	 * 常见顶级域名集合,用于生成随机邮箱.
	 */
	private static final String[] COMMON_TLDS = { "com", "net", "org", "io", "dev", "cn", "com.cn" };

	/**
	 * 生成随机合法邮箱. 例如: john_doe8@sample.org
	 * <ul>
	 * <li>本地部分使用 {@link #generateUsername()} 生成的小写用户名</li>
	 * <li>域名从常见域名和 TLD 随机组合</li>
	 * </ul>
	 * @return 合法邮箱地址
	 */
	public static String generateEmail() {
		ThreadLocalRandom random = ThreadLocalRandom.current();
		String local = generateUsername().toLowerCase(Locale.ROOT);
		// 生成域名的主机名标签(字母数字开头/结尾,中间允许字母数字和短横线)
		String host = buildDomainLabel(random.nextInt(4, 11));
		String tld = COMMON_TLDS[random.nextInt(COMMON_TLDS.length)];
		String email = local + "@" + host + "." + tld;
		// 兜底校验(开发期保证生成的确实是合法邮箱)
		return RegexCache.EMAIL.matcher(email).matches() ? email : (local + "@example.com");
	}

	/**
	 * 构建域名标签(字母数字开头/结尾,中间可含-),长度在 [2,63] 之内,这里按入参长度生成.
	 */
	private static String buildDomainLabel(int len) {
		if (len < 2) {
			len = 2;
		}
		if (len > 63) {
			len = 63;
		}
		ThreadLocalRandom random = ThreadLocalRandom.current();
		String lettersDigits = "abcdefghijklmnopqrstuvwxyz0123456789";
		String midChars = lettersDigits + "-";
		StringBuilder sb = new StringBuilder(len);
		sb.append(lettersDigits.charAt(random.nextInt(lettersDigits.length())));
		for (int i = 1; i < len - 1; i++) {
			sb.append(midChars.charAt(random.nextInt(midChars.length())));
		}
		sb.append(lettersDigits.charAt(random.nextInt(lettersDigits.length())));
		return sb.toString();
	}

	/**
	 * 校验用户名是否合法(使用 {@link space.x9x.radp.commons.regex.pattern.RegexCache#USERNAME} 规则).
	 * 支持字母、数字、下划线和中文，不能以下划线结尾，长度 1-18.
	 * @param username 待校验用户名
	 * @return 合法返回 true
	 */
	public static boolean isValidUsername(String username) {
		return username != null && RegexCache.USERNAME.matcher(username).matches();
	}

	/**
	 * 校验邮箱是否合法(基于 RFC5322 兼容的模式).
	 * @param email 邮箱
	 * @return 合法返回 true
	 */
	public static boolean isValidEmail(String email) {
		return email != null && RegexCache.EMAIL.matcher(email).matches();
	}

	/**
	 * 校验手机号是否合法(中国大陆，支持可选国家码 +86、86 或 0 前缀). 使用
	 * {@link space.x9x.radp.commons.regex.pattern.RegexCache#MOBILE} 规则进行校验.
	 * @param mobile 手机号
	 * @return 合法返回 true
	 */
	public static boolean isValidMobile(String mobile) {
		return mobile != null && RegexCache.MOBILE.matcher(mobile).matches();
	}

	/**
	 * 生成合法的随机 IPv4 地址. 例如: 192.168.3.25 每个段在 [0, 255] 范围内.
	 * @return 随机 IPv4 字符串
	 */
	public static String generateIPv4() {
		ThreadLocalRandom r = ThreadLocalRandom.current();
		return r.nextInt(256) + "." + r.nextInt(256) + "." + r.nextInt(256) + "." + r.nextInt(256);
	}

	/**
	 * 生成合法的随机 IPv6 地址(8 组 1-4 位十六进制,使用冒号分隔). 例如: 2001:db8:85a3:0:0:8a2e:370:7334
	 * @return 随机 IPv6 字符串
	 */
	public static String generateIPv6() {
		ThreadLocalRandom r = ThreadLocalRandom.current();
		StringBuilder sb = new StringBuilder(39);
		for (int i = 0; i < 8; i++) {
			int v = r.nextInt(0x10000); // 0..65535
			sb.append(Integer.toHexString(v)); // 小写,无前导 0,合法
			if (i < 7) {
				sb.append(':');
			}
		}
		return sb.toString();
	}

	/**
	 * 校验是否为合法 IPv4 地址.
	 * @param ip 待校验字符串
	 * @return 合法返回 true
	 */
	public static boolean isValidIPv4(String ip) {
		return ip != null && RegexCache.IPV4.matcher(ip).matches();
	}

	/**
	 * 校验是否为合法 IPv6 地址.
	 * @param ip 待校验字符串
	 * @return 合法返回 true
	 */
	public static boolean isValidIPv6(String ip) {
		return ip != null && RegexCache.IPV6.matcher(ip).matches();
	}

	/**
	 * 校验是否为合法 IP 地址(IPv4 或 IPv6 均可).
	 * @param ip 待校验字符串
	 * @return 合法返回 true
	 */
	public static boolean isValidIp(String ip) {
		return isValidIPv4(ip) || isValidIPv6(ip);
	}

	/**
	 * 按指定规则校验 IP 是否合法.
	 * <ul>
	 * <li>allowIPv4: 允许 IPv4</li>
	 * <li>allowIPv6: 允许 IPv6</li>
	 * </ul>
	 * @param ip 待校验字符串
	 * @param allowIPv4 是否允许 IPv4
	 * @param allowIPv6 是否允许 IPv6
	 * @return 合法返回 true；若两者均不允许则恒为 false
	 */
	public static boolean isValidIp(String ip, boolean allowIPv4, boolean allowIPv6) {
		boolean ok4 = allowIPv4 && isValidIPv4(ip);
		boolean ok6 = allowIPv6 && isValidIPv6(ip);
		return ok4 || ok6;
	}

	/**
	 * 使用自定义正则表达式校验用户名是否合法.
	 * @param username 待校验用户名
	 * @param pattern 自定义校验规则(非空)
	 * @return 合法返回 true；pattern 为 null 时返回 false
	 */
	public static boolean isValidUsername(String username, Pattern pattern) {
		return username != null && pattern != null && pattern.matcher(username).matches();
	}

	/**
	 * 使用自定义正则表达式校验用户名是否合法.
	 * @param username 待校验用户名
	 * @param regex 自定义正则(非空)
	 * @return 合法返回 true；regex 为空时返回 false
	 * @throws java.util.regex.PatternSyntaxException 当正则不合法时抛出
	 */
	public static boolean isValidUsername(String username, String regex) {
		return username != null && regex != null && RegexCache.get(regex, 0).matcher(username).matches();
	}

	/**
	 * 使用自定义规则生成合法用户名.
	 * @param rule 自定义规则(非空)
	 * @return 合法随机用户名
	 */
	public static String generateUsername(UsernameRule rule) {
		if (rule == null) {
			throw new IllegalArgumentException("rule must not be null");
		}
		ThreadLocalRandom random = ThreadLocalRandom.current();
		int len = (rule.minLength == rule.maxLength) ? rule.minLength
				: random.nextInt(rule.minLength, rule.maxLength + 1);
		StringBuilder sb = new StringBuilder(len);
		// 首字符
		sb.append(rule.firstChars.charAt(random.nextInt(rule.firstChars.length())));
		for (int i = 1; i < len; i++) {
			sb.append(rule.allowedChars.charAt(random.nextInt(rule.allowedChars.length())));
		}
		if (rule.disallowTrailingUnderscore && sb.charAt(sb.length() - 1) == '_') {
			// 若禁止以下划线结尾,则将末位替换为一个合法的非下划线字符
			char replacement;
			int guard = 0;
			do {
				replacement = rule.allowedChars.charAt(random.nextInt(rule.allowedChars.length()));
				guard++;
			}
			while (replacement == '_' && guard < 8);
			if (replacement == '_') {
				// 仍然是 '_',那就从首字符集合中取一个字母替换
				replacement = rule.firstChars.charAt(random.nextInt(rule.firstChars.length()));
			}
			sb.setCharAt(sb.length() - 1, replacement);
		}
		return sb.toString();
	}

	/**
	 * 使用自定义规则校验用户名是否合法. 若 rule.validatePattern 非空,则优先基于其进行校验；否则按集合/长度规则校验.
	 * @param username 待校验用户名
	 * @param rule 用户名规则(非空)
	 * @return 合法返回 true
	 */
	public static boolean isValidUsername(String username, UsernameRule rule) {
		if (username == null || rule == null) {
			return false;
		}
		if (rule.validatePattern != null) {
			return rule.validatePattern.matcher(username).matches();
		}
		int len = username.length();
		if (len < rule.minLength || len > rule.maxLength) {
			return false;
		}
		char first = username.charAt(0);
		if (notContainsChar(rule.firstChars, first)) {
			return false;
		}
		for (int i = 1; i < len; i++) {
			if (notContainsChar(rule.allowedChars, username.charAt(i))) {
				return false;
			}
		}
		return !rule.disallowTrailingUnderscore || username.charAt(len - 1) != '_';
	}

	/**
	 * 便捷重载: 自定义字符集与长度的用户名生成.
	 * @param minLength 最小长度
	 * @param maxLength 最大长度
	 * @param firstChars 首字符允许集合
	 * @param allowedChars 总体允许字符集合
	 * @return 合法随机用户名
	 */
	public static String generateUsername(int minLength, int maxLength, String firstChars, String allowedChars) {
		return generateUsername(minLength, maxLength, firstChars, allowedChars, false);
	}

	/**
	 * 便捷重载: 自定义字符集与长度的用户名生成.
	 * @param minLength 最小长度
	 * @param maxLength 最大长度
	 * @param firstChars 首字符允许集合
	 * @param allowedChars 总体允许字符集合
	 * @param disallowTrailingUnderscore 是否禁止以下划线结尾
	 * @return 合法随机用户名
	 */
	public static String generateUsername(int minLength, int maxLength, String firstChars, String allowedChars,
			boolean disallowTrailingUnderscore) {
		UsernameRule rule = UsernameRule.builder()
			.firstChars(firstChars)
			.allowedChars(allowedChars)
			.minLength(minLength)
			.maxLength(maxLength)
			.disallowTrailingUnderscore(disallowTrailingUnderscore)
			.build();
		return generateUsername(rule);
	}

	private static boolean notContainsChar(String set, char c) {
		return set.indexOf(c) < 0;
	}

	/**
	 * 可自定义的用户名规则. 支持: 首字符集合、整体可用字符集合、长度范围与“禁止以下划线结尾”选项； 也可提供自定义 Pattern
	 * 作为最终校验规则(若提供,则以其为准).
	 */
	public static final class UsernameRule {

		/**
		 * 允许的首字符集合(必填).
		 */
		public final String firstChars;

		/**
		 * 允许出现的字符集合(必填).
		 */
		public final String allowedChars;

		/**
		 * 最小长度(>=1).
		 */
		public final int minLength;

		/**
		 * 最大长度(>=minLength).
		 */
		public final int maxLength;

		/**
		 * 是否禁止以下划线结尾.
		 */
		public final boolean disallowTrailingUnderscore;

		/**
		 * 可选: 自定义正则,若非空,则以该正则为最终校验.
		 */
		public final Pattern validatePattern;

		/**
		 * 创建 {@link UsernameRule} 的构建器.
		 * @return builder 实例
		 */
		public static Builder builder() {
			return new Builder();
		}

		/**
		 * 提供与默认逻辑一致的规则: 首字母必须为英文字母,允许 [A-Za-z0-9_],长度 3-16.
		 * @return 默认用户名规则
		 */
		public static UsernameRule defaultRule() {
			return builder().firstChars(new String(LETTERS))
				.allowedChars(new String(USERNAME_CHARS))
				.minLength(3)
				.maxLength(16)
				.build();
		}

		private UsernameRule(Builder b) {
			validate(b.firstChars, b.allowedChars, b.minLength, b.maxLength);
			this.firstChars = b.firstChars;
			this.allowedChars = b.allowedChars;
			this.minLength = b.minLength;
			this.maxLength = b.maxLength;
			this.disallowTrailingUnderscore = b.disallowTrailingUnderscore;
			this.validatePattern = b.validatePattern;
		}

		private static void validate(String firstChars, String allowedChars, int minLength, int maxLength) {
			if (firstChars == null || firstChars.isEmpty()) {
				throw new IllegalArgumentException("firstChars must not be empty");
			}
			if (allowedChars == null || allowedChars.isEmpty()) {
				throw new IllegalArgumentException("allowedChars must not be empty");
			}
			if (minLength < 1) {
				throw new IllegalArgumentException("minLength must be >= 1");
			}
			if (maxLength < minLength) {
				throw new IllegalArgumentException("maxLength must be >= minLength");
			}
		}

		/**
		 * Builder for {@link UsernameRule}.
		 */
		public static final class Builder {

			/**
			 * Allowed first character set. Required.
			 */
			private String firstChars;

			/**
			 * Allowed character sets for the rest of the username. Required.
			 */
			private String allowedChars;

			/**
			 * Minimum length of username. Default is 3.
			 */
			private int minLength = 3;

			/**
			 * Maximum length of username. Default is 16.
			 */
			private int maxLength = 16;

			/**
			 * Whether to disallow an underscore at the end. Default is false.
			 */
			private boolean disallowTrailingUnderscore = false;

			/**
			 * Optional custom validation pattern. If set, it will be used for final
			 * validation.
			 */
			private Pattern validatePattern;

			/**
			 * 设置首字符允许集合.
			 * @param firstChars 首字符允许集合
			 * @return this builder
			 */
			public Builder firstChars(String firstChars) {
				this.firstChars = firstChars;
				return this;
			}

			/**
			 * 设置总体允许字符集合.
			 * @param allowedChars 允许字符集合
			 * @return this builder
			 */
			public Builder allowedChars(String allowedChars) {
				this.allowedChars = allowedChars;
				return this;
			}

			/**
			 * 设置最小长度.
			 * @param minLength 最小长度
			 * @return this builder
			 */
			public Builder minLength(int minLength) {
				this.minLength = minLength;
				return this;
			}

			/**
			 * 设置最大长度.
			 * @param maxLength 最大长度
			 * @return this builder
			 */
			public Builder maxLength(int maxLength) {
				this.maxLength = maxLength;
				return this;
			}

			/**
			 * 设置是否禁止以下划线结尾.
			 * @param disallowTrailingUnderscore 是否禁止以下划线结尾
			 * @return this builder
			 */
			public Builder disallowTrailingUnderscore(boolean disallowTrailingUnderscore) {
				this.disallowTrailingUnderscore = disallowTrailingUnderscore;
				return this;
			}

			/**
			 * 设置自定义最终校验正则.
			 * @param validatePattern 自定义正则(可选)
			 * @return this builder
			 */
			public Builder validatePattern(Pattern validatePattern) {
				this.validatePattern = validatePattern;
				return this;
			}

			/**
			 * 构建 {@link UsernameRule}.
			 * @return 规则实例
			 */
			public UsernameRule build() {
				return new UsernameRule(this);
			}

		}

	}

}
