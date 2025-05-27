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

import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.PropertyKey;

/**
 * @author IO x9x
 * @since 2024-09-26 23:49
 */
@EqualsAndHashCode(callSuper = true)
public class ThirdServiceException extends BaseException {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new ThirdServiceException with the specified error code and cause.
	 * @param errCode the error code from the resource bundle
	 * @param t the cause of this exception
	 */
	public ThirdServiceException(@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode,
			Throwable t) {
		super(errCode, t);
	}

	/**
	 * Constructs a new ThirdServiceException with the specified error code, error
	 * message, and parameters.
	 * @param errCode the error code from the resource bundle
	 * @param errMessage the error message
	 * @param params the parameters to be used in the error message
	 */
	public ThirdServiceException(@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode,
			String errMessage, Object... params) {
		super(errCode, errMessage, params);
	}

	/**
	 * Constructs a new ThirdServiceException with the specified error code and
	 * parameters.
	 * @param errCode the error code from the resource bundle
	 * @param params the parameters to be used in the error message
	 */
	public ThirdServiceException(@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode,
			Object... params) {
		super(errCode, params);
	}

	/**
	 * Constructs a new ThirdServiceException with the specified error code.
	 * @param errorCode the error code object
	 */
	public ThirdServiceException(ErrorCode errorCode) {
		super(errorCode);
	}

	/**
	 * Constructs a new ThirdServiceException with the specified error code and
	 * parameters.
	 * @param errorCode the error code object
	 * @param params the parameters to be used in the error message
	 */
	public ThirdServiceException(ErrorCode errorCode, Object... params) {
		super(errorCode, params);
	}

}
