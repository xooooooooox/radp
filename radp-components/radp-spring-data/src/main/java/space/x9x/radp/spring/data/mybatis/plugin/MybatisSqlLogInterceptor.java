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

package space.x9x.radp.spring.data.mybatis.plugin;

import java.time.Duration;

import com.baomidou.mybatisplus.core.toolkit.SystemClock;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import space.x9x.radp.spring.data.mybatis.util.MybatisUtils;

/**
 * MyBatis SQL logging interceptor. This interceptor logs SQL statements executed by
 * MyBatis and their execution times. It can also identify slow SQL queries that exceed a
 * configurable threshold and log them at a warning level.
 *
 * @author x9x
 * @since 2024-09-30 13:38
 */
@Intercepts({
		@Signature(method = "query", type = Executor.class,
				args = { MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class }),
		@Signature(method = "query", type = Executor.class,
				args = { MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class,
						BoundSql.class }),
		@Signature(method = "update", type = Executor.class, args = { MappedStatement.class, Object.class }) })
@Slf4j
public class MybatisSqlLogInterceptor implements Interceptor {

	/**
	 * Constant for the log category name.
	 */
	private static final String MYBATIS_SQL_LOG = "MybatisSqlLog";

	/**
	 * Log format for normal SQL execution. Parameters: 1) statement ID, 2) SQL, 3)
	 * duration in ms.
	 */
	private static final String INFO_SQL = "{} execute sql: {} ({} ms)";

	/**
	 * Log format for slow SQL execution. Parameters: 1) statement ID, 2) threshold in ms,
	 * 3) SQL, 4) duration in ms.
	 */
	private static final String WARN_SQL = "{} execute sql took more than {} ms: {} ({} ms)";

	/**
	 * Threshold for identifying slow SQL queries. Queries that take longer than this
	 * duration will be logged at WARN level instead of INFO level.
	 */
	@Setter
	private Duration slownessThreshold = Duration.ofMillis(1000);

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
		String mappedStatementId = mappedStatement.getId();

		String originalSql = MybatisUtils.getSql(mappedStatement, invocation);

		long start = SystemClock.now();
		Object result = invocation.proceed();
		long duration = SystemClock.now() - start;
		if (Duration.ofMillis(duration).compareTo(this.slownessThreshold) < 0) {
			log.info(INFO_SQL, mappedStatementId, originalSql, duration);
		}
		else {
			log.warn(WARN_SQL, mappedStatementId, this.slownessThreshold.toMillis(), originalSql, duration);
		}

		return result;
	}

	@Override
	public Object plugin(Object target) {
		if (target instanceof Executor) {
			return Plugin.wrap(target, this);
		}
		return target;
	}

}
