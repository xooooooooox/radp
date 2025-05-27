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

package space.x9x.radp.smoke.tests.framework.asserts;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import space.x9x.radp.spring.framework.error.ThirdServiceException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for {@link ThirdServiceAssert}.
 *
 * @author IO x9x
 * @since 2025-05-16 03:02
 */
class ThirdServiceAssertTest {

	private static final String ERROR_CODE = "10000";

	private static final String EXPECTED_MESSAGE = "HELLO 123";

	private static final Object[] PLACEHOLDERS = { 123 };

	@DisplayName("Test doesNotContain")
	@Test
	void testDoesNotContain() {
		// Test with string that doesn't contain substring (should not throw exception)
		ThirdServiceAssert.doesNotContain("hello world", "foo", ERROR_CODE, PLACEHOLDERS);

		// Test with a string that contains substring (should throw exception)
		ThirdServiceException exception = assertThrows(ThirdServiceException.class,
				() -> ThirdServiceAssert.doesNotContain("hello world", "world", ERROR_CODE, PLACEHOLDERS));
		assertEquals(ERROR_CODE, exception.getErrCode());
		assertEquals(EXPECTED_MESSAGE, exception.getMessage());
	}

	@DisplayName("Test hasLength")
	@Test
	void testHasLength() {
		// Test with non-empty string (should not throw exception)
		ThirdServiceAssert.hasLength("hello", ERROR_CODE, PLACEHOLDERS);

		// Test with empty string (should throw exception)
		ThirdServiceException exception = assertThrows(ThirdServiceException.class, () -> {
			ThirdServiceAssert.hasLength("", ERROR_CODE, PLACEHOLDERS);
		});
		assertEquals(ERROR_CODE, exception.getErrCode());
		assertEquals(EXPECTED_MESSAGE, exception.getMessage());
	}

	@DisplayName("Test hasText")
	@Test
	void testHasText() {
		// Test with string containing non-whitespace (should not throw exception)
		ThirdServiceAssert.hasText("hello", ERROR_CODE, PLACEHOLDERS);

		// Test with whitespace-only string (should throw exception)
		ThirdServiceException exception = assertThrows(ThirdServiceException.class, () -> {
			ThirdServiceAssert.hasText("   ", ERROR_CODE, PLACEHOLDERS);
		});
		assertEquals(ERROR_CODE, exception.getErrCode());
		assertEquals(EXPECTED_MESSAGE, exception.getMessage());
	}

	@DisplayName("Test isInstanceOf")
	@Test
	void testIsInstanceOf() {
		// Test with object that is instance of class (should not throw exception)
		ThirdServiceAssert.isInstanceOf(Object.class, "hello", ERROR_CODE, PLACEHOLDERS);

		// Test with object that is not instance of class (should throw exception)
		ThirdServiceException exception = assertThrows(ThirdServiceException.class, () -> {
			ThirdServiceAssert.isInstanceOf(Integer.class, "hello", ERROR_CODE, PLACEHOLDERS);
		});
		assertEquals(ERROR_CODE, exception.getErrCode());
		assertTrue(exception.getMessage().startsWith(EXPECTED_MESSAGE),
				"Expected message to start with '" + EXPECTED_MESSAGE + "' but was '" + exception.getMessage() + "'");
	}

	@DisplayName("Test isNull")
	@Test
	void testIsNull() {
		// Test with null object (should not throw exception)
		ThirdServiceAssert.isNull(null, ERROR_CODE, PLACEHOLDERS);

		// Test with non-null object (should throw exception)
		ThirdServiceException exception = assertThrows(ThirdServiceException.class, () -> {
			ThirdServiceAssert.isNull("not null", ERROR_CODE, PLACEHOLDERS);
		});
		assertEquals(ERROR_CODE, exception.getErrCode());
		assertEquals(EXPECTED_MESSAGE, exception.getMessage());
	}

	@DisplayName("Test notNull")
	@Test
	void testNotNull() {
		// Test with non-null object (should not throw exception)
		ThirdServiceAssert.notNull("not null", ERROR_CODE, PLACEHOLDERS);

		// Test with null object (should throw exception)
		ThirdServiceException exception = assertThrows(ThirdServiceException.class, () -> {
			ThirdServiceAssert.notNull(null, ERROR_CODE, PLACEHOLDERS);
		});
		assertEquals(ERROR_CODE, exception.getErrCode());
		assertEquals(EXPECTED_MESSAGE, exception.getMessage());
	}

