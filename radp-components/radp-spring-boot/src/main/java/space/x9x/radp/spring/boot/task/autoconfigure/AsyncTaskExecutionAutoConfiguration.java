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

import java.util.concurrent.Executor;

import lombok.extern.slf4j.Slf4j;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration;
import org.springframework.boot.autoconfigure.task.TaskExecutionProperties;
import org.springframework.boot.task.ThreadPoolTaskExecutorBuilder;
import org.springframework.boot.task.ThreadPoolTaskExecutorCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Role;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;

import space.x9x.radp.spring.boot.task.TtlThreadPoolTaskExecutor;
import space.x9x.radp.spring.framework.task.interceptor.ExceptionHandlingAsyncTaskExecutor;

/**
 * Autoconfiguration for asynchronous task execution. This class configures a thread pool
 * task executor for handling asynchronous tasks in the application. It uses TTL
 * (TransmittableThreadLocal) to ensure context variables are properly propagated across
 * threads, and provides exception handling for async tasks. The configuration includes
 * limits on pool size and queue capacity to prevent resource exhaustion.
 *
 * @author x9x
 * @since 2024-09-30 11:52
 * @see org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration
 * @see org.springframework.boot.autoconfigure.task.TaskSchedulingAutoConfiguration
 */
@EnableAsync
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@AutoConfiguration
@Slf4j
public class AsyncTaskExecutionAutoConfiguration implements AsyncConfigurer {

	/**
	 * Log message for when the async task executor is autowired. This constant defines
	 * the message that is logged when the async task executor is created.
	 */
	private static final String AUTOWIRED_ASYNC_TASK_EXECUTION = "Autowired applicationTaskExecutor";

	/**
	 * Maximum thread pool size limit. This constant defines the maximum number of threads
	 * that can be created in the thread pool, based on the number of available
	 * processors.
	 */
	private static final int POOL_SIZE_LIMIT = Runtime.getRuntime().availableProcessors();

	/**
	 * Maximum queue capacity limit. This constant defines the maximum number of tasks
	 * that can be queued for execution when all threads are busy.
	 */
	private static final int QUEUE_CAPACITY_LIMIT = 10_000;

	/**
	 * Default bean name for the application task executor. This constant defines the name
	 * of the primary task executor bean that will be created by this autoconfiguration.
	 * It uses the same name as Spring Boot's default task executor to ensure
	 * compatibility with other components that expect this bean name.
	 */
	public static final String DEFAULT_TASK_EXECUTOR_BEAN_NAME = TaskExecutionAutoConfiguration.APPLICATION_TASK_EXECUTOR_BEAN_NAME;

	/**
	 * The task execution properties used to configure the executor. This field holds the
	 * properties that control thread pool settings such as core size, max size, queue
	 * capacity, and thread naming.
	 */
	private final TaskExecutionProperties properties;

	/**
	 * Provider for task executor customizers. This field allows additional customizations
	 * to be applied to the task executor beyond the standard configuration.
	 */
	private final ObjectProvider<ThreadPoolTaskExecutorCustomizer> taskExecutorCustomizers;

	/**
	 * Provider for task decorators. This field allows tasks to be decorated or wrapped
	 * before execution, enabling cross-cutting concerns like security context
	 * propagation.
	 */
	private final ObjectProvider<TaskDecorator> taskDecorators;

	/**
	 * Constructs a new AsyncTaskExecutionAutoConfiguration with the specified properties
	 * and customizers. This constructor initializes the autoconfiguration with task
	 * execution properties and providers for task executor customizers and decorators,
	 * which will be used to configure the task executor.
	 * @param properties the task execution properties to use for configuring the executor
	 * @param taskExecutorCustomizers provider for task executor customizers
	 * @param taskDecorators provider for task decorators
	 */
	public AsyncTaskExecutionAutoConfiguration(TaskExecutionProperties properties,
			ObjectProvider<ThreadPoolTaskExecutorCustomizer> taskExecutorCustomizers,
			ObjectProvider<TaskDecorator> taskDecorators) {
		log.debug("Autowired asyncTaskExecutionAutoConfiguration");
		this.properties = properties;
		this.taskExecutorCustomizers = taskExecutorCustomizers;
		this.taskDecorators = taskDecorators;
	}

	/**
	 * Creates and configures the async task executor. This method builds a thread pool
	 * task executor with appropriate settings for core size, max size, queue capacity,
	 * and other parameters based on the task execution properties. It applies limits to
	 * prevent resource exhaustion and wraps the executor with exception handling.
	 * @return a configured Executor for handling async tasks
	 */
	@Primary
	@Bean(name = DEFAULT_TASK_EXECUTOR_BEAN_NAME)
	@Override
	public Executor getAsyncExecutor() {
		log.debug(AUTOWIRED_ASYNC_TASK_EXECUTION);
		TaskExecutionProperties.Pool pool = this.properties.getPool();
		ThreadPoolTaskExecutorBuilder builder = new ThreadPoolTaskExecutorBuilder();

		if (pool.getCoreSize() > POOL_SIZE_LIMIT) {
			builder = builder.corePoolSize(POOL_SIZE_LIMIT);
		}
		else {
			builder = builder.corePoolSize(pool.getCoreSize());
		}

		if (pool.getMaxSize() > POOL_SIZE_LIMIT) {
			builder = builder.maxPoolSize(POOL_SIZE_LIMIT);
		}
		else {
			builder = builder.maxPoolSize(pool.getMaxSize());
		}

		// Note: Spring uses LinkedBlockingQueue unbounded blocking queue by default
		if (pool.getQueueCapacity() == Integer.MAX_VALUE) {
			builder = builder.queueCapacity(QUEUE_CAPACITY_LIMIT);
		}
		else {
			builder = builder.queueCapacity(pool.getQueueCapacity());
		}

		builder = builder.allowCoreThreadTimeOut(pool.isAllowCoreThreadTimeout());
		builder = builder.keepAlive(pool.getKeepAlive());
		TaskExecutionProperties.Shutdown shutdown = this.properties.getShutdown();
		builder = builder.awaitTermination(shutdown.isAwaitTermination());
		builder = builder.awaitTerminationPeriod(shutdown.getAwaitTerminationPeriod());
		builder = builder.threadNamePrefix(this.properties.getThreadNamePrefix());
		builder = builder.customizers(this.taskExecutorCustomizers.orderedStream()::iterator);
		builder = builder.taskDecorator(this.taskDecorators.getIfUnique());

		// 使用阿里巴巴 TTL 线程池
		TtlThreadPoolTaskExecutor taskExecutor = builder.configure(new TtlThreadPoolTaskExecutor());
		taskExecutor.initialize();
		// Spring 默认装配的 Bean 对异常的处理不是很友好, 需要替换
		return new ExceptionHandlingAsyncTaskExecutor(taskExecutor);
	}

	/**
	 * Provides an exception handler for uncaught exceptions in async methods. This method
	 * returns a handler that logs errors that occur during asynchronous method execution,
	 * including the method that was being executed and the exception that was thrown.
	 * @return an AsyncUncaughtExceptionHandler for handling exceptions in async methods
	 */
	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		return (ex, method, params) -> log.error("Unexpected exception occurred invoking async method: {}", method, ex);
	}

}
