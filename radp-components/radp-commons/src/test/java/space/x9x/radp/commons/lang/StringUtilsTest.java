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

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link StringUtils}.
 *
 * @author x9x
 * @since 2024-09-23 13:57
 */
class StringUtilsTest {

	@Test
	void testCamelToSplitName() {
		// Test with an empty string
		assertThat(StringUtils.camelToSplitName("", "-")).isEqualTo("");

		// Test with null
		assertThat(StringUtils.camelToSplitName(null, "-")).isNull();

		// Test with no uppercase letters
		assertThat(StringUtils.camelToSplitName("lowercase", "-")).isEqualTo("lowercase");

		// Test with uppercase letters
		assertThat(StringUtils.camelToSplitName("camelCase", "-")).isEqualTo("camel-case");

		// Test with uppercase letters at the beginning
		assertThat(StringUtils.camelToSplitName("CamelCase", "-")).isEqualTo("camel-case");

		// Test with multiple uppercase letters
		assertThat(StringUtils.camelToSplitName("camelCaseWithMultipleWords", "-"))
			.isEqualTo("camel-case-with-multiple-words");

		// Test with different separator
		assertThat(StringUtils.camelToSplitName("camelCase", "_")).isEqualTo("camel_case");

		// Test with consecutive uppercase letters
		assertThat(StringUtils.camelToSplitName("camelCaseURL", "-")).isEqualTo("camel-case-url");
	}

}
