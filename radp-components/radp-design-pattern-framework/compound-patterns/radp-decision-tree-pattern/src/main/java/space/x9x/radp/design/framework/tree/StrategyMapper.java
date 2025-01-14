package space.x9x.radp.design.framework.tree;

/**
 * 策略映射器
 *
 * @param <T> 入参
 * @param <D> 动态上下文
 * @param <R> 返参
 * @author x9x
 * @since 2025-01-14 14:25
 */
public interface StrategyMapper<T, D, R> {

    /**
     * 获取待执行策略
     * <p>
     * 用于获取每个要执行的节点, 相当于一个流程走完进入到下一个流程的过程.
     *
     * @param requestParameter 入参
     * @param dynamicContext   动态上下文
     * @return 下一个待执行策略
     * @throws Exception 异常
     */
    StrategyHandler<T, D, R> get(T requestParameter, D dynamicContext) throws Exception;
}
