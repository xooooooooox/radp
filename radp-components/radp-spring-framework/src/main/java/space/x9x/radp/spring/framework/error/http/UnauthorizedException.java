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

import lombok.EqualsAndHashCode;

import space.x9x.radp.spring.framework.error.BaseException;

/**
 * Exception thrown when a user attempts to access a resource without proper
 * authentication. This exception corresponds to HTTP 401 Unauthorized status.
 *
 * @author RADP x9x
 * @since 2024-09-27 11:20
 */
@EqualsAndHashCode(callSuper = true)
public class UnauthorizedException extends BaseException {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new UnauthorizedException with the specified error message and
	 * optional parameters.
	 * @param errMessage the error message describing the unauthorized access
	 * @param params optional parameters to be used in formatting the error message
	 */
	public UnauthorizedException(String errMessage, Object... params) {
		super("401", errMessage, params);
	}

}
