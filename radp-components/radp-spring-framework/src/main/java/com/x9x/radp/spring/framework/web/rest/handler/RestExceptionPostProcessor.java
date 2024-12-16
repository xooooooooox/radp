package com.x9x.radp.spring.framework.web.rest.handler;

import com.x9x.radp.extension.SPI;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Rest 异常后置处理
 *
 * @author x9x
 * @since 2024-09-27 10:44
 */
@SPI
public interface RestExceptionPostProcessor {

    /**
     * 在HTTP请求中抛出错误后处理任何异常后的逻辑。
     *
     * @param request  包含客户端对servlet所做请求的HttpServletRequest对象
     * @param response 包含servlet返回给客户端的响应的HttpServletResponse对象
     * @param t        表示在请求处理过程中遇到的错误的Throwable对象
     */
    void postProcess(HttpServletRequest request, HttpServletResponse response, Throwable t);

    /**
     * 获取在HTTP请求中抛出的错误。
     *
     * @param request  包含客户端对servlet所做请求的HttpServletRequest对象
     * @param response 包含servlet返回给客户端的响应的HttpServletResponse对象
     * @return 在请求处理过程中遇到的Throwable对象
     */
    Throwable getThrowable(HttpServletRequest request, HttpServletResponse response);
}
