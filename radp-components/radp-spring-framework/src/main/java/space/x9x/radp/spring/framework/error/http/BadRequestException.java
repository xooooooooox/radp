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

package space.x9x.radp.spring.framework.error.http;

import java.io.Serial;

import lombok.EqualsAndHashCode;

import space.x9x.radp.spring.framework.error.BaseException;

/**
 * Exception representing a 400 Bad Request HTTP error, indicating that the server cannot
 * process the request due to client error (e.g., malformed request syntax, invalid
 * request message framing, or deceptive request routing).
 *
 * @author RADP x9x
 * @since 2024-09-27 11:08
 */
@EqualsAndHashCode(callSuper = true)
public class BadRequestException extends BaseException {

	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new BadRequestException with the specified error message and
	 * parameters.
	 * @param errMessage the error message
	 * @param params the parameters to be used for message formatting
	 */
	public BadRequestException(String errMessage, Object... params) {
		super("400", errMessage, params);
	}

}
