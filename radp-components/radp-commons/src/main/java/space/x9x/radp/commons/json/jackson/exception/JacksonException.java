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

package space.x9x.radp.commons.json.jackson.exception;

/**
 * @author x9x
 * @since 2024-09-23 13:54
 */
public class JacksonException extends RuntimeException {

    private static final long serialVersionUID = 9041976807836061564L;

    /**
     * Constructs a new JacksonException with the specified cause.
     * <p>
     * This exception is typically used to wrap Jackson-related exceptions
     * while preserving the original cause for debugging purposes.
     *
     * @param cause the cause of this exception
     */
    public JacksonException(Throwable cause) {
        super(cause);
    }
}
