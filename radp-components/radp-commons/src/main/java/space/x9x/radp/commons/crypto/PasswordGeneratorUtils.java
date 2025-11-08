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

package space.x9x.radp.commons.crypto;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import lombok.experimental.UtilityClass;
import org.passay.CharacterData;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.EnglishSequenceData;
import org.passay.IllegalSequenceRule;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.PasswordGenerator;
import org.passay.PasswordValidator;
import org.passay.RuleResult;
import org.passay.WhitespaceRule;

/**
 * Password generation and validation utilities powered by Passay.
 * <p>
 * <pre>{@code
 * 		// 1) 默认强密码（16 位）
 * 		String p1 = PasswordGeneratorUtils.generate();
 * 		System.out.println("Default: " + p1);
 *
 * 		// 2) 自定义策略：长度 14~64；每类至少 2；避开易混淆字符；自定义符号集
 * 		Policy policy = Policy.builder()
 * 			.withLength(14, 64)
 * 			.minUpper(2).minLower(2).minDigit(2).minSpecial(2)
 * 			.specialChars("!@#$%^&*~?")
 * 			.avoidAmbiguous(true)
 * 			.sequenceLength(3)
 * 			.build();
 *
 * 		String p2 = PasswordGeneratorUtils.generate(policy, 18);
 * 		System.out.println("Custom:  " + p2);
 *
 * 		// 3) 校验已有密码
 * 		ValidationResult vr = PasswordGeneratorUtils.validate(p2, policy);
 * 		System.out.println("Valid? " + vr.valid + (vr.valid ? "" : " -> " + vr.messages));
 * }</pre>
 *
 * @author x9x
 * @since 2025-09-15 23:47
 */
@UtilityClass
public final class PasswordGeneratorUtils {

	/**
	 * Secure random source used for shuffling and character selection.
	 */
	private static final SecureRandom SECURE_RANDOM = new SecureRandom();

	/**
	 * Passay password generator instance reused for efficiency.
	 */
	private static final PasswordGenerator PASS_GEN = new PasswordGenerator();

	/**
	 * 默认特殊字符.
	 */
	private static final String DEFAULT_SPECIALS = "!@#$%^&*()_+-=[]{}|;:,.<>?";

	/**
	 * 生成默认策略的强密码（16 位）.
	 */
	public static String generate() {
		return generate(16);
	}

	/**
	 * 生成指定长度、默认策略的强密码.
	 */
	public static String generate(int length) {
		return generate(Policy.builder().withLength(12, Math.max(12, Math.min(length, 128))).build(), length);
	}

	/**
	 * 使用自定义策略生成强密码（length 必须在策略区间内）.
	 */
	public static String generate(Policy policy, int length) {
		Objects.requireNonNull(policy, "policy");
		if (length < policy.minLength || length > policy.maxLength) {
			throw new IllegalArgumentException(
					"length must be in [" + policy.minLength + ", " + policy.maxLength + "]");
		}

		// 1) 准备字符类规则(最小出现次数).
		List<CharacterRule> rules = new ArrayList<>();
		if (policy.minUpper > 0) {
			rules.add(new CharacterRule(EnglishCharacterData.UpperCase, policy.minUpper));
		}
		if (policy.minLower > 0) {
			rules.add(new CharacterRule(EnglishCharacterData.LowerCase, policy.minLower));
		}
		if (policy.minDigit > 0) {
			rules.add(new CharacterRule(EnglishCharacterData.Digit, policy.minDigit));
		}
		if (policy.minSpecial > 0) {
			rules.add(new CharacterRule(specials(policy), policy.minSpecial));
		}

		int minRequired = policy.minUpper + policy.minLower + policy.minDigit + policy.minSpecial;
		if (minRequired > length) {
			throw new IllegalArgumentException(
					"Sum of minimum required characters (" + minRequired + ") exceeds length " + length);
		}

		// 2) 生成.
		String pwd = PASS_GEN.generatePassword(length, rules);

		// 3) 按需剔除易混淆字符(如 0/O, 1/l/I).
		if (policy.avoidAmbiguous) {
			pwd = replaceAmbiguous(pwd, policy);
		}

		// 4) 打散顺序以减少模式偏差.
		return shuffle(pwd);
	}

