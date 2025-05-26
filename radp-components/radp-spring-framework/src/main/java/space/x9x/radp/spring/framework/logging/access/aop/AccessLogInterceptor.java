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

package space.x9x.radp.spring.framework.logging.access.aop;

import space.x9x.radp.spring.framework.logging.access.config.AccessLogConfig;
import space.x9x.radp.spring.framework.logging.access.util.AccessLogHelper;
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
