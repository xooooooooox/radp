package space.x9x.radp.spring.framework.error.asserts;

import space.x9x.radp.spring.framework.error.ErrorCodeLoader;
import lombok.NonNull;
import org.jetbrains.annotations.PropertyKey;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.Map;

/**
 * 与
 * {@link BaseClientAssert}, <br/>
 * {@link BaseServerAssert}, <br/>
 * {@link BaseThirdServiceAssert}, <br/>
 * 不同的是,
 * {@link ExtendedAssert} 主要用于框架内, 继承了 {@link Assert} 会抛出 {@link IllegalArgumentException}, 而前者主要为了前后端联调统一错误码和返回值,
 * 之所以这里额外封装一个 AssertUtils 是为了使用相同的错误码定义方式.
 *
 * @author x9x
 * @since 2024-09-27 12:13
 */
public abstract class ExtendedAssert extends Assert {

    public static void doesNotContain(@NonNull String textToSearch, String substring, @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode,
                                      Object... placeholders) {
        doesNotContain(textToSearch, substring, ErrorCodeLoader.getErrMessage(errCode, placeholders));
    }

    public static void hasLength(@NonNull String expression, @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode,
                                 Object... placeholders) {
        hasLength(expression, ErrorCodeLoader.getErrMessage(errCode, placeholders));
    }

    public static void hasText(String text, @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode,
                               Object... placeholders) {
        hasText(text, ErrorCodeLoader.getErrMessage(errCode, placeholders));
    }

    public static void isInstanceOf(Class<?> type, @NonNull Object obj, @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode,
                                    Object... placeholders) {
        isInstanceOf(type, obj, ErrorCodeLoader.getErrMessage(errCode, placeholders));
    }

    public static void isNull(Object object, @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode,
                              Object... placeholders) {
        isNull(object, ErrorCodeLoader.getErrMessage(errCode, placeholders));
    }

    public static void notNull(Object object, @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode,
                               Object... placeholders) {
        notNull(object, ErrorCodeLoader.getErrMessage(errCode, placeholders));
    }

    public static void isTrue(boolean expression, @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode,
                              Object... placeholders) {
        isTrue(expression, ErrorCodeLoader.getErrMessage(errCode, placeholders));
    }

    public static void noNullElements(@NonNull Collection<?> collection, @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode,
                                      Object... placeholders) {
        noNullElements(collection, ErrorCodeLoader.getErrMessage(errCode, placeholders));
    }

    public static void notEmpty(@NonNull Object[] array, @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode,
                                Object... placeholders) {
        notEmpty(array, ErrorCodeLoader.getErrMessage(errCode, placeholders));
    }

    public static void notEmpty(@NonNull Collection<?> collection, @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode,
                                Object... placeholders) {
        notEmpty(collection, ErrorCodeLoader.getErrMessage(errCode, placeholders));
    }

    public static void notEmpty(@NonNull Map<?, ?> map, @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode,
                                Object... placeholders) {
        notEmpty(map, ErrorCodeLoader.getErrMessage(errCode, placeholders));
    }

    public static void isAssignable(Class<?> superType, @NonNull Class<?> subType, @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode,
                                    Object... placeholders) {
        isAssignable(superType, subType, ErrorCodeLoader.getErrMessage(errCode, placeholders));
    }

    public static void state(boolean expression, @PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode,
                             Object... placeholders) {
        state(expression, ErrorCodeLoader.getErrMessage(errCode, placeholders));
    }
}