	/**
	 * 校验密码是否符合策略.
	 * @param password 带校验的密码
	 * @param policy 密码策略
	 * @return {@link ValidationResult} 包含是否通过与原因
	 */
	public static ValidationResult validate(String password, Policy policy) {
		Objects.requireNonNull(password, "password");
		Objects.requireNonNull(policy, "policy");

		PasswordValidator validator = new PasswordValidator(
				Arrays.asList(new LengthRule(policy.minLength, policy.maxLength),
						// 各类最小数量
						new CharacterRule(EnglishCharacterData.UpperCase, policy.minUpper),
						new CharacterRule(EnglishCharacterData.LowerCase, policy.minLower),
						new CharacterRule(EnglishCharacterData.Digit, policy.minDigit),
						new CharacterRule(specials(policy), policy.minSpecial),
						// 约束：不允许空白
						new WhitespaceRule(),
						// 约束：避免连续序列（如 abc/123/qwe），长度阈值可在策略里改
						new IllegalSequenceRule(EnglishSequenceData.Alphabetical, policy.sequenceLength, false),
						new IllegalSequenceRule(EnglishSequenceData.Numerical, policy.sequenceLength, false),
						new IllegalSequenceRule(EnglishSequenceData.USQwerty, policy.sequenceLength, false)
				// 也可加：new RepeatCharacterRegexRule(3) 避免同字符连用 3 次
				));

		RuleResult result = validator.validate(new PasswordData(password));
		if (result.isValid()) {
			return ValidationResult.ok();
		}
		List<String> messages = validator.getMessages(result); // 英文默认提示
		return ValidationResult.fail(messages);
	}

	private static CharacterData specials(Policy policy) {
		final String chars = policy.specialChars == null || policy.specialChars.isEmpty() ? DEFAULT_SPECIALS
				: policy.specialChars;
		return new CharacterData() {
			@Override
			public String getErrorCode() {
				return "ERR_SPECIAL";
			}

			@Override
			public String getCharacters() {
				return chars;
			}
		};
	}

	private static String shuffle(String s) {
		List<Character> list = new ArrayList<>(s.length());
		for (char c : s.toCharArray()) {
			list.add(c);
		}
		Collections.shuffle(list, SECURE_RANDOM);
		StringBuilder sb = new StringBuilder(list.size());
		for (char c : list) {
			sb.append(c);
		}
		return sb.toString();
	}

	/**
	 * 易混淆特殊字符集.
	 */
	private static final Set<Character> AMBIGUOUS = new HashSet<>(Arrays.asList('0', 'O', 'o', '1', 'l', 'I'));

	private static String replaceAmbiguous(String pwd, Policy policy) {
		if (pwd.chars().noneMatch(c -> AMBIGUOUS.contains((char) c))) {
			return pwd;
		}

		String specials = policy.specialChars == null || policy.specialChars.isEmpty() ? DEFAULT_SPECIALS
				: policy.specialChars;

		StringBuilder sb = new StringBuilder(pwd.length());
		for (char ch : pwd.toCharArray()) {
			if (!AMBIGUOUS.contains(ch)) {
				sb.append(ch);
				continue;
			}
			// 用更清晰的替换字符
			if (ch == '0') {
				sb.append('9');
			}
			else if (ch == 'O' || ch == 'o') {
				sb.append('Q');
			}
			else if (ch == '1' || ch == 'l' || ch == 'I') {
				sb.append('7');
			}
			else {
				sb.append(oneOf(specials));
			}
		}
		return sb.toString();
	}

	private static char oneOf(String candidates) {
		return candidates.charAt(SECURE_RANDOM.nextInt(candidates.length()));
	}

	/**
	 * 密码策略.
	 */
	public static final class Policy {

