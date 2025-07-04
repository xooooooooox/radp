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

package space.x9x.radp.config;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;

import space.x9x.radp.spring.framework.bootstrap.constant.Globals;

/**
 * Configuration properties for thread pool executor settings. This class defines the
 * properties that can be configured in the application properties file to customize the
 * thread pool executor behavior.
 *
 * @author IO x9x
 * @since 2025-01-17 11:13
 */
@Data
@ConfigurationProperties(prefix = Globals.RADP_CONFIGURATION_PROPERTIES_PREFIX + "thread.pool.executor.config",
		ignoreInvalidFields = true)
public class ThreadPoolConfigProperties {

	/**
	 * Core pool size for the thread pool executor.
	 */
	private Integer corePoolSize = 20;

	/**
	 * Maximum pool size for the thread pool executor.
	 */
	private Integer maxPoolSize = 200;

	/**
	 * Maximum time (in seconds) that excess idle threads will wait for new tasks.
	 */
	private Long keepAliveTime = 10L;

	/**
	 * Maximum queue size for the thread pool executor.
	 */
	private Integer blockQueueSize = 5000;

	/**
	 * Rejection policy for the thread pool executor.
	 */
	private Policy policy = Policy.ABORT_POLICY;

	public enum Policy {

		/**
		 * Discards the task and throws a
		 * {@link java.util.concurrent.RejectedExecutionException}.
		 */
		ABORT_POLICY,

		/**
		 * Discards the task silently without throwing an exception.
		 */
		DISCARD_POLICY,

		/**
		 * Discards the oldest task in the queue and then tries to add the new task again.
		 */
		DISCARD_OLDEST_POLICY,

		/**
		 * If the task cannot be added to the thread pool, the calling thread executes the
		 * task itself.
		 */
		CALLER_RUNS_POLICY

	}

}
