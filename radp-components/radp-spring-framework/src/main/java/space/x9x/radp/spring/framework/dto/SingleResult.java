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

import lombok.*;
import lombok.experimental.SuperBuilder;
import space.x9x.radp.spring.framework.error.ErrorCode;
import space.x9x.radp.spring.framework.error.GlobalResponseCode;

/**
 * Result object for responses that contain a single data item. This class extends the
 * base Result class to provide a typed data field.
 *
 * @param <T> the type of the data item in the result
 * @author IO x9x
 * @since 2024-09-26 15:57
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
	 * The single data item returned in the response
	 */
	private T data;

	/**
	 * Creates a successful result with no data.
	 * @param <T> the type of the data item
	 * @return a new SingleResult instance with success status and no data
	 */
	public static <T> SingleResult<T> build() {
		return build(null);
	}

	/**
	 * Creates a successful result with the specified data.
	 * @param <T> the type of the data item
	 * @param data the data to include in the result
	 * @return a new SingleResult instance with success status and the specified data
	 */
	public static <T> SingleResult<T> build(T data) {
		return SingleResult.<T>singleResultBuilder()
			.success(true)
			.code(GlobalResponseCode.SUCCESS.code())
			.msg(GlobalResponseCode.SUCCESS.message())
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
	public static <T> SingleResult<T> buildFailure(ErrorCode errorCode) {
		return SingleResult.<T>singleResultBuilder()
			.success(false)
			.code(errorCode.getCode())
			.msg(errorCode.getMessage())
			.build();
	}

}
