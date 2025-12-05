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

package space.x9x.radp.solutions.dict.autoconfigure;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import space.x9x.radp.solutions.dict.core.DictService;
import space.x9x.radp.solutions.dict.core.provider.DbDictDataProvider;
import space.x9x.radp.solutions.dict.core.provider.DictDataProvider;
import space.x9x.radp.solutions.dict.core.provider.DictDataQuery;
import space.x9x.radp.solutions.dict.core.provider.MemoryDictDataProvider;
import space.x9x.radp.solutions.dict.core.service.DefaultDictService;

/**
 * 字典自动装配.
 * <p>
 * 默认提供 Memory 实现，可通过配置切换为 DB 实现。
 * <p>
 * 配置项：
 * <ul>
 * <li>radp.dict.provider: memory | db（默认 memory）</li>
 * <li>radp.dict.types: Map 类型，存放各字典类型的条目</li>
 * </ul>
 *
 * @author RADP x9x
 * @since 2025-11-07 15:48
 */
@AutoConfiguration
@EnableConfigurationProperties(DictProperties.class)
public class DictAutoConfiguration {

	/**
	 * Provides an in-memory {@link DictDataProvider} when provider is set to "memory"
	 * (default).
	 * @param properties dict configuration properties
	 * @return memory-backed {@link DictDataProvider}
	 */
	@Bean
	@ConditionalOnProperty(prefix = DictProperties.PREFIX, name = "provider", havingValue = "memory",
			matchIfMissing = true)
	public DictDataProvider memoryDictDataProvider(DictProperties properties) {
		return new MemoryDictDataProvider(properties);
	}

	/**
	 * Provides a DB-backed {@link DictDataProvider} when provider is set to "db".
	 * @param queryProvider provider for {@link DictDataQuery} (optional)
	 * @return db-backed {@link DictDataProvider}
	 */
	@Bean
	@ConditionalOnProperty(prefix = DictProperties.PREFIX, name = "provider", havingValue = "db")
	public DictDataProvider dbDictDataProvider(ObjectProvider<DictDataQuery> queryProvider) {
		return new DbDictDataProvider(queryProvider);
	}

	/**
	 * Creates the primary {@link DictService} backed by the configured data provider.
	 * @param provider the selected {@link DictDataProvider}
	 * @return {@link DictService} bean
	 */
	@Bean
	@ConditionalOnMissingBean
	public DictService dictService(DictDataProvider provider) {
		return new DefaultDictService(provider);
	}

}
