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
 * Strategy mapper interface. 策略映射器.
 *
 * @author IO x9x
 * @since 2025-01-14 14:25
 * @param <T> 入参 - the type of request parameter
 * @param <D> 动态上下文 - the type of dynamic context
 * @param <R> 返参 - the type of result
 */
public interface StrategyMapper<T, D, R> {

	/**
	 * Gets the strategy to be executed. 获取待执行策略
	 * <p>
	 * Used to get each node to be executed, equivalent to the process of completing one
	 * flow and entering the next flow. 用于获取每个要执行的节点, 相当于一个流程走完进入到下一个流程的过程.
	 * @param requestParameter the request parameter
	 * @param dynamicContext the dynamic context
	 * @return the next strategy to be executed
	 * @throws Exception when an error occurs during strategy retrieval
	 */
	StrategyHandler<T, D, R> get(T requestParameter, D dynamicContext) throws Exception;

}
