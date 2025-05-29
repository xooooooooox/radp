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

package space.x9x.radp.spring.framework.dto;

import org.jetbrains.annotations.PropertyKey;
import space.x9x.radp.commons.lang.MessageFormatUtils;
import space.x9x.radp.spring.framework.dto.extension.ResponseBuilder;
import space.x9x.radp.spring.framework.error.ErrorCode;
import space.x9x.radp.spring.framework.error.ErrorCodeLoader;

/**
 * Implementation of the ResponseBuilder interface for creating Result objects. This class
 * provides methods for building successful and failure responses with various parameters
 * and error codes.
 *
 * @author IO x9x
 * @since 2024-09-26 20:19
 */
public class ResultBuilder implements ResponseBuilder<Result> {

	/**
	 * Creates a successful Result with standard success code and message.
	 * @return a new Result instance with success status
	 */
	@Override
	public Result buildSuccess() {
		return Result.buildSuccess();
	}

	/**
	 * Creates a successful Result containing the provided data. If the data is null,
	 * returns a basic Result. Otherwise, returns a SingleResult containing the data.
	 * @param <T> the type of data to include in the result
	 * @param data the data to include in the result
	 * @return a new Result instance with success status and the provided data
	 */
	@Override
	public <T> Result buildSuccess(T data) {
		if (data == null) {
			return new Result();
		}
		return SingleResult.build(data);
	}

	/**
	 * Creates a failure Result with the specified error code. The error message is loaded
	 * from the resource bundle using the error code.
	 * @param errCode the error code key in the resource bundle
	 * @param params parameters to substitute in the error message
	 * @return a new Result instance with failure status and the provided error details
	 */
	@Override
	public Result buildFailure(@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode,
			Object... params) {
		return Result.builder()
			.success(false)
			.code(errCode)
			.msg(ErrorCodeLoader.getErrMessage(errCode, params))
			.build();
	}

	/**
	 * Creates a failure Result with the specified error code and custom error message.
	 * The error message is formatted using the provided parameters.
	 * @param errCode the error code
	 * @param errMessage the custom error message template
	 * @param params parameters to substitute in the error message
	 * @return a new Result instance with failure status and the provided error details
	 */
	@Override
	public Result buildFailure(@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode,
			String errMessage, Object... params) {
		return Result.builder().success(false).code(errCode).msg(MessageFormatUtils.format(errMessage, params)).build();
	}

	/**
	 * Creates a failure Result with the specified ErrorCode object. Uses the code and
	 * message from the ErrorCode object.
	 * @param errorCode the ErrorCode object containing code and message
	 * @return a new Result instance with failure status and the provided error details
	 */
	@Override
	public Result buildFailure(ErrorCode errorCode) {
		return Result.builder().success(false).code(errorCode.getCode()).msg(errorCode.getMessage()).build();
	}

}
