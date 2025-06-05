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

package space.x9x.radp.types.exception;

import org.jetbrains.annotations.PropertyKey;

import space.x9x.radp.spring.framework.error.ErrorCode;
import space.x9x.radp.spring.framework.error.ServerException;

/**
 * Application-specific exception class that extends the ServerException. This exception
 * is used to represent application-level errors with specific error codes.
 *
 * @author IO x9x
 * @since 2025-01-14 23:18
 */
public class AppException extends ServerException {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructs an AppException with the specified error code and cause.
	 * @param errCode the error code from the resource bundle
	 * @param t the cause of this exception
	 */
	public AppException(@PropertyKey(resourceBundle = "META-INF.error.message") String errCode, Throwable t) {
		super(errCode, t);
	}

	/**
	 * Constructs an AppException with the specified error code, message pattern, and
	 * parameters.
	 * @param errCode the error code from the resource bundle
	 * @param messagePattern the pattern for the error message
	 * @param params the parameters to be used in the message pattern
	 */
	public AppException(@PropertyKey(resourceBundle = "META-INF.error.message") String errCode, String messagePattern,
			Object... params) {
		super(errCode, messagePattern, params);
	}

	/**
	 * Constructs an AppException with the specified error code and parameters.
	 * @param errCode the error code from the resource bundle
	 * @param params the parameters to be used in the error message
	 */
	public AppException(@PropertyKey(resourceBundle = "META-INF.error.message") String errCode, Object... params) {
		super(errCode, params);
	}

	/**
	 * Constructs an AppException with the specified error code object.
	 * @param errorCode the error code object containing code and message information
	 */
	public AppException(ErrorCode errorCode) {
		super(errorCode);
	}

	/**
	 * Constructs an AppException with the specified error code object and parameters.
	 * @param errorCode the error code object containing code and message information
	 * @param params the parameters to be used in the error message
	 */
	public AppException(ErrorCode errorCode, Object... params) {
		super(errorCode, params);
	}

}
