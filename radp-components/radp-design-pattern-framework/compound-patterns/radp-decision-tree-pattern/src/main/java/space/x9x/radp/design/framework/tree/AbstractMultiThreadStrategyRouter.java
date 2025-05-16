package space.x9x.radp.design.framework.tree;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * 多线程异步资源加载 + 策略路由的抽象类
 *
 * @author x9x
 * @since 2025-01-14 21:53
 */
public abstract class AbstractMultiThreadStrategyRouter<T, D, R> extends AbstractStrategyRouter<T, D, R> {

    @Override
    public R apply(T requestParameter, D dynamicContext) throws Exception {
        // 异步加载数据
        multiThread(requestParameter, dynamicContext);
        // 业务流程受理
        return doApply(requestParameter, dynamicContext);
    }

    /**
     * 异步加载数据
     *
     * @param requestParameter 入参
     * @param dynamicContext   动态上下文
     * @throws ExecutionException 当异步任务执行过程中发生异常时抛出
     * @throws InterruptedException 当异步任务被中断时抛出
     * @throws TimeoutException 当异步任务执行超时时抛出
     */
    protected abstract void multiThread(T requestParameter, D dynamicContext) throws ExecutionException, InterruptedException, TimeoutException;

    /**
     * 业务流程受理
     *
     * @param requestParameter 入参
     * @param dynamicContext   动态上下文
     * @return 执行结果
     * @throws Exception 当业务流程处理过程中发生异常时抛出
     */
    protected abstract R doApply(T requestParameter, D dynamicContext) throws Exception;
}
