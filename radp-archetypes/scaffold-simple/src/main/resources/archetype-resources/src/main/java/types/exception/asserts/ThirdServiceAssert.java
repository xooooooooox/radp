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

#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.types.exception.asserts;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.PropertyKey;
import space.x9x.radp.spring.framework.error.ErrorCodeLoader;
import space.x9x.radp.spring.framework.error.ThirdServiceException;
import space.x9x.radp.spring.framework.error.asserts.BaseAssert;
import space.x9x.radp.spring.framework.error.util.ExceptionUtils;

import java.util.Collection;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * Third-party service assertion utility class that provides static methods for assertions.
 *
 * @author x9x
 * @since 2024-10-24 23:46
 */
public final class ThirdServiceAssert extends BaseAssert<ThirdServiceException> {

    private ThirdServiceAssert() {
    }

    private static final ThirdServiceAssert INSTANCE = new ThirdServiceAssert();


    @Override
    protected BiFunction<String, String, ThirdServiceException> getExceptionCreator() {
        return ExceptionUtils::thirdServiceException;
    }

    @Override
    protected BiFunction<String, String, ThirdServiceException> getFormattedMessageExceptionCreator() {
        return ExceptionUtils::thirdServiceExceptionWithFormattedMessage;
    }

    public static void doesNotContain(@NotNull String textToSearch, String substring,
                                      @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode,
                                      Object... placeholders) {
        INSTANCE.assertDoesNotContain(textToSearch, substring, errCode, placeholders);
    }

    public static void hasLength(@NotNull String expression,
                                 @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
        INSTANCE.assertHasLength(expression, errCode, placeholders);
    }

    public static void hasText(String text,
                               @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
        INSTANCE.assertHasText(text, errCode, placeholders);
    }

    public static void isInstanceOf(Class<?> type, @NotNull Object obj,
                                    @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
        INSTANCE.assertIsInstanceOf(type, obj, errCode, placeholders);
    }

    public static void isNull(Object object,
                              @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
        INSTANCE.assertIsNull(object, errCode, placeholders);
    }

    public static void notNull(Object object,
                               @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
        INSTANCE.assertNotNull(object, errCode, placeholders);
    }

    public static void isTrue(boolean expression,
                              @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
        INSTANCE.assertIsTrue(expression, errCode, placeholders);
    }

    public static void noNullElements(@NotNull Collection<?> collection,
                                      @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
        INSTANCE.assertNoNullElements(collection, errCode, placeholders);
    }

    public static void notEmpty(Object[] array,
                                @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
        INSTANCE.assertNotEmpty(array, errCode, placeholders);
    }

    public static void notEmpty(@NotNull Collection<?> collection,
                                @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
        INSTANCE.assertNotEmpty(collection, errCode, placeholders);
    }

    public static void notEmpty(@NotNull Map<?, ?> map,
                                @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
        INSTANCE.assertNotEmpty(map, errCode, placeholders);
    }

    public static void isAssignable(Class<?> superType, @NotNull Class<?> subType,
                                    @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
        INSTANCE.assertIsAssignable(superType, subType, errCode, placeholders);
    }

    public static void state(boolean expression,
                             @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
        INSTANCE.assertState(expression, errCode, placeholders);
    }
}
