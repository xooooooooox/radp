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

package space.x9x.radp.spring.framework.logging.access.model;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model class for storing access log information. This class captures details about
 * method invocations or HTTP requests, including location (method or URI), arguments,
 * return values, execution time, and any exceptions that occurred during execution.
 *
 * @author x9x
 * @since 2024-09-30 09:54
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccessLog {

	/**
	 * The location where the access occurred. For method invocations, this is the class
	 * and method name. For HTTP requests, this is the request URI.
	 */
	private String location;

	/**
	 * The arguments passed to the method or the request body. This may be truncated if it
	 * exceeds the configured maximum length.
	 */
	private String arguments;

	/**
	 * The return value from the method or the response body. This may be truncated if it
	 * exceeds the configured maximum length.
	 */
	private String returnValue;

	/**
	 * The exception that was thrown during execution, if any. This is null if the
	 * execution completed successfully.
	 */
	private Throwable throwable;

	/**
	 * The execution time in milliseconds. This is the time taken to execute the method or
	 * process the HTTP request.
	 */
	private long duration;

	/**
	 * The start time of the execution. This is transient and not included in serialized
	 * forms of this object.
	 */
	private transient Instant start;

	/**
	 * The end time of the execution. This is transient and not included in serialized
	 * forms of this object.
	 */
	private transient Instant end;

}
