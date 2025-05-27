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

package space.x9x.radp.spring.framework.task.interceptor;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.task.AsyncTaskExecutor;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

/**
 * exception handling wrapper for asynchronous task executors.
 * <p>
 * this class wraps an AsyncTaskExecutor and adds exception handling functionality for
 * asynchronous task execution. when exceptions are thrown during task execution, they are
 * processed through the handle method, ensuring exceptions are properly logged rather
 * than silently swallowed.
 * <p>
 * this class also implements InitializingBean and DisposableBean interfaces to ensure the
 * wrapped executor's lifecycle methods are properly called.
 *
 * @author IO x9x
 * @since 2024-09-30 11:43
 */
@Slf4j
public class ExceptionHandlingAsyncTaskExecutor implements AsyncTaskExecutor, InitializingBean, DisposableBean {

	/**
	 * log message template for async exceptions. this constant defines the message format
	 * used when logging exceptions caught during asynchronous task execution.
	 */
	private static final String MSG_ASYNC_EXCEPTION = "AsyncTaskExecutor exec caught exception: {}";

	/**
	 * the wrapped async task executor. this is the underlying executor that will actually
	 * execute the tasks after they've been wrapped with exception handling.
	 */
	private final AsyncTaskExecutor executor;

	/**
	 * Constructs a new ExceptionHandlingAsyncTaskExecutor with the specified executor.
	 * This wrapper adds exception handling capabilities to the provided
	 * AsyncTaskExecutor, ensuring that exceptions thrown during asynchronous task
	 * execution are properly logged.
	 * @param executor the AsyncTaskExecutor to be wrapped with exception handling
	 */
	public ExceptionHandlingAsyncTaskExecutor(AsyncTaskExecutor executor) {
		this.executor = executor;
	}

	@Override
	public void destroy() throws Exception {
		if (this.executor instanceof DisposableBean) {
			((DisposableBean) this.executor).destroy();
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (this.executor instanceof InitializingBean) {
			((InitializingBean) this.executor).afterPropertiesSet();
		}
	}

	/**
	 * 使用启动超时执行Runnable任务.
	 * <p>
	 * 将任务包装为异常处理任务，并委托给底层执行器执行
	 * @param task 要执行的任务
	 * @param startTimeout 启动超时时间（毫秒）
	 * @deprecated 此方法在AsyncTaskExecutor接口中已被弃用，建议使用其他方法替代
	 */
	@Override
	@Deprecated
	public void execute(@NotNull Runnable task, long startTimeout) {
		this.executor.execute(createWrappedRunnable(task), startTimeout);
	}

	/**
	 * 提交Runnable任务.
	 * <p>
	 * 将任务包装为异常处理任务，并委托给底层执行器执行
	 * @param task 要执行的任务
	 * @return 表示任务挂起完成的Future
	 */
	@Override
	public @NotNull Future<?> submit(@NotNull Runnable task) {
		return this.executor.submit(createWrappedRunnable(task));
	}

	/**
	 * 提交Callable任务.
	 * <p>
	 * 将任务包装为异常处理任务，并委托给底层执行器执行
	 * @param task 要执行的任务
	 * @param <T> 结果类型
	 * @return 表示任务挂起完成的Future
	 */
	@Override
	public <T> @NotNull Future<T> submit(@NotNull Callable<T> task) {
		return this.executor.submit(createCallable(task));
	}

	/**
	 * 执行Runnable任务.
	 * <p>
	 * 将任务包装为异常处理任务，并委托给底层执行器执行
	 * @param task 要执行的任务
	 */
	@Override
	public void execute(@NotNull Runnable task) {
		this.executor.execute(createWrappedRunnable(task));
	}

	/**
	 * 创建带有异常处理的Callable任务.
	 * <p>
	 * 包装原始Callable任务，添加异常处理逻辑
	 * @param task 原始任务
	 * @param <T> 结果类型
	 * @return 包装后的Callable任务
	 */
	private <T> Callable<T> createCallable(final Callable<T> task) {
		return () -> {
			try {
				return task.call();
			}
			catch (Exception ex) {
				handle(ex);
				throw ex;
			}
		};
	}

	/**
	 * 创建带有异常处理的Runnable任务.
	 * <p>
	 * 包装原始Runnable任务，添加异常处理逻辑
	 * @param task 原始任务
	 * @return 包装后的Runnable任务
	 */
	private Runnable createWrappedRunnable(final Runnable task) {
		return () -> {
			try {
				task.run();
			}
			catch (Exception ex) {
				handle(ex);
			}
		};
	}

	/**
	 * 处理异步任务执行过程中发生的异常.
	 * <p>
	 * 当执行任务时捕获到异常时，会调用此方法。 它使用配置的日志记录器记录异常消息和异常本身。
	 * @param e 任务执行过程中捕获的异常
	 */
	protected void handle(Exception e) {
		log.error(MSG_ASYNC_EXCEPTION, e.getMessage(), e);
	}

}
