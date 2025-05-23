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

package space.x9x.radp.spring.cloud.dubbo.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.common.utils.ReflectUtils;
import org.apache.dubbo.common.utils.StringUtils;
import org.apache.dubbo.rpc.*;
import org.apache.dubbo.rpc.service.GenericService;
import space.x9x.radp.spring.framework.error.BaseException;

import java.lang.reflect.Method;

/**
 * Dubbo 自定义异常过滤器
 * <p>
 * 该过滤器用于处理Dubbo服务调用过程中产生的异常，主要功能包括：
 * 1. 对不同类型的异常进行分类处理
 * 2. 对未在方法签名中声明的异常进行日志记录
 * 3. 对未知异常进行包装，避免客户端无法反序列化
 * 4. 对调用过程中的错误进行日志记录
 * <p>
 * 过滤器通过实现{@link Filter}和{@link BaseFilter.Listener}接口，
 * 在Dubbo的调用链中拦截和处理异常，提高系统的稳定性和可维护性。
 *
 * @author x9x
 * @since 2024-10-01 23:08
 */
@Activate(group = CommonConstants.PROVIDER, order = DubboExceptionFilter.ORDER)
@Slf4j
public class DubboExceptionFilter implements Filter, BaseFilter.Listener {

    /**
     * 定义此过滤器在Dubbo过滤器链中的执行顺序
     * <p>
     * 较低的值表示更高的优先级。值-1确保此过滤器
     * 在过滤器链中较早运行，以便正确处理异常。
     */
    public static final int ORDER = -1;

    /**
     * 构造一个新的DubboExceptionFilter实例
     * <p>
     * 此构造函数初始化过滤器并记录一条消息，
     * 表明Dubbo3异常过滤器已初始化。
     */
    public DubboExceptionFilter() {
        log.info("Dubbo3 异常过滤器已初始化");
    }

