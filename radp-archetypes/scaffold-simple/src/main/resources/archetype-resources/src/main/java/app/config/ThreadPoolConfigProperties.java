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

#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.app.config;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;

import space.x9x.radp.spring.framework.bootstrap.constant.Globals;

/**
 * Configuration properties for thread pool executor settings. This class defines the
 * properties that can be configured in the application properties file to customize the
 * thread pool executor behavior.
 *
 * @author RADP x9x
 * @since 2025-01-17 11:13
 */
@Data
@ConfigurationProperties(prefix = Globals.RADP_CONFIGURATION_PROPERTIES_PREFIX + "thread.pool.executor.config",
		ignoreInvalidFields = true)
public class ThreadPoolConfigProperties {

	/**
	 * 核心线程数.
	 */
	private Integer corePoolSize = 20;

	/**
	 * 最大线程数.
	 */
	private Integer maxPoolSize = 200;

	/**
	 * 最大等待时间（单位：秒）.
	 */
	private Long keepAliveTime = 10L;

	/**
	 * 最大队列数.
	 */
	private Integer blockQueueSize = 5000;

	/**
	 * 拒绝策略.
	 */
	private Policy policy = Policy.ABORT_POLICY;

	public enum Policy {

		/**
		 * 丢弃任务并抛出 {@link java.util.concurrent.RejectedExecutionException} 异常.
		 */
		ABORT_POLICY,

		/**
		 * 直接丢弃任务,但不会抛出异常.
		 */
		DISCARD_POLICY,

		/**
		 * 将最早进入队列的任务删除,之后再尝试加入队列的任务被拒绝.
		 */
		DISCARD_OLDEST_POLICY,

		/**
		 * 如果任务添加线程池失败,那么主线程自己执行该任务.
		 */
		CALLER_RUNS_POLICY

	}

}
