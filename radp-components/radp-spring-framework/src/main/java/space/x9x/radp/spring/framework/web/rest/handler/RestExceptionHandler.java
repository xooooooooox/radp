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

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
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

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import space.x9x.radp.commons.collections.CollectionUtils;
import space.x9x.radp.commons.lang.StringUtils;
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
 * @author IO x9x
 * @since 2024-09-26 23:52
 */
@RestControllerAdvice
@Slf4j
public class RestExceptionHandler {
    private static final String EXCEPTION_HANDLER_CATCH = "@RestControllerAdvice catch exception: {}";

    /**
     * Handles general exceptions that are not caught by more specific handlers.
     *
     * @param ex the exception
     * @return a response entity with error details
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> resolveException(Exception ex) {
        log.error(EXCEPTION_HANDLER_CATCH, ex.getMessage(), ex);
        BodyBuilder builder;
        Object response;
        ResponseStatus responseStatus = AnnotationUtils.findAnnotation(ex.getClass(), ResponseStatus.class);
        if (responseStatus != null) {
            builder = ResponseEntity.status(responseStatus.value());
            response = builder().buildFailure("500", responseStatus.reason());
        } else {
            builder = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
            response = builder().buildFailure("500", ex.getMessage());
        }
        this.postProcess(ex);
        return builder.body(response);
    }

    /**
     * Handles validation exceptions thrown when method arguments fail validation.
     *
     * @param ex the validation exception
     * @return a response entity with validation error details
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> resolveValidationException(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        String message = StringUtils.join(fieldErrors.toArray(), ",");
        Object response = builder().buildFailure("400", message);
        return this.buildResponseEntity(HttpStatus.BAD_REQUEST, response);
    }

    /**
     * Handles exceptions thrown when method argument types don't match the expected types.
     *
     * @param ex the method argument type mismatch exception
     * @return a response entity with error details
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> resolveMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        log.warn(EXCEPTION_HANDLER_CATCH, ex.getMessage(), ex);
        Object response = builder().buildFailure("400", "请求参数类型错误{}", ex.getMessage());
        return this.buildResponseEntity(HttpStatus.BAD_REQUEST, response);
    }

    /**
     * Handles exceptions thrown when an HTTP request method is not supported.
     *
     * @param ex the HTTP request method didn't support exception
     * @return a response entity with a method didn't allow error details
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<?> resolveMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        log.warn(EXCEPTION_HANDLER_CATCH, ex.getMessage(), ex);
        Object response = builder().buildFailure(GlobalResponseCode.METHOD_NOT_ALLOWED.getErrorCode());
        return this.buildResponseEntity(HttpStatus.METHOD_NOT_ALLOWED, response);
    }


    /**
     * Handles exceptions thrown when a requested resource is not found.
     *
     * @param ex no resource found exception
     * @return a response entity with not found error details
     */
    @ExceptionHandler(value = NoResourceFoundException.class)
    public ResponseEntity<?> resolveNoResourceFoundException(NoResourceFoundException ex) {
        log.warn(EXCEPTION_HANDLER_CATCH, ex.getMessage());
        Object response = builder().buildFailure(GlobalResponseCode.NOT_FOUND.getErrorCode());
        return this.buildResponseEntity(HttpStatus.NOT_FOUND, response);
    }

    /**
     * Handles bad request exceptions when the client sends an invalid request.
     *
     * @param ex the bad request exception
     * @return a response entity with bad request error details
     */
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> resolveBadRequestException(BadRequestException ex) {
        return this.buildResponseEntity(HttpStatus.BAD_REQUEST, ex);
    }

    /**
     * Handles unauthorized exceptions when a user is not authenticated.
     *
     * @param ex the unauthorized exception
     * @return a response entity with unauthorized error details
     */
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<?> resolveUnauthorizedException(UnauthorizedException ex) {
        return this.buildResponseEntity(HttpStatus.UNAUTHORIZED, ex);
    }

    /**
     * Handles forbidden exceptions when a user is not authorized to access a resource.
     *
     * @param ex the forbidden exception
     * @return a response entity with forbidden error details
     */
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<?> resolveForbiddenException(ForbiddenException ex) {
        return this.buildResponseEntity(HttpStatus.FORBIDDEN, ex);
    }

    /**
     * Handles client exceptions for errors caused by client-side issues.
     *
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
     *
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
     *
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
        ExtensionLoader<RestExceptionPostProcessor> extensionLoader = ExtensionLoader.getExtensionLoader(RestExceptionPostProcessor.class);
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

    private ResponseEntity<?> buildResponseEntity(HttpStatus httpStatus, Object response) {
        BodyBuilder builder = ResponseEntity.status(httpStatus);
        return builder.body(response);
    }

    private ResponseEntity<?> buildResponseEntity(HttpStatus httpStatus, BaseException ex) {
        Object response = builder().buildFailure(ex.getErrCode(), ex.getErrMessage(), ex.getParams());
        return this.buildResponseEntity(httpStatus, response);
    }
}
