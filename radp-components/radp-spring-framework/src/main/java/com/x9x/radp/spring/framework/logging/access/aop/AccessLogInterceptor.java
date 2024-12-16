package com.x9x.radp.spring.framework.logging.access.aop;

import com.x9x.radp.spring.framework.logging.access.config.AccessLogConfig;
import com.x9x.radp.spring.framework.logging.access.util.AccessLogHelper;
import lombok.RequiredArgsConstructor;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.aop.support.AopUtils;

import java.time.Duration;
import java.time.Instant;

/**
 * @author x9x
 * @since 2024-09-30 09:52
 */
@RequiredArgsConstructor
public class AccessLogInterceptor implements MethodInterceptor {

    private final AccessLogConfig accessLogConfig;

    @Override
    public @Nullable Object invoke(@NotNull MethodInvocation invocation) throws Throwable {
        // 排除代理类
        if (AopUtils.isAopProxy(invocation.getThis())) {
            return invocation.proceed();
        }

        // 判断是否需要输出日志
        if (AccessLogHelper.shouldLog(accessLogConfig.getSampleRate())) {
            return invocation.proceed();
        }

        Instant start = Instant.now();
        Object result = null;
        Throwable throwable = null;
        try {
            result = invocation.proceed();
            return result;
        } catch (Throwable t) {
            throwable = t;
            throw throwable;
        } finally {
            long duration = Duration.between(start, Instant.now()).toMillis();
            AccessLogHelper.log(invocation, result, throwable, duration, accessLogConfig.isEnabledMdc(), accessLogConfig.getMaxLength(), accessLogConfig.getSlowThreshold());
        }
    }
}
