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

package space.x9x.radp.spring.data.jdbc.datasource;

/**
 * Exception thrown when there is an error parsing a data source URL. This exception is
 * used to wrap the original exception with additional context information.
 *
 * @author IO x9x
 * @since 2024-09-30 13:52
 */
public class DataSourceUrlParserException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new DataSourceUrlParserException with the specified detail message and
	 * cause.
	 * @param message the detail message (which is saved for later retrieval by the
	 * {@link #getMessage()} method)
	 * @param cause the cause (which is saved for later retrieval by the
	 * {@link #getCause()} method)
	 */
	public DataSourceUrlParserException(String message, Throwable cause) {
		super(message, cause);
	}

}
