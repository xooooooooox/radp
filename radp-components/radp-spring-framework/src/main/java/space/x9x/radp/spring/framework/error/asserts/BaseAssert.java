package space.x9x.radp.spring.framework.error.asserts;

import lombok.NonNull;
import org.jetbrains.annotations.PropertyKey;
import space.x9x.radp.spring.framework.error.ErrorCodeLoader;

import java.util.Collection;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * Base an abstract class for all assertion classes with common assertion logic.
 *
 * @param <E> the type of exception to be thrown
 * @author x9x
 * @since 2024-09-26 23:47
 */
public abstract class BaseAssert<E extends RuntimeException> extends ExtendedAssert {

    /**
     * Get the exception creator function that creates the specific exception type.
     *
     * @return the exception creator function
     */
    protected abstract BiFunction<String, String, E> getExceptionCreator();

    /**
     * Create an exception of the specific type.
     *
     * @param errCode the error code
     * @param message the error message
     * @return the created exception
     */
    protected E createException(String errCode, String message) {
        return getExceptionCreator().apply(errCode, message);
    }

    /**
     * Assert that the given string does not contain the given substring.
     */
    public void assertDoesNotContain(@NonNull String textToSearch, String substring,
                              @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode,
                              Object... placeholders) {
        try {
            ExtendedAssert.doesNotContain(textToSearch, substring, errCode, placeholders);
        } catch (IllegalArgumentException e) {
            throw createException(errCode, e.getMessage());
        }
    }

    /**
     * Assert that the given string has length.
     */
    public void assertHasLength(@NonNull String expression,
                         @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
        try {
            ExtendedAssert.hasLength(expression, errCode, placeholders);
        } catch (IllegalArgumentException e) {
            throw createException(errCode, e.getMessage());
        }
    }

    /**
     * Assert that the given string has text.
     */
    public void assertHasText(String text,
                       @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
        try {
            ExtendedAssert.hasText(text, errCode, placeholders);
        } catch (IllegalArgumentException e) {
            throw createException(errCode, e.getMessage());
        }
    }

    /**
     * Assert that the given object is an instance of the given type.
     */
    public void assertIsInstanceOf(Class<?> type, @NonNull Object obj,
                            @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
        try {
            ExtendedAssert.isInstanceOf(type, obj, errCode, placeholders);
        } catch (IllegalArgumentException e) {
            throw createException(errCode, e.getMessage());
        }
    }

    /**
     * Assert that the given object is null.
     */
    public void assertIsNull(Object object,
                      @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
        try {
            ExtendedAssert.isNull(object, errCode, placeholders);
        } catch (IllegalArgumentException e) {
            throw createException(errCode, e.getMessage());
        }
    }

    /**
     * Assert that the given object is not null.
     */
    public void assertNotNull(Object object,
                       @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
        try {
            ExtendedAssert.notNull(object, errCode, placeholders);
        } catch (IllegalArgumentException e) {
            throw createException(errCode, e.getMessage());
        }
    }

    /**
     * Assert that the given expression is true.
     */
    public void assertIsTrue(boolean expression,
                      @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
        try {
            ExtendedAssert.isTrue(expression, errCode, placeholders);
        } catch (IllegalArgumentException e) {
            throw createException(errCode, e.getMessage());
        }
    }

    /**
     * Assert that the given collection has no null elements.
     */
    public void assertNoNullElements(@NonNull Collection<?> collection,
                              @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
        try {
            ExtendedAssert.noNullElements(collection, errCode, placeholders);
        } catch (IllegalArgumentException e) {
            throw createException(errCode, e.getMessage());
        }
    }

    /**
     * Assert that the given array is not empty.
     */
    public void assertNotEmpty(@NonNull Object[] array,
                        @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
        try {
            ExtendedAssert.notEmpty(array, errCode, placeholders);
        } catch (IllegalArgumentException e) {
            throw createException(errCode, e.getMessage());
        }
    }

    /**
     * Assert that the given collection is not empty.
     */
    public void assertNotEmpty(@NonNull Collection<?> collection,
                        @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
        try {
            ExtendedAssert.notEmpty(collection, errCode, placeholders);
        } catch (IllegalArgumentException e) {
            throw createException(errCode, e.getMessage());
        }
    }

    /**
     * Assert that the given map is not empty.
     */
    public void assertNotEmpty(@NonNull Map<?, ?> map,
                        @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
        try {
            ExtendedAssert.notEmpty(map, errCode, placeholders);
        } catch (IllegalArgumentException e) {
            throw createException(errCode, e.getMessage());
        }
    }

    /**
     * Assert that the given subType is assignable to the given superType.
     */
    public void assertIsAssignable(Class<?> superType, @NonNull Class<?> subType,
                            @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
        try {
            ExtendedAssert.isAssignable(superType, subType, errCode, placeholders);
        } catch (IllegalArgumentException e) {
            throw createException(errCode, e.getMessage());
        }
    }

    /**
     * Assert that the given expression is true.
     */
    public void assertState(boolean expression,
                     @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
        try {
            ExtendedAssert.state(expression, errCode, placeholders);
        } catch (IllegalArgumentException e) {
            throw createException(errCode, e.getMessage());
        }
    }
}
