package space.x9x.radp.spring.boot.task;

import com.alibaba.ttl.TtlCallable;
import com.alibaba.ttl.TtlRunnable;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * TTL 线程池执行器
 *
 * @author x9x
 * @since 2024-09-30 12:04
 */
public class TtlThreadPoolTaskExecutor extends ThreadPoolTaskExecutor {

    private static final long serialVersionUID = 1L;
    private static final String MESSAGE = "Task is not null";

    @Override
    public void execute(Runnable task) {
        super.execute(Objects.requireNonNull(TtlRunnable.get(task), MESSAGE));
    }

    @Override
    public void execute(Runnable task, long startTimeout) {
        super.execute(Objects.requireNonNull(TtlRunnable.get(task), MESSAGE), startTimeout);
    }

    @Override
    public Future<?> submit(Runnable task) {
        return super.submit(Objects.requireNonNull(TtlRunnable.get(task), MESSAGE));
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
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
