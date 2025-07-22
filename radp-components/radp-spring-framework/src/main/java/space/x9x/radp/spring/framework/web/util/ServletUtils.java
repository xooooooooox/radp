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
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.experimental.UtilityClass;

import org.springframework.http.MediaType;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import space.x9x.radp.commons.lang.StringUtils;
import space.x9x.radp.commons.lang.Strings;

/**
 * Utility class providing helper methods for working with HTTP servlet requests and
 * responses. This class offers convenient methods for accessing request/response objects,
 * extracting information from them, and handling request/response content in a web
 * application context.
 *
 * @author IO x9x
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
		return StringUtils.trimToEmpty(request.getRemoteUser());
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
		return StringUtils.trimToEmpty(request.getRequestURI());
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
		return StringUtils.trimToEmpty(request.getLocalAddr());
	}

}
