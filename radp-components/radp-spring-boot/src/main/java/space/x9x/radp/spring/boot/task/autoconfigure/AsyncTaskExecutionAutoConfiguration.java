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

package space.x9x.radp.spring.boot.task.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration;
import org.springframework.boot.autoconfigure.task.TaskExecutionProperties;
import org.springframework.boot.task.TaskExecutorBuilder;
import org.springframework.boot.task.TaskExecutorCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Role;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import space.x9x.radp.spring.boot.task.TtlThreadPoolTaskExecutor;
import space.x9x.radp.spring.framework.task.interceptor.ExceptionHandlingAsyncTaskExecutor;

import java.util.concurrent.Executor;

/**
 * 异步任务执行器自动装配
 *
 * @author x9x
 * @see org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration
 * @see org.springframework.boot.autoconfigure.task.TaskSchedulingAutoConfiguration
 * @since 2024-09-30 11:52
 */
@EnableAsync
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@AutoConfiguration
@Slf4j
public class AsyncTaskExecutionAutoConfiguration implements AsyncConfigurer {

    private static final String AUTOWIRED_ASYNC_TASK_EXECUTION = "Autowired applicationTaskExecutor";
    private static final int POOL_SIZE_LIMIT = Runtime.getRuntime().availableProcessors();
    private static final int QUEUE_CAPACITY_LIMIT = 10_000;

    /**
     * Default bean name for the application task executor.
     * This constant defines the name of the primary task executor bean that will be created
     * by this auto-configuration.
     * It uses the same name as Spring Boot's default task executor
     * to ensure compatibility with other components that expect this bean name.
     */
    public static final String DEFAULT_TASK_EXECUTOR_BEAN_NAME = TaskExecutionAutoConfiguration.APPLICATION_TASK_EXECUTOR_BEAN_NAME;

    private final TaskExecutionProperties properties;
    private final ObjectProvider<TaskExecutorCustomizer> taskExecutorCustomizers;
    private final ObjectProvider<TaskDecorator> taskDecorators;

    /**
     * Constructs a new AsyncTaskExecutionAutoConfiguration with the specified properties and customizers.
     * This constructor initializes the auto-configuration with task execution properties and providers
     * for task executor customizers and decorators, which will be used to configure the task executor.
     *
     * @param properties              the task execution properties to use for configuring the executor
     * @param taskExecutorCustomizers provider for task executor customizers
     * @param taskDecorators          provider for task decorators
     */
    public AsyncTaskExecutionAutoConfiguration(TaskExecutionProperties properties,
                                               ObjectProvider<TaskExecutorCustomizer> taskExecutorCustomizers,
                                               ObjectProvider<TaskDecorator> taskDecorators) {
        log.debug("Autowired asyncTaskExecutionAutoConfiguration");
        this.properties = properties;
        this.taskExecutorCustomizers = taskExecutorCustomizers;
        this.taskDecorators = taskDecorators;
    }

    @Primary
    @Bean(name = DEFAULT_TASK_EXECUTOR_BEAN_NAME)
    @Override
    public Executor getAsyncExecutor() {
        log.debug(AUTOWIRED_ASYNC_TASK_EXECUTION);
        TaskExecutionProperties.Pool pool = properties.getPool();
        TaskExecutorBuilder builder = new TaskExecutorBuilder();

        if (pool.getCoreSize() > POOL_SIZE_LIMIT) {
            builder = builder.corePoolSize(POOL_SIZE_LIMIT);
        } else {
            builder = builder.corePoolSize(pool.getCoreSize());
        }

        if (pool.getMaxSize() > POOL_SIZE_LIMIT) {
            builder = builder.maxPoolSize(POOL_SIZE_LIMIT);
        } else {
            builder = builder.maxPoolSize(pool.getMaxSize());
        }

        // 注意: Spring 默认使用 LinkedBlockingQueue 无界阻塞队列
        if (pool.getQueueCapacity() == Integer.MAX_VALUE) {
            builder = builder.queueCapacity(QUEUE_CAPACITY_LIMIT);
        } else {
            builder = builder.queueCapacity(pool.getQueueCapacity());
        }

        builder = builder.allowCoreThreadTimeOut(pool.isAllowCoreThreadTimeout());
        builder = builder.keepAlive(pool.getKeepAlive());
        TaskExecutionProperties.Shutdown shutdown = properties.getShutdown();
        builder = builder.awaitTermination(shutdown.isAwaitTermination());
        builder = builder.awaitTerminationPeriod(shutdown.getAwaitTerminationPeriod());
        builder = builder.threadNamePrefix(properties.getThreadNamePrefix());
        builder = builder.customizers(taskExecutorCustomizers.orderedStream()::iterator);
        builder = builder.taskDecorator(taskDecorators.getIfUnique());

        // 使用阿里巴巴 TTL 线程池
        TtlThreadPoolTaskExecutor taskExecutor = builder.configure(new TtlThreadPoolTaskExecutor());
        taskExecutor.initialize();
        // Spring 默认装配的 Bean 对异常的处理不是很友好, 需要替换
        return new ExceptionHandlingAsyncTaskExecutor(taskExecutor);
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (ex, method, params) -> log.error("Unexpected exception occurred invoking async method: {}", method, ex);
    }
}
