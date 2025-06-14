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

#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.types.exception.asserts;

import space.x9x.radp.spring.framework.error.ErrorCode;
import space.x9x.radp.spring.framework.error.asserts.AbstractAssert;

/**
 * utility class that provides assertion methods for validating arguments and state. this
 * class extends AbstractAssert to provide common assertion functionality.
 *
 * @author IO x9x
 * @since 2024-10-24 22:17
 */
public class AssertUtils extends AbstractAssert {

	/**
	 * asserts that the specified object is not null, throwing an IllegalArgumentException
	 * with the message from the specified error code if it is.
	 * @param object the object to check
	 * @param errorCode the error code to use for the exception message
	 * @throws IllegalArgumentException if the object is null
	 */
	public static void notNull(Object object, ErrorCode errorCode) {
		notNull(object, errorCode.getMessage());
	}

}
