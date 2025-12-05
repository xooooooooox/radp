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
package ${package}.config;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Configuration class for thread pool executor settings. This class provides a
 * customizable thread pool executor bean based on the properties defined in
 * ThreadPoolConfigProperties.
 *
 * The {@code @EnableAsync} annotation enables asynchronous method execution. The
 * {@code @Configuration} annotation indicates that this class declares one or more
 * {@code @Bean} methods and may be processed by the Spring container.
 *
 * @author RADP x9x
 * @since 2025-01-17 11:13
 */
@Slf4j
@EnableAsync
@Configuration
@EnableConfigurationProperties(ThreadPoolConfigProperties.class)
public class ThreadPoolConfig {

	/**
	 * Creates and configures a ThreadPoolExecutor based on the provided properties. This
	 * bean will only be created if no other ThreadPoolExecutor bean exists in the
	 * context.
	 * @param properties the configuration properties for the thread pool
	 * @return a configured ThreadPoolExecutor instance
	 */
	@Bean
	@ConditionalOnMissingBean(ThreadPoolExecutor.class)
	public ThreadPoolExecutor threadPoolExecutor(ThreadPoolConfigProperties properties) {
		// 实例化策略
		RejectedExecutionHandler handler = switch (properties.getPolicy()) {
			case ABORT_POLICY -> new ThreadPoolExecutor.AbortPolicy();
			case DISCARD_POLICY -> new ThreadPoolExecutor.DiscardPolicy();
			case DISCARD_OLDEST_POLICY -> new ThreadPoolExecutor.DiscardOldestPolicy();
			case CALLER_RUNS_POLICY -> new ThreadPoolExecutor.CallerRunsPolicy();
		};

		// 创建线程池
		return new ThreadPoolExecutor(properties.getCorePoolSize(), properties.getMaxPoolSize(),
				properties.getKeepAliveTime(), TimeUnit.SECONDS,
				new LinkedBlockingQueue<>(properties.getBlockQueueSize()), Executors.defaultThreadFactory(), handler);
	}

}
