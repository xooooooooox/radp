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

package space.x9x.radp.types.exception.asserts;

import java.util.Collection;
import java.util.Map;
import java.util.function.BiFunction;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.PropertyKey;

import space.x9x.radp.spring.framework.error.ErrorCode;
import space.x9x.radp.spring.framework.error.ErrorCodeLoader;
import space.x9x.radp.spring.framework.error.ServerException;
import space.x9x.radp.spring.framework.error.asserts.BaseAssert;
import space.x9x.radp.spring.framework.error.util.ExceptionUtils;

/**
 * Server assertion utility class that provides static methods for assertions.
 *
 * @author IO x9x
 * @since 2024-10-24 21:54
 */
public final class ServerAssert extends BaseAssert<ServerException> {

	/**
	 * private constructor to prevent instantiation.
	 */
	private ServerAssert() {

	}

	/**
	 * singleton instance of the ServerAssert class.
	 */
	private static final ServerAssert INSTANCE = new ServerAssert();

	@Override
	protected BiFunction<String, String, ServerException> getExceptionCreator() {
		return ExceptionUtils::serverException;
	}

	@Override
	protected BiFunction<String, String, ServerException> getFormattedMessageExceptionCreator() {
		return ExceptionUtils::serverExceptionWithFormattedMessage;
	}

	/**
	 * Assert that the object is not null, throwing a ServerException with the given
	 * ErrorCode if it is.
	 * @param object the object to check
	 * @param errorCode the error code to use in the exception
	 */
	public static void notNull(Object object, ErrorCode errorCode) {
		try {
			AssertUtils.notNull(object, errorCode);
		}
		catch (IllegalArgumentException ex) {
			throw new ServerException(errorCode);
		}
	}

	/**
	 * asserts that the specified string does not contain the specified substring,
	 * throwing a ServerException with the specified error code if it does.
	 * @param textToSearch the string to search
	 * @param substring the substring to search for
	 * @param errCode the error code to use in the exception
	 * @param placeholders the placeholders to use in the error message
	 * @throws ServerException if the string contains the substring
	 */
	public static void doesNotContain(@NotNull String textToSearch, String substring,
			@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
		INSTANCE.assertDoesNotContain(textToSearch, substring, errCode, placeholders);
	}

	/**
	 * asserts that the specified string has a length greater than 0, throwing a
	 * ServerException with the specified error code if it doesn't.
	 * @param expression the string to check
	 * @param errCode the error code to use in the exception
	 * @param placeholders the placeholders to use in the error message
	 * @throws ServerException if the string has a length of 0
	 */
	public static void hasLength(@NotNull String expression,
			@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
		INSTANCE.assertHasLength(expression, errCode, placeholders);
	}

	/**
	 * asserts that the specified string has text (contains at least one non-whitespace
	 * character), throwing a ServerException with the specified error code if it doesn't.
	 * @param text the string to check
	 * @param errCode the error code to use in the exception
	 * @param placeholders the placeholders to use in the error message
	 * @throws ServerException if the string does not contain the text
	 */
	public static void hasText(String text, @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode,
			Object... placeholders) {
		INSTANCE.assertHasText(text, errCode, placeholders);
	}

	/**
	 * asserts that the specified object is an instance of the specified type, throwing a
	 * ServerException with the specified error code if it isn't.
	 * @param type the type to check against
	 * @param obj the object to check
	 * @param errCode the error code to use in the exception
	 * @param placeholders the placeholders to use in the error message
	 * @throws ServerException if the object is not an instance of the specified type
	 */
	public static void isInstanceOf(Class<?> type, @NotNull Object obj,
			@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
		INSTANCE.assertIsInstanceOf(type, obj, errCode, placeholders);
	}

