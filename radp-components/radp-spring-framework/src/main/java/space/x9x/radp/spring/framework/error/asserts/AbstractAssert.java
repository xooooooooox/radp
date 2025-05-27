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

package space.x9x.radp.spring.framework.error.asserts;

import lombok.NonNull;
import org.jetbrains.annotations.PropertyKey;
import org.springframework.util.Assert;
import space.x9x.radp.spring.framework.error.ErrorCodeLoader;

import java.util.Collection;
import java.util.Map;

/**
 * Abstract base class for all assertion classes with common static assertion methods.
 * This class extends Spring's Assert and adds error code handling.
 *
 * @author IO x9x
 * @since 2024-09-27 12:13
 */
public abstract class AbstractAssert extends Assert {

	/**
	 * Helper method to get error message from error code.
	 * @param errCode the error code
	 * @param placeholders the placeholders for the error message
	 * @return the error message
	 */
	protected static String getErrorMessage(String errCode, Object... placeholders) {
		return ErrorCodeLoader.getErrMessage(errCode, placeholders);
	}

	/**
	 * Assert that the given string does not contain the given substring.
	 * @param textToSearch the string to search in
	 * @param substring the substring to search for
	 * @param errCode the error code to use if the assertion fails
	 * @param placeholders the placeholder values to be substituted in the error message
	 */
	public static void doesNotContain(@NonNull String textToSearch, String substring,
			@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
		doesNotContain(textToSearch, substring, getErrorMessage(errCode, placeholders));
	}

	/**
	 * Assert that the given string has length.
	 * @param expression the string to check
	 * @param errCode the error code to use if the assertion fails
	 * @param placeholders the placeholder values to be substituted in the error message
	 */
	public static void hasLength(@NonNull String expression,
			@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
		hasLength(expression, getErrorMessage(errCode, placeholders));
	}

	/**
	 * Assert that the given string has text.
	 * @param text the string to check
	 * @param errCode the error code to use if the assertion fails
	 * @param placeholders the placeholder values to be substituted in the error message
	 */
	public static void hasText(String text, @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode,
			Object... placeholders) {
		hasText(text, getErrorMessage(errCode, placeholders));
	}

	/**
	 * Assert that the given object is an instance of the given type.
	 * @param type the type to check against
	 * @param obj the object to check
	 * @param errCode the error code to use if the assertion fails
	 * @param placeholders the placeholder values to be substituted in the error message
	 */
	public static void isInstanceOf(Class<?> type, @NonNull Object obj,
			@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
		isInstanceOf(type, obj, getErrorMessage(errCode, placeholders));
	}

	/**
	 * Assert that the given object is null.
	 * @param object the object to check
	 * @param errCode the error code to use if the assertion fails
	 * @param placeholders the placeholder values to be substituted in the error message
	 */
	public static void isNull(Object object, @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode,
			Object... placeholders) {
		isNull(object, getErrorMessage(errCode, placeholders));
	}

	/**
	 * Assert that the given object is not null.
	 * @param object the object to check
	 * @param errCode the error code to use if the assertion fails
	 * @param placeholders the placeholder values to be substituted in the error message
	 */
	public static void notNull(Object object, @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode,
			Object... placeholders) {
		notNull(object, getErrorMessage(errCode, placeholders));
	}

	/**
	 * Assert that the given expression is true.
	 * @param expression the boolean expression to check
	 * @param errCode the error code to use if the assertion fails
	 * @param placeholders the placeholder values to be substituted in the error message
	 */
	public static void isTrue(boolean expression,
			@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
		isTrue(expression, getErrorMessage(errCode, placeholders));
	}

	/**
	 * Assert that the given collection has no null elements.
	 * @param collection the collection to check
	 * @param errCode the error code to use if the assertion fails
	 * @param placeholders the placeholder values to be substituted in the error message
	 */
	public static void noNullElements(@NonNull Collection<?> collection,
			@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
		noNullElements(collection, getErrorMessage(errCode, placeholders));
	}

	/**
	 * Assert that the given array is not empty.
	 * @param array the array to check
	 * @param errCode the error code to use if the assertion fails
	 * @param placeholders the placeholder values to be substituted in the error message
	 */
	public static void notEmpty(@NonNull Object[] array,
			@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
		notEmpty(array, getErrorMessage(errCode, placeholders));
	}

	/**
	 * Assert that the given collection is not empty.
	 * @param collection the collection to check
	 * @param errCode the error code to use if the assertion fails
	 * @param placeholders the placeholder values to be substituted in the error message
	 */
	public static void notEmpty(@NonNull Collection<?> collection,
			@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
		notEmpty(collection, getErrorMessage(errCode, placeholders));
	}

	/**
	 * Assert that the given map is not empty.
	 * @param map the map to check
	 * @param errCode the error code to use if the assertion fails
	 * @param placeholders the placeholder values to be substituted in the error message
	 */
	public static void notEmpty(@NonNull Map<?, ?> map,
			@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
		notEmpty(map, getErrorMessage(errCode, placeholders));
	}

	/**
	 * Assert that the given subType is assignable to the given superType.
	 * @param superType the super type to check against
	 * @param subType the sub type to check
	 * @param errCode the error code to use if the assertion fails
	 * @param placeholders the placeholder values to be substituted in the error message
	 */
	public static void isAssignable(Class<?> superType, @NonNull Class<?> subType,
			@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
		isAssignable(superType, subType, getErrorMessage(errCode, placeholders));
	}

	/**
	 * Assert that the given expression is true.
	 * @param expression the boolean expression to check
	 * @param errCode the error code to use if the assertion fails
	 * @param placeholders the placeholder values to be substituted in the error message
	 */
	public static void state(boolean expression,
			@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
		state(expression, getErrorMessage(errCode, placeholders));
	}

}
