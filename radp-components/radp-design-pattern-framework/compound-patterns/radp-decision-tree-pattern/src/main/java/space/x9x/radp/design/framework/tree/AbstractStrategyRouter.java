package space.x9x.radp.design.framework.tree;

/**
 * 策略路由抽象类
 *
 * @author x9x
 * @since 2025-01-14 14:36
 */
public abstract class AbstractStrategyRouter<T, D, R> implements StrategyMapper<T, D, R>, StrategyHandler<T, D, R> {

    /**
     * Default strategy handler that will be used when no matching strategy is found.
     * This handler is initialized to an empty implementation that can be overridden
     * by concrete router implementations.
     */
    @SuppressWarnings("unchecked")
    protected StrategyHandler<T, D, R> defaultStrategyHandler = StrategyHandler.EMPTY;

    /**
     * Routes the request to the appropriate strategy handler based on the input parameters.
     * This method implements the core decision tree logic by:
     * 1. Using the strategy mapper to determine the next handler
     * 2. Applying the selected handler to process the request
     * 3. Falling back to the default handler if no matching strategy is found
     *
     * @param requestParameter the input parameter that determines routing
     * @param dynamicContext   additional context information for processing
     * @return the result from the selected strategy handler
     * @throws Exception if an error occurs during strategy execution
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
