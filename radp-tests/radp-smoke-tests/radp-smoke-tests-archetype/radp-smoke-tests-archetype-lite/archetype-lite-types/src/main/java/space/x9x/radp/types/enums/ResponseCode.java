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

package space.x9x.radp.types.enums;

import lombok.Getter;
import org.jetbrains.annotations.PropertyKey;

import space.x9x.radp.spring.framework.error.ErrorCode;
import space.x9x.radp.spring.framework.error.ErrorCodeLoader;
import space.x9x.radp.spring.framework.error.GlobalResponseCode;

/**
 * 业务返回码枚举.
 *
 * @author x9x
 * @since 2024-10-24 14:08
 * @see GlobalResponseCode
 */
@Getter
public enum ResponseCode {

	;

	/**
	 * The error code associated with this response code.
	 */
	private final ErrorCode errorCode;

	/**
	 * Constructs a ResponseCode with the specified code and message.
	 * @param code the error code
	 * @param message the error message
	 */
	ResponseCode(String code, String message) {
		this.errorCode = new ErrorCode(code, message);
	}

	/**
	 * Constructs a ResponseCode with the specified error code and parameters.
	 * @param errCode the error code from the resource bundle
	 * @param params the parameters to be used in the error message
	 */
	ResponseCode(@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, Object... params) {
		this.errorCode = new ErrorCode(errCode, params);
	}

	/**
	 * Returns the code of this response code.
	 * @return the code as a String
	 */
	public String code() {
		return this.errorCode.getCode();
	}

	/**
	 * Returns the message of this response code.
	 * @return the message as a String
	 */
	public String msg() {
		return this.errorCode.getMessage();
	}

}