	@DisplayName("Test isTrue")
	@Test
	void testIsTrue() {
		// Test with true expression (should not throw exception)
		ThirdServiceAssert.isTrue(true, ERROR_CODE, PLACEHOLDERS);

		// Test with false expression (should throw exception)
		ThirdServiceException exception = assertThrows(ThirdServiceException.class, () -> {
			ThirdServiceAssert.isTrue(false, ERROR_CODE, PLACEHOLDERS);
		});
		assertEquals(ERROR_CODE, exception.getErrCode());
		assertEquals(EXPECTED_MESSAGE, exception.getMessage());
	}

	@DisplayName("Test noNullElements")
	@Test
	void testNoNullElements() {
		// Test with collection without null elements (should not throw exception)
		ThirdServiceAssert.noNullElements(Arrays.asList("a", "b", "c"), ERROR_CODE, PLACEHOLDERS);

		// Test with collection containing null elements (should throw exception)
		ThirdServiceException exception = assertThrows(ThirdServiceException.class, () -> {
			ThirdServiceAssert.noNullElements(Arrays.asList("a", null, "c"), ERROR_CODE, PLACEHOLDERS);
		});
		assertEquals(ERROR_CODE, exception.getErrCode());
		assertEquals(EXPECTED_MESSAGE, exception.getMessage());
	}

	@DisplayName("Test notEmpty for array")
	@Test
	void testNotEmptyArray() {
		// Test with non-empty array (should not throw exception)
		ThirdServiceAssert.notEmpty(new String[] { "a", "b" }, ERROR_CODE, PLACEHOLDERS);

		// Test with empty array (should throw exception)
		ThirdServiceException exception = assertThrows(ThirdServiceException.class, () -> {
			ThirdServiceAssert.notEmpty(new String[0], ERROR_CODE, PLACEHOLDERS);
		});
		assertEquals(ERROR_CODE, exception.getErrCode());
		assertEquals(EXPECTED_MESSAGE, exception.getMessage());
	}

	@DisplayName("Test notEmpty for Collection")
	@Test
	void testNotEmptyCollection() {
		// Test with non-empty collection (should not throw exception)
		ThirdServiceAssert.notEmpty(Arrays.asList("a", "b"), ERROR_CODE, PLACEHOLDERS);

		// Test with empty collection (should throw exception)
		ThirdServiceException exception = assertThrows(ThirdServiceException.class, () -> {
			ThirdServiceAssert.notEmpty(Collections.emptyList(), ERROR_CODE, PLACEHOLDERS);
		});
		assertEquals(ERROR_CODE, exception.getErrCode());
		assertEquals(EXPECTED_MESSAGE, exception.getMessage());
	}

	@DisplayName("Test notEmpty for Map")
	@Test
	void testNotEmptyMap() {
		// Test with non-empty map (should not throw exception)
		Map<String, String> nonEmptyMap = new HashMap<>();
		nonEmptyMap.put("key", "value");
		ThirdServiceAssert.notEmpty(nonEmptyMap, ERROR_CODE, PLACEHOLDERS);

		// Test with empty map (should throw exception)
		ThirdServiceException exception = assertThrows(ThirdServiceException.class, () -> {
			ThirdServiceAssert.notEmpty(Collections.emptyMap(), ERROR_CODE, PLACEHOLDERS);
		});
		assertEquals(ERROR_CODE, exception.getErrCode());
		assertEquals(EXPECTED_MESSAGE, exception.getMessage());
	}

	@DisplayName("Test isAssignable")
	@Test
	void testIsAssignable() {
		// Test with assignable classes (should not throw exception)
		ThirdServiceAssert.isAssignable(Object.class, String.class, ERROR_CODE, PLACEHOLDERS);

		// Test with non-assignable classes (should throw exception)
		ThirdServiceException exception = assertThrows(ThirdServiceException.class, () -> {
			ThirdServiceAssert.isAssignable(Integer.class, String.class, ERROR_CODE, PLACEHOLDERS);
		});
		assertEquals(ERROR_CODE, exception.getErrCode());
		assertTrue(exception.getMessage().startsWith(EXPECTED_MESSAGE),
				"Expected message to start with '" + EXPECTED_MESSAGE + "' but was '" + exception.getMessage() + "'");
	}

	@DisplayName("Test state")
	@Test
	void testState() {
		// Test with true state (should not throw exception)
		ThirdServiceAssert.state(true, ERROR_CODE, PLACEHOLDERS);

		// Test with false state (should throw exception)
		IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
			ThirdServiceAssert.state(false, ERROR_CODE, PLACEHOLDERS);
		});
		assertEquals(EXPECTED_MESSAGE, exception.getMessage());
	}

}