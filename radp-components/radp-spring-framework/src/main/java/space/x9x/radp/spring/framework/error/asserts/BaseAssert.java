package space.x9x.radp.spring.framework.error.asserts;

import lombok.NonNull;
import org.jetbrains.annotations.PropertyKey;
import space.x9x.radp.spring.framework.error.ErrorCodeLoader;

import java.util.Collection;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * Base interface for all asserted interfaces with common assertion logic.
 *
 * @param <E> the type of exception to be thrown
 * @author x9x
 * @since 2024-09-26 23:47
 */
public interface BaseAssert<E extends RuntimeException> {

    /**
     * Get the exception creator function that creates the specific exception type.
     *
     * @return the exception creator function
     */
    BiFunction<String, String, E> getExceptionCreator();

    /**
     * Create an exception of the specific type.
     *
     * @param errCode the error code
     * @param message the error message
     * @return the created exception
     */
    default E createException(String errCode, String message) {
        return getExceptionCreator().apply(errCode, message);
    }

    default void doesNotContain(@NonNull String textToSearch, String substring,
                                @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode,
                                Object... placeholders) {
        try {
            ExtendedAssert.doesNotContain(textToSearch, substring, errCode, placeholders);
        } catch (IllegalArgumentException e) {
            throw createException(errCode, e.getMessage());
        }
    }

    default void hasLength(@NonNull String expression,
                           @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
        try {
            ExtendedAssert.hasLength(expression, errCode, placeholders);
        } catch (IllegalArgumentException e) {
            throw createException(errCode, e.getMessage());
        }
    }

    default void hasText(String text,
                         @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
        try {
            ExtendedAssert.hasText(text, errCode, placeholders);
        } catch (IllegalArgumentException e) {
            throw createException(errCode, e.getMessage());
        }
    }

    default void isInstanceOf(Class<?> type, @NonNull Object obj,
                              @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
        try {
            ExtendedAssert.isInstanceOf(type, obj, errCode, placeholders);
        } catch (IllegalArgumentException e) {
            throw createException(errCode, e.getMessage());
        }
    }

    default void isNull(Object object,
                        @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
        try {
            ExtendedAssert.isNull(object, errCode, placeholders);
        } catch (IllegalArgumentException e) {
            throw createException(errCode, e.getMessage());
        }
    }

    default void notNull(Object object,
                         @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
        try {
            ExtendedAssert.notNull(object, errCode, placeholders);
        } catch (IllegalArgumentException e) {
            throw createException(errCode, e.getMessage());
        }
    }

    default void isTrue(boolean expression,
                        @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
        try {
            ExtendedAssert.isTrue(expression, errCode, placeholders);
        } catch (IllegalArgumentException e) {
            throw createException(errCode, e.getMessage());
        }
    }

    default void noNullElements(@NonNull Collection<?> collection,
                                @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
        try {
            ExtendedAssert.noNullElements(collection, errCode, placeholders);
        } catch (IllegalArgumentException e) {
            throw createException(errCode, e.getMessage());
        }
    }

    default void notEmpty(@NonNull Object[] array,
                          @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
        try {
            ExtendedAssert.notEmpty(array, errCode, placeholders);
        } catch (IllegalArgumentException e) {
            throw createException(errCode, e.getMessage());
        }
    }

    default void notEmpty(@NonNull Collection<?> collection,
                          @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
        try {
            ExtendedAssert.notEmpty(collection, errCode, placeholders);
        } catch (IllegalArgumentException e) {
            throw createException(errCode, e.getMessage());
        }
    }

    default void notEmpty(@NonNull Map<?, ?> map,
                          @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
        try {
            ExtendedAssert.notEmpty(map, errCode, placeholders);
        } catch (IllegalArgumentException e) {
            throw createException(errCode, e.getMessage());
        }
    }

    default void isAssignable(Class<?> superType, @NonNull Class<?> subType,
                              @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
        try {
            ExtendedAssert.isAssignable(superType, subType, errCode, placeholders);
        } catch (IllegalArgumentException e) {
            throw createException(errCode, e.getMessage());
        }
    }

    default void state(boolean expression,
                       @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
        try {
            ExtendedAssert.state(expression, errCode, placeholders);
        } catch (IllegalArgumentException e) {
            throw createException(errCode, e.getMessage());
        }
    }
}