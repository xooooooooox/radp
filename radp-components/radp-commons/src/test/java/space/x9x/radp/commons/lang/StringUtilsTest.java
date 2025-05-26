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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Tests for {@link StringUtils}.
 *
 * @author IO x9x
 * @since 2024-09-23 13:57
 */
public class StringUtilsTest {

	@Test
	void testCamelToSplitName() {
		// Test with an empty string
		assertEquals("", StringUtils.camelToSplitName("", "-"));

		// Test with null
		assertNull(StringUtils.camelToSplitName(null, "-"));

		// Test with no uppercase letters
		assertEquals("lowercase", StringUtils.camelToSplitName("lowercase", "-"));

		// Test with uppercase letters
		assertEquals("camel-case", StringUtils.camelToSplitName("camelCase", "-"));

		// Test with uppercase letters at the beginning
		assertEquals("camel-case", StringUtils.camelToSplitName("CamelCase", "-"));

		// Test with multiple uppercase letters
		assertEquals("camel-case-with-multiple-words", StringUtils.camelToSplitName("camelCaseWithMultipleWords", "-"));

		// Test with different separator
		assertEquals("camel_case", StringUtils.camelToSplitName("camelCase", "_"));

		// Test with consecutive uppercase letters
		assertEquals("camel-case-url", StringUtils.camelToSplitName("camelCaseURL", "-"));
	}

}