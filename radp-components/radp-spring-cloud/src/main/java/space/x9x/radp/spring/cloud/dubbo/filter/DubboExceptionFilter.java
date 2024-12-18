package space.x9x.radp.spring.cloud.dubbo.filter;

import space.x9x.radp.spring.framework.error.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.common.utils.ReflectUtils;
import org.apache.dubbo.common.utils.StringUtils;
import org.apache.dubbo.rpc.*;
import org.apache.dubbo.rpc.service.GenericService;

import java.lang.reflect.Method;

/**
 * Dubbo 自定义异常过滤器
 *
 * @author x9x
 * @since 2024-10-01 23:08
 */
@Activate(group = CommonConstants.PROVIDER, order = DubboExceptionFilter.ORDER)
@Slf4j
public class DubboExceptionFilter implements Filter, BaseFilter.Listener {

    public static final int ORDER = -1;

    public DubboExceptionFilter() {
        log.info("Dubbo3 exception filter initialized");
    }

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        return invoker.invoke(invocation);
    }

    @Override
    public void onResponse(Result appResponse, Invoker<?> invoker, Invocation invocation) {
        if (appResponse.hasException() && GenericService.class != invoker.getInterface()) {
            try {
                Throwable exception = appResponse.getException();

                // directly throw if it's checked exception
                if (!(exception instanceof RuntimeException) && (exception instanceof Exception)) {
                    return;
                }

                // directly throw if the exception appears in the signature
                try {
                    Method method = invoker.getInterface().getMethod(invocation.getMethodName(), invocation.getParameterTypes());
                    Class<?>[] exceptionClasses = method.getExceptionTypes();
                    for (Class<?> exceptionClass : exceptionClasses) {
                        if (exception.getClass().equals(exceptionClass)) {
                            return;
                        }
                    }
                } catch (NoSuchMethodException e) {
                    return;
                }

                // for the exception not found in method's signature, print ERROR message in server's log
                log.error("Got unchecked and undeclared exception which called by {}, service: {}, method: {}, exception: {}: {}",
                        RpcContext.getServerContext().getRemoteHost(),
                        invoker.getInterface().getName(),
                        invocation.getMethodName(),
                        exception.getClass().getName(),
                        exception.getMessage(),
                        exception);

                // directly throw if exception class and interface class are in the same jar file
                String serviceFile = ReflectUtils.getCodeBase(invoker.getInterface());
                String exceptionFile = ReflectUtils.getCodeBase(exception.getClass());
                if (serviceFile == null || exceptionFile == null || serviceFile.equals(exceptionFile)) {
                    return;
                }

                // directly throw if it's JDK exception
                String className = exception.getClass().getName();
                if (className.startsWith("java.") || className.startsWith("javax.")) {
                    return;
                }
                // directly throw if it's custom exception
                if (exception instanceof BaseException) {
                    return;
                }
                if (exception instanceof RpcException) {
                    return;
                }

                // otherwise, wrap with RuntimeException and throw back to the client
                appResponse.setException(new RuntimeException(StringUtils.toString(exception)));
            } catch (Throwable e) {
                log.warn("Fail to ExceptionFilter when called by {}. service: {}, method: {}, exception: {}: {}",
                        RpcContext.getServerContext().getRemoteHost(),
                        invoker.getInterface().getName(),
                        invocation.getMethodName(),
                        e.getClass().getName(),
                        e.getMessage(),
                        e);
            }
        }
    }

    @Override
    public void onError(Throwable t, Invoker<?> invoker, Invocation invocation) {
        log.error("Got unchecked and undeclared exception which called by {}, service: {}, method: {}, exception: {}: {}",
                RpcContext.getServerContext().getRemoteHost(),
                invoker.getInterface().getName(),
                invocation.getMethodName(),
                t.getClass().getName(),
                t.getMessage(),
                t);
    }
}
