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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
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
 * Result object for paginated data responses that contains a collection of items and
 * total count. This class extends the base Result class to provide pagination-specific
 * information.
 *
 * @param <T> the type of elements in the result collection
 * @author RADP x9x
 * @since 2024-09-26 16:02
 */
@SuperBuilder(builderMethodName = "pageResultBuilder")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Getter
@SuppressWarnings("java:S1948")
public class PageResult<T> extends Result {

	private static final long serialVersionUID = 1L;

	/**
	 * The collection of data items for the current page.
	 */
	private Collection<T> data;

	/**
	 * The total number of items across all pages.
	 */
	@Builder.Default
	private Long total = 0L;

	/**
	 * Gets the data collection from this page result. If the data is null, returns an
	 * empty collection. If the data is not already a List, converts it to an ArrayList.
	 * @return the collection of data items, never null
	 */
	public Collection<T> getData() {
		if (this.data == null) {
			return Collections.emptyList();
		}
		if (this.data instanceof List) {
			return this.data;
		}
		return new ArrayList<>(this.data);
	}

	/**
	 * Creates a successful {@code PageResult} instance with default values. The result
	 * contains an empty data collection, a total count of 0, and a success status with
	 * corresponding global success code and message.
	 * @param <T> the type parameter for the data collection held by the
	 * {@code PageResult}
	 * @return a new {@code PageResult} instance indicating success with empty data and
	 * default values
	 */
	public static <T> PageResult<T> ok() {
		return PageResult.<T>pageResultBuilder()
			.success(true)
			.data(Collections.emptyList())
			.code(GlobalResponseCode.SUCCESS.code())
			.msg(GlobalResponseCode.SUCCESS.message())
			.total(0L)
			.build();
	}

	/**
	 * Creates a successful PageResult with the specified collection of data and total
	 * count.
	 * @param <T> the type of elements in the result collection
	 * @param data the collection of data to include in the result
	 * @param total the total number of items (across all pages)
	 * @return a new PageResult instance with success status and the provided data and
	 * total
	 */
	public static <T> PageResult<T> ok(Collection<T> data, Long total) {
		return PageResult.<T>pageResultBuilder()
			.success(true)
			.code(GlobalResponseCode.SUCCESS.code())
			.msg(GlobalResponseCode.SUCCESS.message())
			.data(data)
			.total(total)
			.build();
	}

	/**
	 * Creates a successful {@code PageResult} instance with the specified collection of
	 * data, total count, and a custom message. The method builds a result object
	 * indicating a successful operation, populating it with the provided data, the total
	 * count of items, and a user-defined message.
	 * @param <T> the type of elements in the result collection
	 * @param data the collection of data to include in the result
	 * @param total the total number of items (across all pages)
	 * @param message a custom message providing additional context or information about
	 * the result
	 * @return a new {@code PageResult} instance with success status, the provided data,
	 * total, and message
	 */
	public static <T> PageResult<T> ok(Collection<T> data, Long total, String message) {
		return PageResult.<T>pageResultBuilder()
			.success(true)
			.code(GlobalResponseCode.SUCCESS.code())
			.msg(message)
			.data(data)
			.total(total)
			.build();
	}

	/**
	 * Creates a failure PageResult with the specified error code.
	 * @param <T> the type of elements in the result collection (will be empty for
	 * failure)
	 * @param errorCode the error code containing code and message for the failure
	 * @return a new PageResult instance with failure status and the provided error
	 * details
	 */
	public static <T> PageResult<T> failed(ErrorCode errorCode) {
		return PageResult.<T>pageResultBuilder()
			.success(false)
			.code(errorCode.getCode())
			.msg(errorCode.getMessage())
			.build();
	}

	/**
	 * Creates a failure {@code PageResult} instance with the specified error code and
	 * optional placeholders for the error message. The method uses the provided error
	 * code to retrieve the corresponding error message, which can include placeholders
	 * replaced by the given values.
	 * @param <T> the type of elements in the result collection (will be empty for
	 * failures)
	 * @param errCode the error code used to generate the error message
	 * @param placeholders the parameters to replace placeholders in the error message
	 * template
	 * @return a new {@code PageResult} instance with failure status, the specified error
	 * code, and the resolved error message
	 */
	public static <T> PageResult<T> failed(@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode,
			Object... placeholders) {
		return PageResult.<T>pageResultBuilder()
			.success(false)
			.code(errCode)
			.msg(ErrorCodeLoader.getErrMessage(errCode, placeholders))
			.build();
	}

	/**
	 * Creates a failure {@code PageResult} instance with the specified error code, error
	 * message, and optional placeholders for formatting the message. The method uses the
	 * given error code and error message to define the failure details and applies the
	 * placeholders to format the error message.
	 * @param <T> the type of elements in the result collection (will be empty for
	 * failures)
	 * @param errCode the error code representing the specific failure
	 * @param errMessage the error message template that describes the failure
	 * @param placeholders the parameters to replace placeholders in the error message
	 * template
	 * @return a new {@code PageResult} instance with failure status, the specified error
	 * code, and the formatted error message
	 */
	public static <T> PageResult<T> failed(@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode,
			String errMessage, Object... placeholders) {
		return PageResult.<T>pageResultBuilder()
			.success(false)
			.code(errCode)
			.msg(MessageFormatUtils.format(errMessage, placeholders))
			.build();
	}

}
