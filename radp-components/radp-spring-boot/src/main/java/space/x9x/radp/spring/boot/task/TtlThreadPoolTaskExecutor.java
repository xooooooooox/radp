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

import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import com.alibaba.ttl.TtlCallable;
import com.alibaba.ttl.TtlRunnable;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.concurrent.ListenableFuture;

/**
 * TTL Thread Pool Task Executor.
 * <p>
 * This executor extends Spring's ThreadPoolTaskExecutor and uses Alibaba's TTL
 * (TransmittableThreadLocal) to ensure ThreadLocal variables can be correctly passed in
 * thread pools. This is important for applications that need to maintain context
 * information (such as user identity, request tracking ID, etc.) in asynchronous
 * execution contexts.
 * <p>
 * This class overrides all task submission methods to ensure each task is wrapped with
 * TTL, thereby enabling the transmission of ThreadLocal values.
 *
 * @author RADP x9x
 * @since 2024-09-30 12:04
 */
public class TtlThreadPoolTaskExecutor extends ThreadPoolTaskExecutor {

	private static final long serialVersionUID = 1L;

	/**
	 * Error message used for null checks. This constant defines the message used when
	 * validating that tasks are not null.
	 */
	private static final String MESSAGE = "Task is not null";

	/**
	 * Executes a Runnable task.
	 * <p>
	 * Wraps the task as a TTL task to ensure ThreadLocal values are passed to the
	 * execution thread.
	 * @param task the task to execute
	 */
	@Override
	public void execute(Runnable task) {
		super.execute(Objects.requireNonNull(TtlRunnable.get(task), MESSAGE));
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
	public void execute(Runnable task, long startTimeout) {
		super.execute(Objects.requireNonNull(TtlRunnable.get(task), MESSAGE), startTimeout);
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
	public Future<?> submit(Runnable task) {
		return super.submit(Objects.requireNonNull(TtlRunnable.get(task), MESSAGE));
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
	public <T> Future<T> submit(Callable<T> task) {
		return super.submit(Objects.requireNonNull(TtlCallable.get(task), MESSAGE));
	}

	/**
	 * Submits a Runnable task for execution with a listenable future result.
	 * <p>
	 * Wraps the task as a TTL task to ensure ThreadLocal values are passed to the
	 * execution thread.
	 * @param task the task to submit
	 * @return a ListenableFuture representing pending completion of the task
	 */
	@Override
	public ListenableFuture<?> submitListenable(Runnable task) {
		return super.submitListenable(Objects.requireNonNull(TtlRunnable.get(task), MESSAGE));
	}

	/**
	 * Submits a Callable task for execution with a listenable future result.
	 * <p>
	 * Wraps the task as a TTL task to ensure ThreadLocal values are passed to the
	 * execution thread.
	 * @param task the task to submit
	 * @param <T> the result type
	 * @return a ListenableFuture representing pending completion of the task
	 */
	@Override
	public <T> ListenableFuture<T> submitListenable(Callable<T> task) {
		return super.submitListenable(Objects.requireNonNull(TtlCallable.get(task), MESSAGE));
	}

}
