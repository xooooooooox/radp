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

import java.io.Serial;

import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.PropertyKey;

/**
 * Exception class for server-side errors. This exception represents errors that occur on
 * the server side, such as database failures, service unavailability, or internal
 * processing errors. It extends BaseException to provide standardized error handling.
 *
 * @author x9x
 * @since 2024-09-26 23:46
 */
@EqualsAndHashCode(callSuper = true)
public class ServerException extends BaseException {

	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new server exception with the specified error code and cause.
	 * @param errCode the error code from the resource bundle
	 * @param t the cause of this exception
	 */
	public ServerException(@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Throwable t) {
		super(errCode, t);
	}

	/**
	 * Constructs a new server exception with the specified error code, message pattern,
	 * and parameters.
	 * @param errCode the error code from the resource bundle
	 * @param messagePattern the pattern for the error message
	 * @param params the parameters to be used in the message pattern
	 */
	public ServerException(@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode,
			String messagePattern, Object... params) {
		super(errCode, messagePattern, params);
	}

	/**
	 * Constructs a new server exception with the specified error code and parameters.
	 * @param errCode the error code from the resource bundle
	 * @param params the parameters to be used in the error message
	 */
	public ServerException(@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode,
			Object... params) {
		super(errCode, params);
	}

	/**
	 * Constructs a new server exception with the specified error code.
	 * @param errorCode the error code object
	 */
	public ServerException(ErrorCode errorCode) {
		super(errorCode);
	}

	/**
	 * Constructs a new server exception with the specified error code and parameters.
	 * @param errorCode the error code object
	 * @param params the parameters to be used in the error message
	 */
	public ServerException(ErrorCode errorCode, Object... params) {
		super(errorCode, params);
	}

}
