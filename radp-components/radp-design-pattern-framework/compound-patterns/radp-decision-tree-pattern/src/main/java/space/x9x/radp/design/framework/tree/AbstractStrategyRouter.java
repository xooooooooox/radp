package space.x9x.radp.design.framework.tree;

/**
 * 策略路由抽象类
 *
 * @author x9x
 * @since 2025-01-14 14:36
 */
public abstract class AbstractStrategyRouter<T, D, R> implements StrategyMapper<T, D, R>, StrategyHandler<T, D, R> {

    /**
     * 默认策略处理器，当没有找到匹配的策略处理器时使用
     * 默认使用空实现，返回null
     */
    @SuppressWarnings("unchecked")
    protected StrategyHandler<T, D, R> defaultStrategyHandler = StrategyHandler.EMPTY;

    /**
     * 路由方法，根据请求参数和动态上下文选择合适的策略处理器
     * 如果找不到匹配的策略处理器，则使用默认策略处理器
     *
     * @param requestParameter 请求参数
     * @param dynamicContext   动态上下文
     * @return 策略处理结果
     * @throws Exception 当策略处理过程中发生异常时抛出
     */
    public R router(T requestParameter, D dynamicContext) throws Exception {
        // 通过调用 策略映射器StrategyHandler#get, 控制节点流程走向
        StrategyHandler<T, D, R> nextStrategyHandler = get(requestParameter, dynamicContext);

        if (nextStrategyHandler != null) {
            return nextStrategyHandler.apply(requestParameter, dynamicContext);
        }
        return defaultStrategyHandler.apply(requestParameter, dynamicContext);
    }
}
