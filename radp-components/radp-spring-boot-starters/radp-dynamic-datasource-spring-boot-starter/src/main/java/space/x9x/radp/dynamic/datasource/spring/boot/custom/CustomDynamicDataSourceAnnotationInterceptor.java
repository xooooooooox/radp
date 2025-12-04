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

package space.x9x.radp.dynamic.datasource.spring.boot.custom;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.dynamic.datasource.aop.DynamicDataSourceAnnotationInterceptor;
import com.baomidou.dynamic.datasource.processor.DsProcessor;
import com.baomidou.dynamic.datasource.support.DataSourceClassResolver;
import org.aopalliance.intercept.MethodInvocation;

import space.x9x.radp.commons.lang.StringConstants;
import space.x9x.radp.spring.data.jdbc.datasource.routing.RoutingDataSourceContextHolder;

/**
 * 自定义的动态数据源注解拦截器.
 * <p>
 * 基于 dynamic-datasource 的拦截器，结合 RoutingDataSourceContextHolder 管理数据源上下文， 兼容使用 @DS 注解和
 * SpEL/前缀标记的动态解析.
 * </p>
 *
 * @author x9x
 * @since 2025-09-20 01:16
 */
public class CustomDynamicDataSourceAnnotationInterceptor extends DynamicDataSourceAnnotationInterceptor {

	/**
	 * 动态数据源前缀标记（例如 "#"），用于指示需要通过 DsProcessor 解析的键.
	 */
	private static final String DYNAMIC_PREFIX = StringConstants.HASH;

	/**
	 * 数据源类解析器，用于在方法/类上解析 @DS 注解定义的数据源键.
	 */
	private final DataSourceClassResolver dataSourceClassResolver;

	/**
	 * 动态数据源键解析器，用于基于方法上下文解析带有前缀的动态数据源键.
	 */
	private final DsProcessor dsProcessor;

	/**
	 * Create a new interceptor that resolves {@link DS} annotations and dynamic data
	 * source keys (starting with {@link #DYNAMIC_PREFIX}) using the given
	 * {@link DsProcessor}.
	 * @param allowedPublicOnly whether only public methods should be intercepted
	 * @param dsProcessor processor used to resolve dynamic data source keys
	 */
	public CustomDynamicDataSourceAnnotationInterceptor(Boolean allowedPublicOnly, DsProcessor dsProcessor) {
		super(allowedPublicOnly, dsProcessor);
		this.dataSourceClassResolver = new DataSourceClassResolver(allowedPublicOnly);
		this.dsProcessor = dsProcessor;
	}

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		String dsKey = determineDatasourceKey(invocation);
		RoutingDataSourceContextHolder.push(dsKey);
		try {
			return invocation.proceed();
		}
		finally {
			RoutingDataSourceContextHolder.poll();
		}
	}

	private String determineDatasourceKey(MethodInvocation invocation) {
		String key = this.dataSourceClassResolver.findKey(invocation.getMethod(), invocation.getThis(), DS.class);
		return key.startsWith(DYNAMIC_PREFIX) ? this.dsProcessor.determineDatasource(invocation, key) : key;
	}

}
