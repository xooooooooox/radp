package com.x9x.radp.spring.framework.web.rest.handler;

import com.x9x.radp.commons.collections.CollectionUtils;
import com.x9x.radp.commons.lang.StringUtils;
import com.x9x.radp.extension.ExtensionLoader;
import com.x9x.radp.spring.framework.error.BaseException;
import com.x9x.radp.spring.framework.error.ClientException;
import com.x9x.radp.spring.framework.error.ServerException;
import com.x9x.radp.spring.framework.error.ThirdServiceException;
import com.x9x.radp.spring.framework.error.http.BadRequestException;
import com.x9x.radp.spring.framework.error.http.ForbiddenException;
import com.x9x.radp.spring.framework.error.http.UnauthorizedException;
import com.x9x.radp.spring.framework.web.extension.ResponseBuilder;
import com.x9x.radp.spring.framework.web.util.ServletUtils;
import lombok.extern.slf4j.Slf4j;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Set;

/**
 * @author x9x
 * @since 2024-09-26 23:52
 */
@RestControllerAdvice
@Slf4j
public class RestExceptionHandler {
    private static final String EXCEPTION_HANDLER_CATCH = "@RestControllerAdvice catch exception: {}";

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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> resolveValidationException(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        String message = StringUtils.join(fieldErrors.toArray(), ",");
        Object response = builder().buildFailure("400", message);
        return this.buildResponseEntity(HttpStatus.BAD_REQUEST, response);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<?> resolveMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        Object response = builder().buildFailure("400", "不支持的请求方法");
        return this.buildResponseEntity(HttpStatus.METHOD_NOT_ALLOWED, response);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> resolveBadRequestException(BadRequestException ex) {
        return this.buildResponseEntity(HttpStatus.BAD_REQUEST, ex);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<?> resolveUnauthorizedException(UnauthorizedException ex) {
        return this.buildResponseEntity(HttpStatus.UNAUTHORIZED, ex);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<?> resolveForbiddenException(ForbiddenException ex) {
        return this.buildResponseEntity(HttpStatus.FORBIDDEN, ex);
    }

    @ExceptionHandler(ClientException.class)
    public ResponseEntity<?> resolveClientException(ClientException ex) {
        log.warn(EXCEPTION_HANDLER_CATCH, ex.getMessage(), ex);
        this.postProcess(ex);
        return this.buildResponseEntity(HttpStatus.BAD_REQUEST, ex);
    }

    @ExceptionHandler(ServerException.class)
    public ResponseEntity<?> resolveServerException(ServerException ex) {
        log.error(EXCEPTION_HANDLER_CATCH, ex.getMessage(), ex);
        this.postProcess(ex);
        return this.buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, ex);
    }

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
