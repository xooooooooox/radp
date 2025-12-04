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

package space.x9x.radp.design.framework.tree;

/**
 * Abstract class for strategy routing. 策略路由抽象类
 *
 * @author RADP x9x
 * @since 2025-01-14 14:36
 * @param <T> the type of request parameter
 * @param <D> the type of dynamic context
 * @param <R> the type of result
 */
public abstract class AbstractStrategyRouter<T, D, R> implements StrategyMapper<T, D, R>, StrategyHandler<T, D, R> {

	/**
	 * Default strategy handler used when no matching strategy handler is found.
	 * 默认策略处理器，当没有找到匹配的策略处理器时使用 默认使用空实现，返回null.
	 */
	@SuppressWarnings("unchecked")
	protected StrategyHandler<T, D, R> defaultStrategyHandler = StrategyHandler.EMPTY;

	/**
	 * 路由方法. 根据请求参数和动态上下文选择合适的策略处理器 如果找不到匹配的策略处理器，则使用默认策略处理器
	 * @param requestParameter 请求参数
	 * @param dynamicContext 动态上下文
	 * @return 策略处理结果
	 * @throws Exception 当策略处理过程中发生异常时抛出
	 */
	public R router(T requestParameter, D dynamicContext) throws Exception {
		// 通过调用 策略映射器StrategyHandler#get, 控制节点流程走向
		StrategyHandler<T, D, R> nextStrategyHandler = get(requestParameter, dynamicContext);

		if (nextStrategyHandler != null) {
			return nextStrategyHandler.apply(requestParameter, dynamicContext);
		}
		return this.defaultStrategyHandler.apply(requestParameter, dynamicContext);
	}

}
