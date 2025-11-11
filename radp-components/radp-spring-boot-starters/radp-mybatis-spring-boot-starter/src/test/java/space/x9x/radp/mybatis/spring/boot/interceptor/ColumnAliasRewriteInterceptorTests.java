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

package space.x9x.radp.mybatis.spring.boot.interceptor;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Collections;

import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.junit.jupiter.api.Test;

import space.x9x.radp.mybatis.spring.boot.env.MybatisPlusExtensionProperties;
import space.x9x.radp.spring.data.mybatis.autofill.BasePO;

import static org.assertj.core.api.Assertions.assertThat;

class ColumnAliasRewriteInterceptorTests {

	private static final Method PREPARE_METHOD;

	static {
		try {
			PREPARE_METHOD = StatementHandler.class.getMethod("prepare", Connection.class, Integer.class);
		}
		catch (NoSuchMethodException ex) {
			throw new IllegalStateException(ex);
		}
	}

	@Test
	void scenarioTwoShouldRewriteSelectAndNonSelectSql() throws Throwable {
		MybatisPlusExtensionProperties.SqlRewrite config = new MybatisPlusExtensionProperties.SqlRewrite();
		config.setEnabled(true);
		config.setCreatedColumnName("created_date");
		config.setLastModifiedColumnName("last_modified_date");
		config.setCreatorColumnName("created_by");
		config.setUpdaterColumnName("updated_by");
		config.setScope(MybatisPlusExtensionProperties.SqlRewrite.Scope.BASEPO);

		ColumnAliasRewriteInterceptor interceptor = new ColumnAliasRewriteInterceptor(config);

		String selectSql = "SELECT created_at, updated_at, creator, updater FROM demo WHERE created_at > NOW()";
		TestStatementHandler handler = new TestStatementHandler(selectSql, new DemoPO(), BasePO.class);

		Invocation invocation = new Invocation(handler, PREPARE_METHOD, new Object[] { null, 1 });
		interceptor.intercept(invocation);

		assertThat(handler.getDelegate().getBoundSql().getSql())
			.isEqualTo("SELECT created_date AS created_at, last_modified_date AS updated_at, created_by AS creator, "
					+ "updated_by AS updater FROM demo WHERE created_date > NOW()");

		String updateSql = "UPDATE demo SET updated_at = NOW(), updater = #{updater} WHERE id = #{id}";
		TestStatementHandler updateHandler = new TestStatementHandler(updateSql, new DemoPO(), BasePO.class);
		interceptor.intercept(new Invocation(updateHandler, PREPARE_METHOD, new Object[] { null, 1 }));

		assertThat(updateHandler.getDelegate().getBoundSql().getSql())
			.isEqualTo("UPDATE demo SET last_modified_date = NOW(), updated_by = #{updater} WHERE id = #{id}");
	}

	@Test
	void scopeShouldSkipRewriteWhenBasePOIsNotInvolved() throws Throwable {
		MybatisPlusExtensionProperties.SqlRewrite config = new MybatisPlusExtensionProperties.SqlRewrite();
		config.setEnabled(true);
		config.setCreatedColumnName("created_date");
		config.setLastModifiedColumnName("last_modified_date");
		config.setScope(MybatisPlusExtensionProperties.SqlRewrite.Scope.BASEPO);

		ColumnAliasRewriteInterceptor interceptor = new ColumnAliasRewriteInterceptor(config);

		String selectSql = "SELECT created_at FROM demo";
		TestStatementHandler handler = new TestStatementHandler(selectSql, new Object(), Object.class);
		interceptor.intercept(new Invocation(handler, PREPARE_METHOD, new Object[] { null, 1 }));

		assertThat(handler.getDelegate().getBoundSql().getSql()).isEqualTo(selectSql);

		config.setScope(MybatisPlusExtensionProperties.SqlRewrite.Scope.GLOBAL);
		ColumnAliasRewriteInterceptor globalInterceptor = new ColumnAliasRewriteInterceptor(config);
		TestStatementHandler globalHandler = new TestStatementHandler(selectSql, new Object(), Object.class);
		globalInterceptor.intercept(new Invocation(globalHandler, PREPARE_METHOD, new Object[] { null, 1 }));

		assertThat(globalHandler.getDelegate().getBoundSql().getSql())
			.isEqualTo("SELECT created_date AS created_at FROM demo");
	}

