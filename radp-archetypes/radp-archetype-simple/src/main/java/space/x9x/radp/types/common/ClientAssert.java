package space.x9x.radp.types.common;

import space.x9x.radp.spring.framework.error.ClientException;
import space.x9x.radp.spring.framework.error.asserts.BaseAssert;
import space.x9x.radp.spring.framework.error.util.ExceptionUtils;
import lombok.NonNull;
import org.jetbrains.annotations.PropertyKey;
import space.x9x.radp.spring.framework.error.ErrorCodeLoader;

import java.util.Collection;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * Client assertion utility that throws ClientException when assertions fail.
 *
 * @author x9x
 * @since 2024-10-24 23:46
 */
public class ClientAssert extends BaseAssert<ClientException> {

    private static final ClientAssert INSTANCE = new ClientAssert();

    /**
     * Get the singleton instance of ClientAssert.
     *
     * @return the singleton instance
     */
    public static ClientAssert getInstance() {
        return INSTANCE;
    }

    @Override
    protected BiFunction<String, String, ClientException> getExceptionCreator() {
        return ExceptionUtils::clientException;
    }

    /**
     * Static method to get an instance of ClientAssert for use in static contexts.
     */
    public static ClientAssert assertThat() {
        return INSTANCE;
    }

    /**
     * Assert that the given string does not contain the given substring.
     */
    public void assertDoesNotContain(@NonNull String textToSearch, String substring,
                              @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode,
                              Object... placeholders) {
        super.assertDoesNotContain(textToSearch, substring, errCode, placeholders);
    }

    /**
     * Assert that the given string has length.
     */
    public void assertHasLength(@NonNull String expression,
                         @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
        super.assertHasLength(expression, errCode, placeholders);
    }

    /**
     * Assert that the given string has text.
     */
    public void assertHasText(String text,
                       @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
        super.assertHasText(text, errCode, placeholders);
    }

    /**
     * Assert that the given object is an instance of the given type.
     */
    public void assertIsInstanceOf(Class<?> type, @NonNull Object obj,
                            @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
        super.assertIsInstanceOf(type, obj, errCode, placeholders);
    }

    /**
     * Assert that the given object is null.
     */
    public void assertIsNull(Object object,
                      @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
        super.assertIsNull(object, errCode, placeholders);
    }

    /**
     * Assert that the given object is not null.
     */
    public void assertNotNull(Object object,
                       @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
        super.assertNotNull(object, errCode, placeholders);
    }

    /**
     * Assert that the given expression is true.
     */
    public void assertIsTrue(boolean expression,
                      @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
        super.assertIsTrue(expression, errCode, placeholders);
    }

    /**
     * Assert that the given collection has no null elements.
     */
    public void assertNoNullElements(@NonNull Collection<?> collection,
                              @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
        super.assertNoNullElements(collection, errCode, placeholders);
    }

    /**
     * Assert that the given array is not empty.
     */
    public void assertNotEmpty(@NonNull Object[] array,
                        @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
        super.assertNotEmpty(array, errCode, placeholders);
    }

    /**
     * Assert that the given collection is not empty.
     */
    public void assertNotEmpty(@NonNull Collection<?> collection,
                        @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
        super.assertNotEmpty(collection, errCode, placeholders);
    }

    /**
     * Assert that the given map is not empty.
     */
    public void assertNotEmpty(@NonNull Map<?, ?> map,
                        @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
        super.assertNotEmpty(map, errCode, placeholders);
    }

    /**
     * Assert that the given subType is assignable to the given superType.
     */
    public void assertIsAssignable(Class<?> superType, @NonNull Class<?> subType,
                            @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
        super.assertIsAssignable(superType, subType, errCode, placeholders);
    }

    /**
     * Assert that the given expression is true.
     */
    public void assertState(boolean expression,
                     @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
        super.assertState(expression, errCode, placeholders);
    }
}
