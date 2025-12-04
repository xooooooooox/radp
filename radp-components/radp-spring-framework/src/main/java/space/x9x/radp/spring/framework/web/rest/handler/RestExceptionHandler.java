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

package space.x9x.radp.spring.framework.web.rest.handler;

import java.util.List;
import java.util.Set;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import space.x9x.radp.commons.collections.CollectionUtils;
import space.x9x.radp.commons.lang.MessageFormatUtils;
import space.x9x.radp.commons.lang.StringUtil;
import space.x9x.radp.extension.ExtensionLoader;
import space.x9x.radp.spring.framework.dto.extension.ResponseBuilder;
import space.x9x.radp.spring.framework.error.BaseException;
import space.x9x.radp.spring.framework.error.ClientException;
import space.x9x.radp.spring.framework.error.GlobalResponseCode;
import space.x9x.radp.spring.framework.error.ServerException;
import space.x9x.radp.spring.framework.error.ThirdServiceException;
import space.x9x.radp.spring.framework.error.http.BadRequestException;
import space.x9x.radp.spring.framework.error.http.ForbiddenException;
import space.x9x.radp.spring.framework.error.http.UnauthorizedException;
import space.x9x.radp.spring.framework.web.util.ServletUtils;

/**
 * Global exception handler for REST controllers. This class provides centralized
 * exception handling across all REST controllers in the application, converting various
 * exceptions into appropriate HTTP responses with standardized error formats.
 *
 * @author x9x
 * @since 2024-09-26 23:52
 */
@RestControllerAdvice
@Slf4j
public class RestExceptionHandler {

	/**
	 * Log message template used when catching exceptions in the handler methods. The
	 * placeholder {} will be replaced with the exception message.
	 */
	private static final String EXCEPTION_HANDLER_CATCH = "@RestControllerAdvice catch exception: {}";

