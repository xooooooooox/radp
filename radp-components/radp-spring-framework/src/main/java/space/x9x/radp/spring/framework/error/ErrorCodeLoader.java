package space.x9x.radp.spring.framework.error;

import space.x9x.radp.commons.lang.format.MessageFormatter;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.PropertyKey;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author x9x
 * @since 2024-09-26 16:14
 */
@Slf4j
@UtilityClass
public class ErrorCodeLoader {

    public static final String INTERNAL_BUNDLE_NAME = "META-INF.error.internal";
    public static final String BUNDLE_NAME = "META-INF.error.message";
    private static final ResourceBundle INTERNAL_RESOURCE_BUNDLE = ResourceBundle.getBundle(INTERNAL_BUNDLE_NAME, Locale.SIMPLIFIED_CHINESE);

    private static ResourceBundle resourceBundle;

    static {
        try {
            resourceBundle = ResourceBundle.getBundle(BUNDLE_NAME, Locale.SIMPLIFIED_CHINESE);
        } catch (Exception e) {
            log.error("加载应用错误码文件失败 {}, 将回退使用框架内置错误码资源文件 {}", BUNDLE_NAME + ".properties", INTERNAL_BUNDLE_NAME + ".properties", e);
        } finally {
            resourceBundle = ResourceBundle.getBundle(INTERNAL_BUNDLE_NAME, Locale.SIMPLIFIED_CHINESE);
        }
    }

    public static String getErrMessage(@PropertyKey(resourceBundle = BUNDLE_NAME) String errCode, Object... params) {
        if (resourceBundle.containsKey(errCode)) {
            String messagePattern = resourceBundle.getString(errCode);
            return MessageFormatter.arrayFormat(messagePattern, params).getMessage();
        }

        if (INTERNAL_RESOURCE_BUNDLE.containsKey(errCode)) {
            String messagePattern = INTERNAL_RESOURCE_BUNDLE.getString(errCode);
            return MessageFormatter.arrayFormat(messagePattern, params).getMessage();
        }

        return MessageFormatter.arrayFormat(errCode, params).getMessage();
    }
}
