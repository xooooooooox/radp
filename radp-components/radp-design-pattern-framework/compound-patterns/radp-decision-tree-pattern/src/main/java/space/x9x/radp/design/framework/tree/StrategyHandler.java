package space.x9x.radp.design.framework.tree;

/**
 * 策略受理器
 *
 * @author x9x
 * @since 2025-01-14 14:29
 */
public interface StrategyHandler<T, D, R> {

    StrategyHandler EMPTY = (requestParameter, dynamicContext) -> null;

    /**
     * 受理策略处理
     * <p>
     * 手里执行的业务流程. 每个业务流程执行时,如果后置节点依赖前置节点的数据, 可以将数据填充到 {@code dynamicContext} 中供后置节点使用
     *
     * @param requestParameter 入参
     * @param dynamicContext   动态上下文
     * @return 执行结果
     * @throws Exception 异常
     */
    R apply(T requestParameter, D dynamicContext) throws Exception;
}
