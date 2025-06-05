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

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import space.x9x.radp.extension.SPI;

/**
 * Rest 异常后置处理.
 *
 * @author IO x9x
 * @since 2024-09-27 10:44
 */
@SPI
public interface RestExceptionPostProcessor {

	/**
	 * 在HTTP请求中抛出错误后处理任何异常后的逻辑.
	 * @param request 包含客户端对servlet所做请求的HttpServletRequest对象
	 * @param response 包含servlet返回给客户端的响应的HttpServletResponse对象
	 * @param t 表示在请求处理过程中遇到的错误的Throwable对象
	 */
	void postProcess(HttpServletRequest request, HttpServletResponse response, Throwable t);

	/**
	 * 获取在HTTP请求中抛出的错误.
	 * @param request 包含客户端对servlet所做请求的HttpServletRequest对象
	 * @param response 包含servlet返回给客户端的响应的HttpServletResponse对象
	 * @return 在请求处理过程中遇到的Throwable对象
	 */
	Throwable getThrowable(HttpServletRequest request, HttpServletResponse response);

}