    /**
     * 执行服务调用
     * <p>
     * 该方法在服务调用前被调用，本过滤器不修改调用过程，
     * 仅将调用传递给下一个过滤器或最终的服务实现。
     *
     * @param invoker    调用者
     * @param invocation 调用信息
     * @return 调用结果
     * @throws RpcException 如果调用过程中发生RPC异常
     */
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        return invoker.invoke(invocation);
    }

    /**
     * 处理服务响应
     * <p>
     * 该方法在服务提供者返回响应后被调用，用于处理响应中的异常。
     * 根据异常类型和上下文决定是直接抛出异常还是包装后再抛出。
     *
     * @param appResponse 应用响应
     * @param invoker     调用者
     * @param invocation  调用信息
     */
    @Override
    public void onResponse(Result appResponse, Invoker<?> invoker, Invocation invocation) {
        // 只处理非泛化调用且包含异常的响应
        if (appResponse.hasException() && GenericService.class != invoker.getInterface()) {
            try {
                Throwable exception = appResponse.getException();

                // 如果异常应该直接抛出，则不做处理
                if (shouldDirectlyThrow(exception, invoker, invocation)) {
                    return;
                }

                // 记录未在方法签名中声明的异常
                logUndeclaredException(exception, invoker, invocation);

                // 包装未知异常并返回给客户端
                wrapUnknownException(appResponse, exception, invoker);
            } catch (Exception e) {
                // 处理异常过滤器本身出现的异常
                logFilterException(e, invoker, invocation);
            }
        }
    }

    /**
     * 判断异常是否应该直接抛出
     * <p>
     * 以下情况的异常会直接抛出：
     * 1. 检查型异常（非RuntimeException的Exception）
     * 2. 在方法签名中声明的异常
     * 3. 与接口在同一个JAR文件中的异常
     * 4. JDK内置异常（java.* 或 javax.*包中的异常）
     * 5. 自定义基础异常（BaseException）
     * 6. RPC异常（RpcException）
     *
     * @param exception  异常对象
     * @param invoker    调用者
     * @param invocation 调用信息
     * @return 如果应该直接抛出则返回true，否则返回false
     */
    private boolean shouldDirectlyThrow(Throwable exception, Invoker<?> invoker, Invocation invocation) {
        // 检查型异常直接抛出
        if (!(exception instanceof RuntimeException) && (exception instanceof Exception)) {
            return true;
        }

        // 在方法签名中声明的异常直接抛出
        if (isDeclaredInMethodSignature(exception, invoker, invocation)) {
            return true;
        }

        // 与接口在同一个JAR文件中的异常直接抛出
        String serviceFile = ReflectUtils.getCodeBase(invoker.getInterface());
        String exceptionFile = ReflectUtils.getCodeBase(exception.getClass());
        if (serviceFile == null || exceptionFile == null || serviceFile.equals(exceptionFile)) {
            return true;
        }

        // JDK内置异常直接抛出
        String className = exception.getClass().getName();
        if (className.startsWith("java.") || className.startsWith("javax.")) {
            return true;
        }

        // 自定义基础异常直接抛出
        if (exception instanceof BaseException) {
            return true;
        }

        // RPC异常直接抛出
        return exception instanceof RpcException;
    }

    /**
     * 判断异常是否在方法签名中声明
     *
     * @param exception  异常对象
     * @param invoker    调用者
     * @param invocation 调用信息
     * @return 如果异常在方法签名中声明则返回true，否则返回false
     */
    private boolean isDeclaredInMethodSignature(Throwable exception, Invoker<?> invoker, Invocation invocation) {
        try {
            Method method = invoker.getInterface().getMethod(invocation.getMethodName(), invocation.getParameterTypes());
            Class<?>[] exceptionClasses = method.getExceptionTypes();
            for (Class<?> exceptionClass : exceptionClasses) {
                if (exception.getClass().equals(exceptionClass)) {
                    return true;
                }
            }
            return false;
        } catch (NoSuchMethodException e) {
            // 如果找不到方法，则认为异常不在方法签名中
            return true; // 返回true以避免进一步处理
        }
    }

    /**
     * 记录未在方法签名中声明的异常
     *
     * @param exception  异常对象
     * @param invoker    调用者
     * @param invocation 调用信息
     */
    private void logUndeclaredException(Throwable exception, Invoker<?> invoker, Invocation invocation) {
        log.error("捕获到未在方法签名中声明的异常，调用方: {}, 服务: {}, 方法: {}, 异常类型: {}: {}",
                RpcContext.getServerContext().getRemoteHost(),
                invoker.getInterface().getName(),
                invocation.getMethodName(),
                exception.getClass().getName(),
                exception.getMessage(),
                exception);
    }

    /**
     * 包装未知异常并返回给客户端
     *
     * @param appResponse 应用响应
     * @param exception   异常对象
     * @param invoker     调用者
     */
    private void wrapUnknownException(Result appResponse, Throwable exception, Invoker<?> invoker) {
        // 将未知异常包装为RuntimeException并返回给客户端
        appResponse.setException(new RuntimeException(StringUtils.toString(exception)));
    }

    /**
     * 记录异常过滤器本身出现的异常
     *
     * @param e          异常对象
     * @param invoker    调用者
     * @param invocation 调用信息
     */
    private void logFilterException(Throwable e, Invoker<?> invoker, Invocation invocation) {
        log.warn("异常过滤器处理失败，调用方: {}, 服务: {}, 方法: {}, 异常类型: {}: {}",
                RpcContext.getServerContext().getRemoteHost(),
                invoker.getInterface().getName(),
                invocation.getMethodName(),
                e.getClass().getName(),
                e.getMessage(),
                e);
    }

    /**
     * 处理调用过程中发生的错误
     * <p>
     * 该方法在调用过程中发生错误时被调用，用于记录错误信息。
     * 与onResponse不同，onError处理的是在RPC调用过程中发生的错误，
     * 而不是服务执行过程中抛出的业务异常。
     *
     * @param t          错误对象
     * @param invoker    调用者
     * @param invocation 调用信息
     */
    @Override
    public void onError(Throwable t, Invoker<?> invoker, Invocation invocation) {
        log.error("调用过程中发生未捕获的异常，调用方: {}, 服务: {}, 方法: {}, 异常类型: {}: {}",
                RpcContext.getServerContext().getRemoteHost(),
                invoker.getInterface().getName(),
                invocation.getMethodName(),
                t.getClass().getName(),
                t.getMessage(),
                t);
    }
}
