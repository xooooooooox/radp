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

package space.x9x.radp.spring.framework.web.util;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * HttpServletResponse 包装器.
 *
 * @author IO x9x
 * @since 2024-09-27 20:55
 */
public class CustomHttpServletResponseWrapper extends HttpServletResponseWrapper {

	/**
	 * The output stream that captures the response content.
	 */
	private final ByteArrayOutputStream outputStream;

	/**
	 * The writer that writes to the output stream.
	 */
	private final PrintWriter writer;

	/**
	 * Constructs a response adaptor wrapping the given response.
	 * @param response the {@link HttpServletResponse} to be wrapped.
	 * @throws IllegalArgumentException if the response is null
	 */
	public CustomHttpServletResponseWrapper(HttpServletResponse response) {
		super(response);
		this.outputStream = new ByteArrayOutputStream();
		this.writer = new PrintWriter(this.outputStream, true);
	}

	@Override
	public PrintWriter getWriter() {
		return this.writer;
	}

	/**
	 * Returns the content of the response as a String. This method converts the captured
	 * response bytes to a String using UTF-8 encoding.
	 * @return the content of the response as a String
	 */
	public String getContent() {
		return new String(this.outputStream.toByteArray(), StandardCharsets.UTF_8);
	}

}
