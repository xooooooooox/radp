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

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.jetbrains.annotations.PropertyKey;

import space.x9x.radp.commons.lang.MessageFormatUtils;
import space.x9x.radp.spring.framework.error.ErrorCode;
import space.x9x.radp.spring.framework.error.ErrorCodeLoader;
import space.x9x.radp.spring.framework.error.GlobalResponseCode;

/**
 * Result object for responses that contain a single data item. This class extends the
 * base Result class to provide a typed data field.
 *
 * @author x9x
 * @since 2024-09-26 15:57
 * @param <T> the type of the data item in the result
 */
@SuperBuilder(builderMethodName = "singleResultBuilder")
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Getter
@SuppressWarnings("java:S1948")
public class SingleResult<T> extends Result {

	private static final long serialVersionUID = 1L;

	/**
	 * The single data item returned in the response.
	 */
	private T data;

	/**
	 * Creates a successful result with no data.
	 * @param <T> the type of the data item
	 * @return a new SingleResult instance with success status and no data
	 */
	public static <T> SingleResult<T> ok() {
		return ok(null);
	}

	/**
	 * Creates a successful result with the specified data.
	 * @param <T> the type of the data item
	 * @param data the data to include in the result
	 * @return a new SingleResult instance with success status and the specified data
	 */
	public static <T> SingleResult<T> ok(T data) {
		return SingleResult.<T>singleResultBuilder()
			.success(true)
			.code(GlobalResponseCode.SUCCESS.code())
			.msg(GlobalResponseCode.SUCCESS.message())
			.data(data)
			.build();
	}

	/**
	 * Creates and returns a SingleResult object that represents a successful operation.
	 * @param <T> the type of the data to be included in the result
	 * @param data the payload or data to include in the result object
	 * @param message a descriptive message indicating the success of the operation
	 * @return a SingleResult object containing the success flag, status code, message,
	 * and the provided data
	 */
	public static <T> SingleResult<T> ok(T data, String message) {
		return SingleResult.<T>singleResultBuilder()
			.success(true)
			.code(GlobalResponseCode.SUCCESS.code())
			.msg(message)
			.data(data)
			.build();
	}

	/**
	 * Creates a failure result with the specified error code.
	 * @param <T> the type of the data item
	 * @param errorCode the error code to include in the result
	 * @return a new SingleResult instance with failure status and the specified error
	 * code
	 */
	public static <T> SingleResult<T> failed(ErrorCode errorCode) {
		return SingleResult.<T>singleResultBuilder()
			.success(false)
			.code(errorCode.getCode())
			.msg(errorCode.getMessage())
			.build();
	}

	/**
	 * Creates a failure result with the specified error code and optional placeholders
	 * for formatting the error message. The error message is retrieved from a resource
	 * bundle based on the provided error code.
	 * @param <T> the type of the data item in the result
	 * @param errCode the error code to include in the result; a key in the resource
	 * bundle
	 * @param placeholders optional parameters to replace placeholders in the error
	 * message
	 * @return a new SingleResult instance with failure status, the specified error code,
	 * and the formatted error message
	 */
	public static <T> SingleResult<T> failed(@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode,
			Object... placeholders) {
		return SingleResult.<T>singleResultBuilder()
			.success(false)
			.code(errCode)
			.msg(ErrorCodeLoader.getErrMessage(errCode, placeholders))
			.build();
	}

	/**
	 * Constructs a failed {@code SingleResult} instance with the provided error code,
	 * error message, and optional placeholder values for message formatting.
	 * @param <T> the type of the result object
	 * @param errCode the error code associated with the failure
	 * @param errMessage the error message describing the failure
	 * @param placeholders optional placeholder values to format the error message
	 * @return a {@code SingleResult} instance representing a failed result
	 */
	public static <T> SingleResult<T> failed(@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode,
			String errMessage, Object... placeholders) {
		return SingleResult.<T>singleResultBuilder()
			.success(false)
			.code(errCode)
			.msg(MessageFormatUtils.format(errMessage, placeholders))
			.build();
	}

}