		/**
		 * 最小密码长度.
		 */
		final int minLength;

		/**
		 * 最大密码长度.
		 */
		final int maxLength;

		/**
		 * 至少有几个大写字母.
		 */
		final int minUpper;

		/**
		 * 至少有几个小写字母.
		 */
		final int minLower;

		/**
		 * 至少有几个数字.
		 */
		final int minDigit;

		/**
		 * 至少有几个特殊字符.
		 */
		final int minSpecial;

		/**
		 * 特殊字符集.
		 */
		final String specialChars;

		/**
		 * 是否避免易混淆字符(如 0/O, 1/l/I 等).
		 */
		final boolean avoidAmbiguous;

		/**
		 * 连续序列长度阈值(用于序列检测规则).
		 */
		final int sequenceLength;

		private Policy(Builder b) {
			this.minLength = b.minLength;
			this.maxLength = b.maxLength;
			this.minUpper = b.minUpper;
			this.minLower = b.minLower;
			this.minDigit = b.minDigit;
			this.minSpecial = b.minSpecial;
			this.specialChars = b.specialChars;
			this.avoidAmbiguous = b.avoidAmbiguous;
			this.sequenceLength = b.sequenceLength;
		}

		public static Builder builder() {
			return new Builder();
		}

		public static final class Builder {

			/**
			 * 最小密码长度(默认 12).
			 */
			private int minLength = 12;

			/**
			 * 最大密码长度(默认 128).
			 */
			private int maxLength = 128;

			/**
			 * 至少有几个大写字母(默认 1).
			 */
			private int minUpper = 1;

			/**
			 * 至少有几个小写字母(默认 1).
			 */
			private int minLower = 1;

			/**
			 * 至少有几个数字(默认 1).
			 */
			private int minDigit = 1;

			/**
			 * 至少有几个特殊字符(默认 1).
			 */
			private int minSpecial = 1;

			/**
			 * 自定义符号字符集(默认 DEFAULT_SPECIALS).
			 */
			private String specialChars = DEFAULT_SPECIALS;

			/**
			 * 是否避免易混淆字符(默认 false).
			 */
			private boolean avoidAmbiguous = false;

			/**
			 * 连续序列长度阈值(默认 3).
			 */
			private int sequenceLength = 3;

			/**
			 * 长度上下限(如 12~128).
			 */
			public Builder withLength(int min, int max) {
				this.minLength = min;
				this.maxLength = max;
				return this;
			}

			public Builder minUpper(int n) {
				this.minUpper = n;
				return this;
			}

			public Builder minLower(int n) {
				this.minLower = n;
				return this;
			}

			public Builder minDigit(int n) {
				this.minDigit = n;
				return this;
			}

			public Builder minSpecial(int n) {
				this.minSpecial = n;
				return this;
			}

			/**
			 * 自定义符号字符集.
			 */
			public Builder specialChars(String chars) {
				this.specialChars = Objects.requireNonNull(chars);
				return this;
			}

			/**
			 * 避免易混淆字符(0/O, 1/l/I 等).
			 */
			public Builder avoidAmbiguous(boolean v) {
				this.avoidAmbiguous = v;
				return this;
			}

			/**
			 * 连续序列的判定阈值(默认 3).
			 */
			public Builder sequenceLength(int len) {
				this.sequenceLength = Math.max(3, len);
				return this;
			}

			public Policy build() {
				return new Policy(this);
			}

		}

	}

	public static final class ValidationResult {

		/**
		 * 是否校验通过.
		 */
		public final boolean valid;

		/**
		 * 未通过时的提示信息列表.
		 */
		public final List<String> messages;

		private ValidationResult(boolean valid, List<String> messages) {
			this.valid = valid;
			this.messages = messages == null ? Collections.emptyList() : Collections.unmodifiableList(messages);
		}

		public static ValidationResult ok() {
			return new ValidationResult(true, Collections.emptyList());
		}

		public static ValidationResult fail(List<String> messages) {
			return new ValidationResult(false, messages);
		}

	}

}