	/**
	 * asserts that the specified object is null, throwing a ServerException with the
	 * specified error code if it isn't.
	 * @param object the object to check
	 * @param errCode the error code to use in the exception
	 * @param placeholders the placeholders to use in the error message
	 * @throws ServerException if the object is not null
	 */
	public static void isNull(Object object, @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode,
			Object... placeholders) {
		INSTANCE.assertIsNull(object, errCode, placeholders);
	}

	/**
	 * asserts that the specified object is not null, throwing a ServerException with the
	 * specified error code if it is.
	 * @param object the object to check
	 * @param errCode the error code to use in the exception
	 * @param placeholders the placeholders to use in the error message
	 * @throws ServerException if the object is null
	 */
	public static void notNull(Object object, @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode,
			Object... placeholders) {
		INSTANCE.assertNotNull(object, errCode, placeholders);
	}

	/**
	 * asserts that the specified expression is true, throwing a ServerException with the
	 * specified error code if it isn't.
	 * @param expression the boolean expression to check
	 * @param errCode the error code to use in the exception
	 * @param placeholders the placeholders to use in the error message
	 * @throws ServerException if the expression is false
	 */
	public static void isTrue(boolean expression,
			@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
		INSTANCE.assertIsTrue(expression, errCode, placeholders);
	}

	/**
	 * asserts that the specified collection contains no null elements, throwing a
	 * ServerException with the specified error code if it does.
	 * @param collection the collection to check
	 * @param errCode the error code to use in the exception
	 * @param placeholders the placeholders to use in the error message
	 * @throws ServerException if the collection contains a null element
	 */
	public static void noNullElements(@NotNull Collection<?> collection,
			@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
		INSTANCE.assertNoNullElements(collection, errCode, placeholders);
	}

	/**
	 * asserts that the specified array is not empty, throwing a ServerException with the
	 * specified error code if it is.
	 * @param array the array to check
	 * @param errCode the error code to use in the exception
	 * @param placeholders the placeholders to use in the error message
	 * @throws ServerException if the array is empty
	 */
	public static void notEmpty(Object[] array,
			@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
		INSTANCE.assertNotEmpty(array, errCode, placeholders);
	}

	/**
	 * asserts that the specified collection is not empty, throwing a ServerException with
	 * the specified error code if it is.
	 * @param collection the collection to check
	 * @param errCode the error code to use in the exception
	 * @param placeholders the placeholders to use in the error message
	 * @throws ServerException if the collection is empty
	 */
	public static void notEmpty(@NotNull Collection<?> collection,
			@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
		INSTANCE.assertNotEmpty(collection, errCode, placeholders);
	}

	/**
	 * asserts that the specified map is not empty, throwing a ServerException with the
	 * specified error code if it is.
	 * @param map the map to check
	 * @param errCode the error code to use in the exception
	 * @param placeholders the placeholders to use in the error message
	 * @throws ServerException if the map is empty
	 */
	public static void notEmpty(@NotNull Map<?, ?> map,
			@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
		INSTANCE.assertNotEmpty(map, errCode, placeholders);
	}

	/**
	 * asserts that the specified subType is assignable to the specified superType,
	 * throwing a ServerException with the specified error code if it isn't.
	 * @param superType the super type to check against
	 * @param subType the subtype to check
	 * @param errCode the error code to use in the exception
	 * @param placeholders the placeholders to use in the error message
	 * @throws ServerException if the subType is not assignable to the superType
	 */
	public static void isAssignable(Class<?> superType, @NotNull Class<?> subType,
			@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
		INSTANCE.assertIsAssignable(superType, subType, errCode, placeholders);
	}

	/**
	 * asserts that the specified expression is true, representing a valid state, throwing
	 * a ServerException with the specified error code if it isn't.
	 * @param expression the boolean expression to check
	 * @param errCode the error code to use in the exception
	 * @param placeholders the placeholders to use in the error message
	 * @throws ServerException if the expression is false
	 */
	public static void state(boolean expression,
			@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
		INSTANCE.assertState(expression, errCode, placeholders);
	}

}