	/**
	 * Handles general exceptions that are not caught by more specific handlers.
	 * @param ex the exception
	 * @return a response entity with error details
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> resolveException(Exception ex) {
		log.error(EXCEPTION_HANDLER_CATCH, ex.getMessage(), ex);
		ResponseStatus responseStatus = AnnotationUtils.findAnnotation(ex.getClass(), ResponseStatus.class);
		HttpStatus status = (responseStatus != null) ? responseStatus.value() : HttpStatus.INTERNAL_SERVER_ERROR;
		String message = (responseStatus != null && StringUtil.isNotBlank(responseStatus.reason()))
				? responseStatus.reason() : HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase();
		this.postProcess(ex);
		return this.buildResponseEntity(status, "500", message);
	}

	/**
	 * Handles validation exceptions thrown when method arguments fail validation.
	 * @param ex the validation exception
	 * @return a response entity with validation error details
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> resolveValidationException(MethodArgumentNotValidException ex) {
		BindingResult result = ex.getBindingResult();
		List<FieldError> fieldErrors = result.getFieldErrors();
		String message = StringUtil.join(fieldErrors.toArray(), ",");
		return this.buildResponseEntity(HttpStatus.BAD_REQUEST, "400", message);
	}

	/**
	 * Handles exceptions thrown when method argument types don't match the expected
	 * types.
	 * @param ex the method argument type mismatch exception
	 * @return a response entity with error details
	 */
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<?> resolveMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
		log.warn(EXCEPTION_HANDLER_CATCH, ex.getMessage(), ex);
		String message = "请求参数类型错误: " + ex.getMessage();
		return this.buildResponseEntity(HttpStatus.BAD_REQUEST, "400", message);
	}

	/**
	 * Handles exceptions thrown when an HTTP request method is not supported.
	 * @param ex the HTTP request method didn't support exception
	 * @return a response entity with a method didn't allow error details
	 */
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<?> resolveMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
		log.warn(EXCEPTION_HANDLER_CATCH, ex.getMessage(), ex);
		return this.buildResponseEntity(HttpStatus.METHOD_NOT_ALLOWED,
				GlobalResponseCode.METHOD_NOT_ALLOWED.getErrorCode().getCode(), ex.getMessage());

	}

	/**
	 * Handles exceptions thrown when a requested resource is not found.
	 * @param ex no resource found exception
	 * @return a response entity with not found error details
	 */
	@ExceptionHandler(NoResourceFoundException.class)
	public ResponseEntity<?> resolveNoResourceFoundException(NoResourceFoundException ex) {
		log.warn(EXCEPTION_HANDLER_CATCH, ex.getMessage());
		return this.buildResponseEntity(HttpStatus.NOT_FOUND, GlobalResponseCode.NOT_FOUND.getErrorCode().getCode(),
				ex.getMessage());
	}

	/**
	 * Handles bad request exceptions when the client sends an invalid request.
	 * @param ex the bad request exception
	 * @return a response entity with bad request error details
	 */
	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<?> resolveBadRequestException(BadRequestException ex) {
		return this.buildResponseEntity(HttpStatus.BAD_REQUEST, ex);
	}

	/**
	 * Handles unauthorized exceptions when a user is not authenticated.
	 * @param ex the unauthorized exception
	 * @return a response entity with unauthorized error details
	 */
	@ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<?> resolveUnauthorizedException(UnauthorizedException ex) {
		return this.buildResponseEntity(HttpStatus.UNAUTHORIZED, ex);
	}

	/**
	 * Handles forbidden exceptions when a user is not authorized to access a resource.
	 * @param ex the forbidden exception
	 * @return a response entity with forbidden error details
	 */
	@ExceptionHandler(ForbiddenException.class)
	public ResponseEntity<?> resolveForbiddenException(ForbiddenException ex) {
		return this.buildResponseEntity(HttpStatus.FORBIDDEN, ex);
	}

	/**
	 * Handles client exceptions for errors caused by client-side issues.
	 * @param ex the client exception
	 * @return a response entity with bad request error details
	 */
	@ExceptionHandler(ClientException.class)
	public ResponseEntity<?> resolveClientException(ClientException ex) {
		log.warn(EXCEPTION_HANDLER_CATCH, ex.getMessage(), ex);
		this.postProcess(ex);
		return this.buildResponseEntity(HttpStatus.BAD_REQUEST, ex);
	}

	/**
	 * Handles server exceptions for errors caused by server-side issues.
	 * @param ex the server exception
	 * @return a response entity with internal server error details
	 */
	@ExceptionHandler(ServerException.class)
	public ResponseEntity<?> resolveServerException(ServerException ex) {
		log.error(EXCEPTION_HANDLER_CATCH, ex.getMessage(), ex);
		this.postProcess(ex);
		return this.buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, ex);
	}

	/**
	 * Handles exceptions related to third-party service failures.
	 * @param ex the third service exception
	 * @return a response entity with internal server error details
	 */
	@ExceptionHandler(ThirdServiceException.class)
	public ResponseEntity<?> resolveThirdServiceException(ThirdServiceException ex) {
		log.error(EXCEPTION_HANDLER_CATCH, ex.getMessage(), ex);
		this.postProcess(ex);
		return this.buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, ex);
	}

	private ResponseBuilder<?> builder() {
		return ResponseBuilder.builder();
	}

	private void postProcess(Throwable ex) {
		ExtensionLoader<RestExceptionPostProcessor> extensionLoader = ExtensionLoader
			.getExtensionLoader(RestExceptionPostProcessor.class);
		Set<String> extensions = extensionLoader.getSupportedExtensions();
		if (CollectionUtils.isNotEmpty(extensions)) {
			HttpServletRequest request = ServletUtils.getRequest();
			HttpServletResponse response = ServletUtils.getResponse();
			for (String extension : extensions) {
				RestExceptionPostProcessor processor = extensionLoader.getExtension(extension);
				processor.postProcess(request, response, ex);
			}
		}
	}

	private boolean isSseRequest(HttpServletRequest request) {
		if (request == null) {
			return false;
		}
		String accept = request.getHeader("Accept");
		return accept != null && accept.contains("text/event-stream");
	}

	private boolean isBinaryOrStreamRequest(HttpServletRequest request) {
		if (request == null) {
			return false;
		}
		String accept = request.getHeader("Accept");
		if (accept == null) {
			return false;
		}
		return accept.contains("application/octet-stream") || accept.contains("application/stream+json")
				|| accept.contains("application/x-ndjson");
	}

	private String formatPlainMessage(String errMessage, Object... params) {
		if (params == null || params.length == 0) {
			return errMessage;
		}
		try {
			String formatted = MessageFormatUtils.format(errMessage, params);
			if (formatted != null) {
				return formatted;
			}
		}
		catch (Throwable ignored) {
			// ignore and fallback
		}
		String base = (errMessage == null ? "" : errMessage);
		return base + (base.isEmpty() ? "" : " ") + StringUtil.join(params, ",");
	}

	private String buildSseErrorEvent(String code, String message) {
		String safeCode = (code == null ? "" : code);
		String safeMsg = (message == null ? "" : message).replace("\r", " ").replace("\n", " ");
		return "event: error\n" + "data: " + safeCode + " " + safeMsg + "\n\n";
	}

	private ResponseEntity<?> buildNegotiatedResponse(HttpStatus httpStatus, String errCode, String errMessage,
			Object... params) {
		HttpServletRequest request = ServletUtils.getRequest();
		BodyBuilder builder = ResponseEntity.status(httpStatus);
		if (isSseRequest(request)) {
			String sse = buildSseErrorEvent(errCode, formatPlainMessage(errMessage, params));
			return builder.contentType(MediaType.TEXT_EVENT_STREAM).body(sse);
		}
		if (isBinaryOrStreamRequest(request)) {
			String text = "error=" + errCode + "; message=" + formatPlainMessage(errMessage, params);
			return builder.contentType(MediaType.TEXT_PLAIN).body(text);
		}
		Object response = builder().buildFailure(errCode, errMessage, params);
		return builder.body(response);
	}

	private ResponseEntity<?> buildResponseEntity(HttpStatus httpStatus, Object response) {
		BodyBuilder builder = ResponseEntity.status(httpStatus);
		return builder.body(response);
	}

	private ResponseEntity<?> buildResponseEntity(HttpStatus httpStatus, BaseException ex) {
		return this.buildNegotiatedResponse(httpStatus, ex.getErrCode(), ex.getErrMessage(), ex.getParams());
	}

	private ResponseEntity<?> buildResponseEntity(HttpStatus httpStatus, String errCode, String errMessage,
			Object... params) {
		return this.buildNegotiatedResponse(httpStatus, errCode, errMessage, params);
	}

}
