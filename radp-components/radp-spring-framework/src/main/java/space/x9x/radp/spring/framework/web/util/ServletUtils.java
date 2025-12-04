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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.PropertyKey;

import org.springframework.http.MediaType;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import space.x9x.radp.commons.lang.StrUtils;
import space.x9x.radp.commons.lang.Strings;
import space.x9x.radp.spring.framework.dto.extension.ResponseBuilder;
import space.x9x.radp.spring.framework.error.ErrorCodeLoader;
import space.x9x.radp.spring.framework.json.support.JSONHelper;

/**
 * Utility class providing helper methods for working with HTTP servlet requests and
 * responses. This class offers convenient methods for accessing request/response objects,
 * extracting information from them, and handling request/response content in a web
 * application context.
 *
 * @author x9x
 * @since 2024-09-27 10:54
 */
@UtilityClass
public class ServletUtils {

	/**
	 * Constant for the Accept-Ranges header value.
	 */
	public static final String ACCEPT_RANGES = "bytes";

	/**
	 * Constant for the Content-Disposition header value for attachments. The {0}
	 * placeholder will be replaced with the filename.
	 */
	public static final String CONTENT_DISPOSITION_ATTACH = "attachment;filename={0}";

	/**
	 * Spring 已标记弃用，但用户不升级 Chrome 是无法解决问题的.
	 */
	public static final String APPLICATION_JSON_UTF8_VALUE = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8";

	/**
	 * Gets the current servlet request attributes from the request context.
	 * @return the current servlet request attributes, or null if not available
	 */
	public static ServletRequestAttributes getRequestAttributes() {
		return (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
	}

	/**
	 * Gets the current HTTP servlet request from the request context.
	 * @return the current HTTP servlet request, or null if not available
	 */
	public static HttpServletRequest getRequest() {
		ServletRequestAttributes attributes = getRequestAttributes();
		if (attributes != null) {
			return attributes.getRequest();
		}
		return null;
	}

	/**
	 * Gets the current HTTP servlet response from the request context.
	 * @return the current HTTP servlet response, or null if not available
	 */
	public static HttpServletResponse getResponse() {
		ServletRequestAttributes attributes = getRequestAttributes();
		if (attributes != null) {
			return attributes.getResponse();
		}
		return null;
	}

	/**
	 * Gets the remote user from the current HTTP request.
	 * @return the remote user as a string, or empty string if not available
	 */
	public static String getRemoteUser() {
		HttpServletRequest request = getRequest();
		return getRemoteUser(request);
	}

	/**
	 * Gets the remote user from the specified HTTP request.
	 * @param request the HTTP servlet request
	 * @return the remote user as a string, or empty string if not available
	 */
	public static String getRemoteUser(HttpServletRequest request) {
		return StrUtils.trimToEmpty(request.getRemoteUser());
	}

	/**
	 * Gets the body content of the HTTP servlet request.
	 * @param request the HTTP servlet request
	 * @return the request body as a string, or null if not available
	 * @throws RuntimeException if an I/O error occurs
	 */
	public static String getRequestBOdy(HttpServletRequest request) {
		try (BufferedReader reader = request.getReader()) {
			if (reader != null) {
				return reader.lines().collect(Collectors.joining(System.lineSeparator()));
			}
		}
		catch (IOException ex) {
			throw new RuntimeException(ex);
		}
		return null;
	}

	/**
	 * Gets the body content of the HTTP servlet response.
	 * @param response the HTTP servlet response
	 * @return the response body as a string, or empty string if not available
	 */
	public static String getResponseBody(HttpServletResponse response) {
		if (response instanceof CustomHttpServletResponseWrapper) {
			return ((CustomHttpServletResponseWrapper) response).getContent();
		}
		String contentType = response.getContentType();
		if (contentType != null && contentType.startsWith(MediaType.APPLICATION_JSON_VALUE)) {
			return response.toString();
		}
		return Strings.EMPTY;
	}

	/**
	 * Gets the URI of the current HTTP request.
	 * @return the request URI as a string, or empty string if not available
	 */
	public static String getRequestURI() {
		return getRequestURI(getRequest());
	}

	/**
	 * Gets the URI of the specified HTTP request.
	 * @param request the HTTP servlet request
	 * @return the request URI as a string, or empty string if not available
	 */
	public static String getRequestURI(HttpServletRequest request) {
		return StrUtils.trimToEmpty(request.getRequestURI());
	}

	/**
	 * Gets the local address of the current HTTP request.
	 * @return the local address as a string, or empty string if not available
	 */
	public static String getLocalAddr() {
		return getLocalAddr(getRequest());
	}

	/**
	 * Gets the local address of the specified HTTP request.
	 * @param request the HTTP servlet request
	 * @return the local address as a string, or empty string if not available
	 */
	public static String getLocalAddr(HttpServletRequest request) {
		return StrUtils.trimToEmpty(request.getLocalAddr());
	}

	/**
	 * Wraps the given HTTP servlet response with a success response in JSON format. This
	 * method sets the HTTP status code, content type, and writes a default success
	 * response as JSON to the response body.
	 * @param response the HTTP servlet response object to be wrapped
	 * @param statusCode the HTTP status code to set on the response
	 * @throws IOException if an I/O error occurs during response writing
	 */
	public static void wrap(HttpServletResponse response, int statusCode) throws IOException {
		response.setStatus(statusCode);
		response.setContentType(APPLICATION_JSON_UTF8_VALUE);
		PrintWriter writer = response.getWriter();
		String result = JSONHelper.json().toJSONString(ResponseBuilder.builder().buildSuccess());
		writer.write(result);
	}

	/**
	 * Wraps the given HTTP response with JSON content representing a failure message.
	 * This method sets the HTTP status code, content type, and writes a JSON response
	 * containing the error code, message, and optional parameters.
	 * @param response the HTTP servlet response object to be wrapped
	 * @param statusCode the HTTP status code to set on the response
	 * @param errCode the error code associated with the failure, derived from a resource
	 * bundle
	 * @param errMessage error message
	 * @param params optional parameters to include in the error message
	 * @throws IOException if an I/O error occurs during response writing
	 */
	public static void wrap(HttpServletResponse response, int statusCode,
			@PropertyKey(resourceBundle = ErrorCodeLoader.BUNDLE_NAME) String errCode, String errMessage,
			Object... params) throws IOException {
		response.setStatus(statusCode);
		response.setContentType(APPLICATION_JSON_UTF8_VALUE);
		PrintWriter writer = response.getWriter();
		String result = JSONHelper.json()
			.toJSONString(ResponseBuilder.builder().buildFailure(errCode, errMessage, params));
		writer.write(result);
	}

}
