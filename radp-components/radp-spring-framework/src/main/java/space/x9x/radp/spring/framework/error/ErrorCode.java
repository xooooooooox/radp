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

package space.x9x.radp.spring.framework.error;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.PropertyKey;

/**
 * @author IO x9x
 * @since 2024-10-24 10:31
 */
@Data
@AllArgsConstructor
public class ErrorCode {

    private final String code;
    private final String message;

    /**
     * Constructs a new ErrorCode with the specified error code and parameters.
     * The error message is loaded from the resource bundle using the error code,
     * and the parameters are used for message formatting.
     *
     * @param errCode the error code that identifies the error message in the resource bundle
     * @param params  the parameters to be substituted in the error message
     */
    public ErrorCode(@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... params) {
        this.code = errCode;
        this.message = ErrorCodeLoader.getErrMessage(errCode, params);
    }
}
