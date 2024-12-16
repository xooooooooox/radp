package com.x9x.radp.spring.framework.web.extension;

import com.x9x.radp.extension.ExtensionLoader;
import com.x9x.radp.extension.SPI;
import com.x9x.radp.spring.framework.error.ErrorCodeLoader;
import org.jetbrains.annotations.PropertyKey;

/**
 * @author x9x
 * @since 2024-09-26 16:11
 */
@SPI("response")
public interface ResponseBuilder<T> {

    static ResponseBuilder<?> builder() {
        return ExtensionLoader.getExtensionLoader(ResponseBuilder.class).getDefaultExtension();
    }

    T buildSuccess();

    @SuppressWarnings("java:S119")
    <Body> T buildSuccess(Body data);

    T buildFailure(@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... params);

    T buildFailure(@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, String errMessage, Object... params);
}
