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

    /**
     * Factory method to obtain a ResponseBuilder instance.
     * First tries to get a bean from the application context, then falls back to the extension loader.
     *
     * @return a ResponseBuilder instance
     */
    static ResponseBuilder<?> builder() {
        ResponseBuilder<?> builder = ApplicationContextHelper.getBean(ResponseBuilder.class);
        if (builder != null) {
            return builder;
        }
        return ExtensionLoader.getExtensionLoader(ResponseBuilder.class).getDefaultExtension();
    }

    /**
     * Builds a successful response with no data.
     *
     * @return a response object indicating success
     */
    T buildSuccess();

    /**
     * Builds a successful response with the provided data.
     *
     * @param <Body> the type of the data to include in the response
     * @param data   the data to include in the response
     * @return a response object indicating success with the provided data
     */
    @SuppressWarnings("java:S119")
    <Body> T buildSuccess(Body data);

    /**
     * Builds a failure response with the specified error code and parameters.
     * The error message is loaded from the resource bundle using the error code.
     *
     * @param errCode the error code that identifies the error message in the resource bundle
     * @param params  the parameters to be substituted in the error message
     * @return a response object indicating failure with the error details
     */
    T buildFailure(@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... params);

    /**
     * Builds a failure response with the specified error code, custom error message, and parameters.
     * This method allows overriding the error message from the resource bundle.
     *
     * @param errCode the error code that identifies the error
     * @param errMessage the custom error message to use
     * @param params the parameters to be substituted in the error message
     * @return a response object indicating failure with the error details
     */
    T buildFailure(@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, String errMessage, Object... params);

    /**
     * Builds a failure response with the specified ErrorCode object.
     * The error code and message are extracted from the ErrorCode object.
     *
     * @param errorCode the ErrorCode object containing the error code and message
     * @return a response object indicating failure with the error details
     */
    T buildFailure(ErrorCode errorCode);
}
