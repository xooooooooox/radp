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
 * 策略受理器.
 *
 * @author IO x9x
 * @since 2025-01-14 14:29
 * @param <T> the type of request parameter
 * @param <D> the type of dynamic context
 * @param <R> the type of result
 */
public interface StrategyHandler<T, D, R> {

	/**
	 * 空策略处理器. 不执行任何操作，直接返回null 用作默认策略处理器，当没有找到匹配的策略处理器时使用.
	 */
	StrategyHandler EMPTY = (requestParameter, dynamicContext) -> null;

	/**
	 * 受理策略处理.
	 * <p>
	 * 受理执行的业务流程. 每个业务流程执行时,如果后置节点依赖前置节点的数据, 可以将数据填充到 {@code dynamicContext} 中供后置节点使用.
	 * @param requestParameter 入参
	 * @param dynamicContext 动态上下文
	 * @return 执行结果
	 * @throws Exception 异常
	 */
	R apply(T requestParameter, D dynamicContext) throws Exception;

}
