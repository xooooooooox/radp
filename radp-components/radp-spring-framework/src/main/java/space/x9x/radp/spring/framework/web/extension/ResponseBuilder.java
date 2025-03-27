package space.x9x.radp.spring.framework.web.extension;

import org.jetbrains.annotations.PropertyKey;
import space.x9x.radp.extension.ExtensionLoader;
import space.x9x.radp.extension.SPI;
import space.x9x.radp.spring.framework.beans.ApplicationContextHelper;
import space.x9x.radp.spring.framework.error.ErrorCode;
import space.x9x.radp.spring.framework.error.ErrorCodeLoader;

/**
 * @author x9x
 * @since 2024-09-26 16:11
 */
@SPI("response")
public interface ResponseBuilder<T> {

    static ResponseBuilder<?> builder() {
        ResponseBuilder<?> builder = ApplicationContextHelper.getBean(ResponseBuilder.class);
        if (builder != null) {
            return builder;
        }
        return ExtensionLoader.getExtensionLoader(ResponseBuilder.class).getDefaultExtension();
    }

    T buildSuccess();

    @SuppressWarnings("java:S119")
    <Body> T buildSuccess(Body data);

    T buildFailure(@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... params);

    T buildFailure(@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, String errMessage, Object... params);

    T buildFailure(ErrorCode errorCode);
}
