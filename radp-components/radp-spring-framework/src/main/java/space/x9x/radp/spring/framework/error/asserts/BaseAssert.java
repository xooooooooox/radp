package space.x9x.radp.spring.framework.error.asserts;

import lombok.NonNull;
import org.jetbrains.annotations.PropertyKey;
import space.x9x.radp.spring.framework.error.ErrorCodeLoader;

import java.util.Collection;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * Base an abstract class for all assertion classes with common assertion logic.
 * This class extends AbstractAssert and adds exception handling functionality.
 *
 * @param <E> the type of exception to be thrown
 * @author x9x
 * @since 2024-09-26 23:47
 */
public abstract class BaseAssert<E extends RuntimeException> extends AbstractAssert {

    /**
     * Get the exception creator function that creates the specific exception type.
     *
     * @return the exception creator function
     */
    protected abstract BiFunction<String, String, E> getExceptionCreator();

    /**
     * Get the exception creator function that creates the specific exception type with a pre-formatted message.
     *
     * @return the exception creator function
     */
    protected BiFunction<String, String, E> getFormattedMessageExceptionCreator() {
        // Default implementation returns the same as getExceptionCreator
        // Subclasses can override this to provide a different implementation
        return getExceptionCreator();
    }

    /**
     * Create an exception to the specific type.
     *
     * @param errCode the error code
     * @param message the error message
     * @return the created exception
     */
    protected E createException(String errCode, String message) {
        // Use the formatted message exception creator to avoid double formatting
        return getFormattedMessageExceptionCreator().apply(errCode, message);
    }

    /**
     * Assert that the given string does not contain the given substring.
     *
     * @param textToSearch the string to search
     * @param substring the substring to find
     * @param errCode the error code to use if the assertion fails
     * @param placeholders the placeholders to be used in the error message
     */
    public void assertDoesNotContain(@NonNull String textToSearch, String substring,
                              @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode,
                              Object... placeholders) {
        try {
            doesNotContain(textToSearch, substring, errCode, placeholders);
        } catch (IllegalArgumentException e) {
            throw createException(errCode, e.getMessage());
        }
    }

    /**
     * Assert that the given string has length.
     *
     * @param expression the string to check
     * @param errCode the error code to use if the assertion fails
     * @param placeholders the placeholders to be used in the error message
     */
    public void assertHasLength(@NonNull String expression,
                         @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
        try {
            hasLength(expression, errCode, placeholders);
        } catch (IllegalArgumentException e) {
            throw createException(errCode, e.getMessage());
        }
    }

    /**
     * Assert that the given string has text.
     *
     * @param text the string to check
     * @param errCode the error code to use if the assertion fails
     * @param placeholders the placeholders to be used in the error message
     */
    public void assertHasText(String text,
                       @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
        try {
            hasText(text, errCode, placeholders);
        } catch (IllegalArgumentException e) {
            throw createException(errCode, e.getMessage());
        }
    }

    /**
     * Assert that the given object is an instance of the given type.
     *
     * @param type the type to check against
     * @param obj the object to check
     * @param errCode the error code to use if the assertion fails
     * @param placeholders the placeholders to be used in the error message
     */
    public void assertIsInstanceOf(Class<?> type, @NonNull Object obj,
                            @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
        try {
            isInstanceOf(type, obj, errCode, placeholders);
        } catch (IllegalArgumentException e) {
            throw createException(errCode, e.getMessage());
        }
    }

    /**
     * Assert that the given object is null.
     *
     * @param object the object to check
     * @param errCode the error code to use if the assertion fails
     * @param placeholders the placeholders to be used in the error message
     */
    public void assertIsNull(Object object,
                      @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
        try {
            isNull(object, errCode, placeholders);
        } catch (IllegalArgumentException e) {
            throw createException(errCode, e.getMessage());
        }
    }

    /**
     * Assert that the given object is not null.
     *
     * @param object the object to check
     * @param errCode the error code to use if the assertion fails
     * @param placeholders the placeholders to be used in the error message
     */
    public void assertNotNull(Object object,
                       @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
        try {
            notNull(object, errCode, placeholders);
        } catch (IllegalArgumentException e) {
            throw createException(errCode, e.getMessage());
        }
    }

    /**
     * Assert that the given expression is true.
     *
     * @param expression the boolean expression to check
     * @param errCode the error code to use if the assertion fails
     * @param placeholders the placeholders to be used in the error message
     */
    public void assertIsTrue(boolean expression,
                      @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
        try {
            isTrue(expression, errCode, placeholders);
        } catch (IllegalArgumentException e) {
            throw createException(errCode, e.getMessage());
        }
    }

    /**
     * Assert that the given collection has no null elements.
     *
     * @param collection the collection to check
     * @param errCode the error code to use if the assertion fails
     * @param placeholders the placeholders to be used in the error message
     */
    public void assertNoNullElements(@NonNull Collection<?> collection,
                              @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
        try {
            noNullElements(collection, errCode, placeholders);
        } catch (IllegalArgumentException e) {
            throw createException(errCode, e.getMessage());
        }
    }

    /**
     * Assert that the given array is not empty.
     *
     * @param array the array to check
     * @param errCode the error code to use if the assertion fails
     * @param placeholders the placeholders to be used in the error message
     */
    public void assertNotEmpty(@NonNull Object[] array,
                        @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
        try {
            notEmpty(array, errCode, placeholders);
        } catch (IllegalArgumentException e) {
            throw createException(errCode, e.getMessage());
        }
    }

    /**
     * Assert that the given collection is not empty.
     *
     * @param collection the collection to check
     * @param errCode the error code to use if the assertion fails
     * @param placeholders the placeholders to be used in the error message
     */
    public void assertNotEmpty(@NonNull Collection<?> collection,
                        @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
        try {
            notEmpty(collection, errCode, placeholders);
        } catch (IllegalArgumentException e) {
            throw createException(errCode, e.getMessage());
        }
    }

    /**
     * Assert that the given map is not empty.
     *
     * @param map the map to check
     * @param errCode the error code to use if the assertion fails
     * @param placeholders the placeholder values for the error message
     */
    public void assertNotEmpty(@NonNull Map<?, ?> map,
                        @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
        try {
            notEmpty(map, errCode, placeholders);
        } catch (IllegalArgumentException e) {
            throw createException(errCode, e.getMessage());
        }
    }

    /**
     * Assert that the given subType is assignable to the given superType.
     *
     * @param superType the super type to check against
     * @param subType the sub type to check
     * @param errCode the error code to use if the assertion fails
     * @param placeholders the placeholder values for the error message
     */
    public void assertIsAssignable(Class<?> superType, @NonNull Class<?> subType,
                            @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
        try {
            isAssignable(superType, subType, errCode, placeholders);
        } catch (IllegalArgumentException e) {
            throw createException(errCode, e.getMessage());
        }
    }

    /**
     * Assert that the given expression is true.
     *
     * @param expression the boolean expression to check
     * @param errCode the error code to use if the assertion fails
     * @param placeholders the placeholder values for the error message
     */
    public void assertState(boolean expression,
                     @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
        try {
            state(expression, errCode, placeholders);
        } catch (IllegalArgumentException e) {
            throw createException(errCode, e.getMessage());
        }
    }
}
