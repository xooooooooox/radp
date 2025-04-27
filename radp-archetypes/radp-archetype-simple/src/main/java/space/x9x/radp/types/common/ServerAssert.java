package space.x9x.radp.types.common;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.PropertyKey;
import space.x9x.radp.spring.framework.error.ErrorCode;
import space.x9x.radp.spring.framework.error.ErrorCodeLoader;
import space.x9x.radp.spring.framework.error.ServerException;
import space.x9x.radp.spring.framework.error.asserts.BaseAssert;
import space.x9x.radp.spring.framework.error.util.ExceptionUtils;

import java.util.Collection;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * Server assertion utility class that provides static methods for assertions.
 *
 * @author x9x
 * @since 2024-10-24 21:54
 */
public class ServerAssert extends BaseAssert<ServerException> {

    private static final ServerAssert INSTANCE = new ServerAssert();

    /**
     * Get the singleton instance of ServerAssert.
     *
     * @return the singleton instance
     */
    public static ServerAssert getInstance() {
        return INSTANCE;
    }

    @Override
    protected BiFunction<String, String, ServerException> getExceptionCreator() {
        return ExceptionUtils::serverException;
    }

    /**
     * Assert that the object is not null, throwing a ServerException with the given ErrorCode if it is.
     *
     * @param object    the object to check
     * @param errorCode the error code to use in the exception
     */
    public static void notNull(Object object, ErrorCode errorCode) {
        try {
            AssertUtils.notNull(object, errorCode);
        } catch (IllegalArgumentException e) {
            throw new ServerException(errorCode);
        }
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
