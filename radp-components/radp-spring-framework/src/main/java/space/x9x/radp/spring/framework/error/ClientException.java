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
 * @since 2024-09-26 23:31
 */
@EqualsAndHashCode(callSuper = true)
public class ClientException extends BaseException {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new ClientException with the specified error code and throwable cause.
	 * The error message is loaded from the resource bundle using the error code.
	 * @param errCode the error code that identifies the error message in the resource
	 * bundle
	 * @param t the throwable cause of this exception
	 */
	public ClientException(@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Throwable t) {
		super(errCode, t);
	}

	/**
	 * Constructs a new ClientException with the specified error code, error message, and
	 * parameters. The parameters are used for message formatting.
	 * @param errCode the error code that identifies the error in the resource bundle
	 * @param errMessage the error message pattern
	 * @param params the parameters to be substituted in the error message
	 */
	public ClientException(@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, String errMessage,
			Object... params) {
		super(errCode, errMessage, params);
	}

	/**
	 * Constructs a new ClientException with the specified error code and parameters. The
	 * error message is loaded from the resource bundle using the error code, and the
	 * parameters are used for message formatting.
	 * @param errCode the error code that identifies the error message in the resource
	 * bundle
	 * @param params the parameters to be substituted in the error message
	 */
	public ClientException(@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode,
			Object... params) {
		super(errCode, params);
	}

	/**
	 * Constructs a new ClientException with the specified ErrorCode object. The error
	 * code and message are extracted from the ErrorCode object.
	 * @param errorCode the ErrorCode object containing the error code and message
	 */
	public ClientException(ErrorCode errorCode) {
		super(errorCode);
	}

	/**
	 * Constructs a new ClientException with the specified ErrorCode object and
	 * parameters. The error code and message are extracted from the ErrorCode object, and
	 * the parameters are used for message formatting.
	 * @param errorCode the ErrorCode object containing the error code and message
	 * @param params the parameters to be substituted in the error message
	 */
	public ClientException(ErrorCode errorCode, Object... params) {
		super(errorCode, params);
	}

}
