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

package space.x9x.radp.spring.framework.error.http;

import lombok.EqualsAndHashCode;
import space.x9x.radp.spring.framework.error.BaseException;

/**
 * @author x9x
 * @since 2024-09-27 11:18
 */
@EqualsAndHashCode(callSuper = true)
public class ForbiddenException extends BaseException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new ForbiddenException with the specified error message and parameters.
     * This exception is used to indicate that the server understood the request but refuses to authorize it (HTTP 403).
     *
     * @param errMessage the error message pattern
     * @param params     the parameters to be substituted in the error message
     */
    public ForbiddenException(String errMessage, Object... params) {
        super("403", errMessage, params);
    }
}
