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
