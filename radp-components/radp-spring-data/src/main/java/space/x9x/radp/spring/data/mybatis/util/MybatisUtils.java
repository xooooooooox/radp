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

package space.x9x.radp.spring.data.mybatis.util;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.DataSource;

import lombok.experimental.UtilityClass;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandlerRegistry;

import space.x9x.radp.commons.lang.StringConstants;
import space.x9x.radp.spring.data.jdbc.datasource.DataSourceUrlParserLoader;

/**
 * Utility class for MyBatis operations. This class provides helper methods for working
 * with MyBatis, including extracting database URLs and resolving SQL queries with
 * parameters. It's particularly useful for logging and debugging MyBatis operations.
 *
 * @author RADP x9x
 * @since 2024-09-30 13:48
 */
@UtilityClass
public class MybatisUtils {

	/**
	 * Pattern used to find parameter placeholders (?) in SQL queries.
	 */
	private static final Pattern PARAMETER_PATTERN = Pattern.compile("\\?");

	/**
	 * Extracts the database URL from a MyBatis MappedStatement. This method retrieves the
	 * data source from the MyBatis configuration and uses the DataSourceUrlParserLoader
	 * to extract the database URL.
	 * @param mappedStatement the MyBatis MappedStatement containing the configuration
	 * @return the database URL as a string
	 */
	public String getDatabaseUrl(MappedStatement mappedStatement) {
		Configuration configuration = mappedStatement.getConfiguration();
		Environment environment = configuration.getEnvironment();
		DataSource dataSource = environment.getDataSource();
		return DataSourceUrlParserLoader.parse(dataSource);
	}

	/**
	 * Resolves a SQL query with its parameters from a MyBatis invocation. This method
	 * extracts the SQL query and its parameters from the MyBatis objects and returns a
	 * complete SQL string with parameter values inserted.
	 * @param mappedStatement the MyBatis MappedStatement containing the SQL
	 * @param invocation the MyBatis Invocation containing the parameters
	 * @return the resolved SQL query with parameter values inserted
	 */
	public String getSql(MappedStatement mappedStatement, Invocation invocation) {
		Object parameter = null;
		if (invocation.getArgs().length > 1) {
			parameter = invocation.getArgs()[1];
		}
		BoundSql boundSql = mappedStatement.getBoundSql(parameter);
		Configuration configuration = mappedStatement.getConfiguration();
		return resolveSql(configuration, boundSql);
	}

	/**
	 * Resolves a SQL query by replacing parameter placeholders with actual values. This
	 * method processes the bound SQL and its parameter mappings to create a complete SQL
	 * string with all parameter values inserted in place of placeholders.
	 * @param configuration the MyBatis Configuration object
	 * @param boundSql the BoundSql containing the SQL and parameter mappings
	 * @return the resolved SQL query with parameter values inserted
	 */
	private static String resolveSql(Configuration configuration, BoundSql boundSql) {
		Object parameterObject = boundSql.getParameterObject();
		List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
		String sql = boundSql.getSql().replaceAll("[\\s]+", StringConstants.SPACE);
		if (!parameterMappings.isEmpty() && parameterObject != null) {
			TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
			if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
				sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(resolveParameterValue(parameterObject)));
			}
			else {
				MetaObject metaObject = configuration.newMetaObject(parameterObject);
				Matcher matcher = PARAMETER_PATTERN.matcher(sql);
				StringBuffer sqlBuffer = new StringBuffer();
				for (ParameterMapping parameterMapping : parameterMappings) {
					String propertyName = parameterMapping.getProperty();
					Object obj = null;
					if (metaObject.hasGetter(propertyName)) {
						obj = metaObject.getValue(propertyName);
					}
					else if (boundSql.hasAdditionalParameter(propertyName)) {
						obj = boundSql.getAdditionalParameter(propertyName);
					}
					if (matcher.find()) {
						matcher.appendReplacement(sqlBuffer, Matcher.quoteReplacement(resolveParameterValue(obj)));
					}
				}
				matcher.appendTail(sqlBuffer);
				sql = sqlBuffer.toString();
			}
		}
		return sql;
	}

	/**
	 * Converts a parameter object to its string representation for SQL. This method
	 * handles different types of parameters differently: - String-like objects are
	 * wrapped in single quotes - Date objects are formatted and wrapped in single quotes
	 * - Other objects are converted to strings using String.valueOf() - Null values are
	 * converted to empty strings
	 * @param obj the parameter object to convert
	 * @return the string representation of the parameter for SQL
	 */
	private static String resolveParameterValue(Object obj) {
		if (obj instanceof CharSequence) {
			return StringConstants.HARD_QUOTE + obj + StringConstants.HARD_QUOTE;
		}
		if (obj instanceof Date) {
			DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);
			return StringConstants.HARD_QUOTE + formatter.format(obj) + StringConstants.HARD_QUOTE;
		}
		return obj == null ? StringConstants.EMPTY : String.valueOf(obj);
	}

}
