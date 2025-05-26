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

package space.x9x.radp.spring.boot.task;

import com.alibaba.ttl.TtlCallable;
import com.alibaba.ttl.TtlRunnable;
import org.jetbrains.annotations.NotNull;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * TTL 线程池执行器
 * <p>
 * 该执行器扩展了Spring的ThreadPoolTaskExecutor，并使用阿里巴巴的TTL（TransmittableThreadLocal）
 * 来确保ThreadLocal变量能够在线程池中正确传递。这对于需要在异步执行上下文中
 * 保持上下文信息（如用户身份、请求跟踪ID等）的应用程序非常重要。
 * <p>
 * 该类重写了所有任务提交方法，确保每个任务都被TTL包装，从而实现ThreadLocal值的传递。
 *
 * @author IO x9x
 * @since 2024-09-30 12:04
 */
public class TtlThreadPoolTaskExecutor extends ThreadPoolTaskExecutor {

    private static final long serialVersionUID = 1L;

    /**
     * 用于空值检查的错误消息
     */
    private static final String MESSAGE = "Task is not null";

    /**
     * 执行Runnable任务
     * <p>
     * 将任务包装为TTL任务，确保ThreadLocal值能够传递到执行线程
     *
     * @param task 要执行的任务
     */
    @Override
    public void execute(@NotNull Runnable task) {
        super.execute(Objects.requireNonNull(TtlRunnable.get(task), MESSAGE));
    }

    /**
     * 使用启动超时执行Runnable任务
     * <p>
     * 将任务包装为TTL任务，确保ThreadLocal值能够传递到执行线程
     *
     * @param task         要执行的任务
     * @param startTimeout 启动超时时间（毫秒）
     * @deprecated 此方法在AsyncTaskExecutor接口中已被弃用，建议使用其他方法替代
     */
    @Override
    @Deprecated
    public void execute(@NotNull Runnable task, long startTimeout) {
        super.execute(Objects.requireNonNull(TtlRunnable.get(task), MESSAGE), startTimeout);
    }

    /**
     * 提交Runnable任务
     * <p>
     * 将任务包装为TTL任务，确保ThreadLocal值能够传递到执行线程
     *
     * @param task 要执行的任务
     * @return 表示任务挂起完成的Future
     */
    @Override
    public @NotNull Future<?> submit(@NotNull Runnable task) {
        return super.submit(Objects.requireNonNull(TtlRunnable.get(task), MESSAGE));
    }

    /**
     * 提交Callable任务
     * <p>
     * 将任务包装为TTL任务，确保ThreadLocal值能够传递到执行线程
     *
     * @param task 要执行的任务
     * @param <T>  结果类型
     * @return 表示任务挂起完成的Future
     */
    @Override
    public <T> @NotNull Future<T> submit(@NotNull Callable<T> task) {
        return super.submit(Objects.requireNonNull(TtlCallable.get(task), MESSAGE));
    }

    @Override
    public ListenableFuture<?> submitListenable(Runnable task) {
        return super.submitListenable(Objects.requireNonNull(TtlRunnable.get(task), MESSAGE));
    }

    @Override
    public <T> ListenableFuture<T> submitListenable(Callable<T> task) {
        return super.submitListenable(Objects.requireNonNull(TtlCallable.get(task), MESSAGE));
    }
}
