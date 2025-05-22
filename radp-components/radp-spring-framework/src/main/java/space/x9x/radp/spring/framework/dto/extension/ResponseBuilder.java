/*
 * Copyright 2012-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package space.x9x.radp.spring.framework.dto.extension;

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
