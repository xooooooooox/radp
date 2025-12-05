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

import java.io.Serial;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import space.x9x.radp.spring.framework.error.GlobalResponseCode;

/**
 * Base class for API response objects that provides common result information. This class
 * includes status, code, and message fields to indicate the outcome of an operation. It
 * serves as the parent class for more specific result types like SingleResult and
 * MultiResult.
 *
 * @author RADP x9x
 * @since 2024-09-26 15:53
 */
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Getter
public class Result implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * Indicates whether the operation was successful.
	 */
	private boolean success;

	/**
	 * The response code. Typically used for error identification
	 */
	private String code;

	/**
	 * The human-readable message describing the result.
	 */
	private String msg;

	/**
	 * Creates a successful Result with standard success code and message.
	 * @return a new Result instance with success status
	 */
	public static Result buildSuccess() {
		return Result.builder()
			.code(GlobalResponseCode.SUCCESS.code())
			.msg(GlobalResponseCode.SUCCESS.message())
			.success(true)
			.build();
	}

}
