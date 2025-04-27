#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.types.common;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.PropertyKey;
import space.x9x.radp.spring.framework.error.ErrorCodeLoader;
import space.x9x.radp.spring.framework.error.asserts.BaseThirdServiceAssert;

import java.util.Collection;
import java.util.Map;

/**
 * Third-party service assertion utility class that provides static methods for assertions.
 *
 * @author x9x
 * @since 2024-10-24 23:46
 */
@UtilityClass
public class ThirdServiceAssert {

    private static final BaseThirdServiceAssert INSTANCE = new BaseThirdServiceAssert() {
    };

    public static void doesNotContain(String textToSearch, String substring,
                                      @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode,
                                      Object... placeholders) {
        INSTANCE.doesNotContain(textToSearch, substring, errCode, placeholders);
    }

    public static void hasLength(String expression,
                                 @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
        INSTANCE.hasLength(expression, errCode, placeholders);
    }

    public static void hasText(String text,
                               @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
        INSTANCE.hasText(text, errCode, placeholders);
    }

    public static void isInstanceOf(Class<?> type, Object obj,
                                    @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
        INSTANCE.isInstanceOf(type, obj, errCode, placeholders);
    }

    public static void isNull(Object object,
                              @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
        INSTANCE.isNull(object, errCode, placeholders);
    }

    public static void notNull(Object object,
                               @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
        INSTANCE.notNull(object, errCode, placeholders);
    }

    public static void isTrue(boolean expression,
                              @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
        INSTANCE.isTrue(expression, errCode, placeholders);
    }

    public static void noNullElements(Collection<?> collection,
                                      @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
        INSTANCE.noNullElements(collection, errCode, placeholders);
    }

    public static void notEmpty(Object[] array,
                                @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
        INSTANCE.notEmpty(array, errCode, placeholders);
    }

    public static void notEmpty(Collection<?> collection,
                                @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
        INSTANCE.notEmpty(collection, errCode, placeholders);
    }

    public static void notEmpty(Map<?, ?> map,
                                @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
        INSTANCE.notEmpty(map, errCode, placeholders);
    }

    public static void isAssignable(Class<?> superType, Class<?> subType,
                                    @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
        INSTANCE.isAssignable(superType, subType, errCode, placeholders);
    }

    public static void state(boolean expression,
                             @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... placeholders) {
        INSTANCE.state(expression, errCode, placeholders);
    }
}