	private static class DemoPO extends BasePO {

	}

	private static class TestStatementHandler implements StatementHandler {

		private final DelegatingStatementHandler delegate;

		TestStatementHandler(String sql, Object parameterObject, Class<?> resultType) {
			this.delegate = new DelegatingStatementHandler(sql, parameterObject, resultType);
		}

		DelegatingStatementHandler getDelegate() {
			return this.delegate;
		}

		@Override
		public Statement prepare(Connection connection, Integer transactionTimeout) {
			return null;
		}

		@Override
		public void parameterize(Statement statement) {
		}

		@Override
		public void batch(Statement statement) {
		}

		@Override
		public int update(Statement statement) {
			return 0;
		}

		@Override
		public <E> java.util.List<E> query(Statement statement, ResultHandler resultHandler) {
			return Collections.emptyList();
		}

		@Override
		public <E> Cursor<E> queryCursor(Statement statement) {
			return null;
		}

		@Override
		public BoundSql getBoundSql() {
			return this.delegate.getBoundSql();
		}

		@Override
		public ParameterHandler getParameterHandler() {
			return this.delegate.getParameterHandler();
		}

	}

	private static class DelegatingStatementHandler implements StatementHandler {

		private final MappedStatement mappedStatement;

		private final BoundSql boundSql;

		private final ParameterHandler parameterHandler;

		DelegatingStatementHandler(String sql, Object parameterObject, Class<?> resultType) {
			Configuration configuration = new Configuration();
			ResultMap resultMap = new ResultMap.Builder(configuration, "default", resultType, Collections.emptyList())
				.build();
			MappedStatement.Builder builder = new MappedStatement.Builder(configuration, "demo",
					new StaticSqlSourceAdapter(configuration, sql), SqlCommandType.SELECT);
			builder.resultMaps(Collections.singletonList(resultMap));
			builder.cache(null);
			this.mappedStatement = builder.build();
			this.boundSql = new BoundSql(configuration, sql, Collections.emptyList(), parameterObject);
			this.parameterHandler = new DefaultParameterHandler(this.mappedStatement, parameterObject, this.boundSql);
		}

		public MappedStatement getMappedStatement() {
			return this.mappedStatement;
		}

		@Override
		public Statement prepare(Connection connection, Integer transactionTimeout) {
			return null;
		}

		@Override
		public void parameterize(Statement statement) {
		}

		@Override
		public void batch(Statement statement) {
		}

		@Override
		public int update(Statement statement) {
			return 0;
		}

		@Override
		public <E> java.util.List<E> query(Statement statement, ResultHandler resultHandler) {
			return Collections.emptyList();
		}

		@Override
		public <E> Cursor<E> queryCursor(Statement statement) {
			return null;
		}

		@Override
		public BoundSql getBoundSql() {
			return this.boundSql;
		}

		@Override
		public ParameterHandler getParameterHandler() {
			return this.parameterHandler;
		}

	}

	private static class StaticSqlSourceAdapter extends org.apache.ibatis.builder.StaticSqlSource {

		private final Configuration configuration;

		StaticSqlSourceAdapter(Configuration configuration, String sql) {
			super(configuration, sql, Collections.emptyList());
			this.configuration = configuration;
		}

		@Override
		public BoundSql getBoundSql(Object parameterObject) {
			BoundSql boundSql = super.getBoundSql(parameterObject);
			return new BoundSql(this.configuration, boundSql.getSql(), boundSql.getParameterMappings(),
					parameterObject);
		}

	}

}
