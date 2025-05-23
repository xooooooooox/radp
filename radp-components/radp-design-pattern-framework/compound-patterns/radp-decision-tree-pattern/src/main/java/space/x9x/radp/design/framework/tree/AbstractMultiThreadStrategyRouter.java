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
