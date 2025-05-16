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
 *
 * @author x9x
 * @since 2024-09-30 11:43
 */
@Slf4j
public class ExceptionHandlingAsyncTaskExecutor implements AsyncTaskExecutor, InitializingBean, DisposableBean {

    private static final String MSG_ASYNC_EXCEPTION = "AsyncTaskExecutor exec caught exception: {}";
    private final AsyncTaskExecutor executor;

    /**
     * Constructs an ExceptionHandlingAsyncTaskExecutor with the specified executor.
     * This wrapper delegates task execution to the provided executor while adding
     * exception handling capabilities.
     *
     * @param executor the underlying AsyncTaskExecutor to delegate task execution to
     */
    public ExceptionHandlingAsyncTaskExecutor(AsyncTaskExecutor executor) {
        this.executor = executor;
    }

    @Override
    public void destroy() throws Exception {
        if (executor instanceof DisposableBean) {
            DisposableBean bean = (DisposableBean) executor;
            bean.destroy();
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (executor instanceof InitializingBean) {
            InitializingBean bean = (InitializingBean) executor;
            bean.afterPropertiesSet();
        }
    }

    @Override
    public void execute(@NotNull Runnable task, long startTimeout) {
        executor.execute(createWrappedRunnable(task), startTimeout);
    }

    @Override
    public Future<?> submit(@NotNull Runnable task) {
        return executor.submit(createWrappedRunnable(task));
    }

    @Override
    public <T> Future<T> submit(@NotNull Callable<T> task) {
        return executor.submit(createCallable(task));
    }

    @Override
    public void execute(@NotNull Runnable task) {
        executor.execute(createWrappedRunnable(task));
    }

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
     * Handles exceptions that occur during task execution.
     * This method logs the exception message and stack trace at the error level.
     * Subclasses can override this method to provide custom exception handling.
     *
     * @param e the exception that occurred during task execution
     */
    protected void handle(Exception e) {
        log.error(MSG_ASYNC_EXCEPTION, e.getMessage(), e);
    }
}
