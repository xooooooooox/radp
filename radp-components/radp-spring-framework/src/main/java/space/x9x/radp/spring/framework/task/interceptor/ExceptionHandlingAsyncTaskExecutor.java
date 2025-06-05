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
	 * Executes a Runnable task with a startup timeout.
	 * <p>
	 * Wraps the task as a TTL task to ensure ThreadLocal values are passed to the
	 * execution thread.
	 * @param task the task to execute
	 * @param startTimeout the startup timeout in milliseconds
	 * @deprecated this method is deprecated in the AsyncTaskExecutor interface, use
	 * alternative methods instead
	 */
	@Override
	@Deprecated
	public void execute(@NotNull Runnable task, long startTimeout) {
		this.executor.execute(createWrappedRunnable(task), startTimeout);
	}

	/**
	 * Submits a Runnable task for execution.
	 * <p>
	 * Wraps the task as a TTL task to ensure ThreadLocal values are passed to the
	 * execution thread.
	 * @param task the task to submit
	 * @return a Future representing pending completion of the task
	 */
	@Override
	public @NotNull Future<?> submit(@NotNull Runnable task) {
		return this.executor.submit(createWrappedRunnable(task));
	}

	/**
	 * Submits a Callable task for execution.
	 * <p>
	 * Wraps the task as a TTL task to ensure ThreadLocal values are passed to the
	 * execution thread.
	 * @param task the task to submit
	 * @param <T> the result type
	 * @return a Future representing pending completion of the task
	 */
	@Override
	public <T> @NotNull Future<T> submit(@NotNull Callable<T> task) {
		return this.executor.submit(createCallable(task));
	}

	/**
	 * Executes a Runnable task.
	 * <p>
	 * Wraps the task as a TTL task to ensure ThreadLocal values are passed to the
	 * execution thread.
	 * @param task the task to execute
	 */
	@Override
	public void execute(@NotNull Runnable task) {
		this.executor.execute(createWrappedRunnable(task));
	}

	/**
	 * Creates a Callable task with exception handling.
	 * <p>
	 * Wraps the original Callable task with exception handling logic.
	 * @param task the original task
	 * @param <T> the result type
	 * @return the wrapped Callable task
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
	 * Creates a Runnable task with exception handling.
	 * <p>
	 * Wraps the original Runnable task with exception handling logic.
	 * @param task the original task
	 * @return the wrapped Runnable task
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
	 * Handles exceptions caught during asynchronous task execution.
	 * <p>
	 * When an exception is caught during task execution, this method is called. It uses
	 * the configured logger to log the exception message and the exception itself.
	 * @param ex the exception caught during task execution
	 */
	protected void handle(Exception ex) {
		log.error(MSG_ASYNC_EXCEPTION, ex.getMessage(), ex);
	}

}
