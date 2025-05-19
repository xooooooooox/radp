package space.x9x.radp.spring.framework.task.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.task.AsyncTaskExecutor;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * 异步任务执行异常处理类
 * <p>
 * 该类包装了AsyncTaskExecutor，为异步任务执行添加了异常处理功能。
 * 当异步任务执行过程中抛出异常时，会通过handle方法进行处理，
 * 确保异常被正确记录而不会被静默吞噬。
 * <p>
 * 该类同时实现了InitializingBean和DisposableBean接口，
 * 以确保被包装的执行器的生命周期方法被正确调用。
 *
 * @author x9x
 * @since 2024-09-30 11:43
 */
@Slf4j
public class ExceptionHandlingAsyncTaskExecutor implements AsyncTaskExecutor, InitializingBean, DisposableBean {

    private static final String MSG_ASYNC_EXCEPTION = "AsyncTaskExecutor exec caught exception: {}";
    private final AsyncTaskExecutor executor;

    /**
     * Constructs a new ExceptionHandlingAsyncTaskExecutor with the specified executor.
     * This wrapper adds exception handling capabilities to the provided AsyncTaskExecutor,
     * ensuring that exceptions thrown during asynchronous task execution are properly logged.
     *
     * @param executor the AsyncTaskExecutor to be wrapped with exception handling
     */
    public ExceptionHandlingAsyncTaskExecutor(AsyncTaskExecutor executor) {
        this.executor = executor;
    }

    @Override
    public void destroy() throws Exception {
        if (executor instanceof DisposableBean bean) {
            bean.destroy();
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (executor instanceof InitializingBean bean) {
            bean.afterPropertiesSet();
        }
    }

    /**
     * 使用启动超时执行Runnable任务
     * <p>
     * 将任务包装为异常处理任务，并委托给底层执行器执行
     *
     * @param task         要执行的任务
     * @param startTimeout 启动超时时间（毫秒）
     * @deprecated 此方法在AsyncTaskExecutor接口中已被弃用，建议使用其他方法替代
     */
    @Override
    @Deprecated(since = "6.0", forRemoval = true)
    public void execute(@NotNull Runnable task, long startTimeout) {
        executor.execute(createWrappedRunnable(task), startTimeout);
    }

    /**
     * 提交Runnable任务
     * <p>
     * 将任务包装为异常处理任务，并委托给底层执行器执行
     *
     * @param task 要执行的任务
     * @return 表示任务挂起完成的Future
     */
    @Override
    public @NotNull Future<?> submit(@NotNull Runnable task) {
        return executor.submit(createWrappedRunnable(task));
    }

    /**
     * 提交Callable任务
     * <p>
     * 将任务包装为异常处理任务，并委托给底层执行器执行
     *
     * @param task 要执行的任务
     * @param <T>  结果类型
     * @return 表示任务挂起完成的Future
     */
    @Override
    public <T> @NotNull Future<T> submit(@NotNull Callable<T> task) {
        return executor.submit(createCallable(task));
    }

    /**
     * 执行Runnable任务
     * <p>
     * 将任务包装为异常处理任务，并委托给底层执行器执行
     *
     * @param task 要执行的任务
     */
    @Override
    public void execute(@NotNull Runnable task) {
        executor.execute(createWrappedRunnable(task));
    }

    /**
     * 创建带有异常处理的Callable任务
     * <p>
     * 包装原始Callable任务，添加异常处理逻辑
     *
     * @param task 原始任务
     * @param <T>  结果类型
     * @return 包装后的Callable任务
     */
    private <T> Callable<T> createCallable(final Callable<T> task) {
        return () -> {
            try {
                return task.call();
            } catch (Exception e) {
                handle(e);
                throw e;
            }
        };
    }

    /**
     * 创建带有异常处理的Runnable任务
     * <p>
     * 包装原始Runnable任务，添加异常处理逻辑
     *
     * @param task 原始任务
     * @return 包装后的Runnable任务
     */
    private Runnable createWrappedRunnable(final Runnable task) {
        return () -> {
            try {
                task.run();
            } catch (Exception e) {
                handle(e);
            }
        };
    }

    /**
     * 处理异步任务执行过程中发生的异常
     * <p>
     * 当执行任务时捕获到异常时，会调用此方法。
     * 它使用配置的日志记录器记录异常消息和异常本身。
     *
     * @param e 任务执行过程中捕获的异常
     */
    protected void handle(Exception e) {
        log.error(MSG_ASYNC_EXCEPTION, e.getMessage(), e);
    }
}
