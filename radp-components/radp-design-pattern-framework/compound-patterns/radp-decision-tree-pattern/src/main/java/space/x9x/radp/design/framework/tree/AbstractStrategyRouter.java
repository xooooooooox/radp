package space.x9x.radp.design.framework.tree;

/**
 * 策略路由抽象类
 *
 * @author x9x
 * @since 2025-01-14 14:36
 */
public abstract class AbstractStrategyRouter<T, D, R> implements StrategyMapper<T, D, R>, StrategyHandler<T, D, R> {

    @SuppressWarnings("unchecked")
    protected StrategyHandler<T, D, R> defaultStrategyHandler = StrategyHandler.EMPTY;

    public R router(T requestParameter, D dynamicContext) throws Exception {
        // 通过调用 策略映射器StrategyHandler#get, 控制节点流程走向
        StrategyHandler<T, D, R> nextStrategyHandler = get(requestParameter, dynamicContext);

        if (nextStrategyHandler != null) {
            return nextStrategyHandler.apply(requestParameter, dynamicContext);
        }
        return defaultStrategyHandler.apply(requestParameter, dynamicContext);
    }
}
