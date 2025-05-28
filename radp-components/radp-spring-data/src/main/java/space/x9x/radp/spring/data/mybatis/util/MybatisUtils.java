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

import space.x9x.radp.commons.lang.Strings;
import space.x9x.radp.spring.data.jdbc.datasource.DataSourceUrlParserLoader;

/**
 * @author IO x9x
 * @since 2024-09-30 13:48
 */
@UtilityClass
public class MybatisUtils {
    private static final Pattern PARAMETER_PATTERN = Pattern.compile("\\?");

    /**
     * Extracts the database URL from a MappedStatement's configuration.
     * This method retrieves the DataSource from the MappedStatement's environment
     * and uses the DataSourceUrlParserLoader to parse the URL.
     *
     * @param mappedStatement the MappedStatement containing the database configuration
     * @return the database URL as a string
     */
    public String getDatabaseUrl(MappedStatement mappedStatement) {
        Configuration configuration = mappedStatement.getConfiguration();
        Environment environment = configuration.getEnvironment();
        DataSource dataSource = environment.getDataSource();
        return DataSourceUrlParserLoader.parse(dataSource);
    }

    /**
     * Extracts and formats the SQL query from a MappedStatement and Invocation.
     * This method retrieves the SQL query with parameter values substituted in place
     * of the parameter placeholders, making it useful for logging and debugging.
     *
     * @param mappedStatement the MappedStatement containing the SQL query
     * @param invocation      the Invocation containing the parameter values
     * @return the formatted SQL query with parameter values
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

    private static String resolveSql(Configuration configuration, BoundSql boundSql) {
        Object parameterObject = boundSql.getParameterObject();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        String sql = boundSql.getSql().replaceAll("[\\s]+", Strings.SPACE);
        if (!parameterMappings.isEmpty() && parameterObject !=null) {
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(resolveParameterValue(parameterObject)));
            } else {
                MetaObject metaObject = configuration.newMetaObject(parameterObject);
                Matcher matcher = PARAMETER_PATTERN.matcher(sql);
                StringBuffer sqlBuffer = new StringBuffer();
                for (ParameterMapping parameterMapping : parameterMappings) {
                    String propertyName = parameterMapping.getProperty();
                    Object obj = null;
                    if (metaObject.hasGetter(propertyName)) {
                        obj = metaObject.getValue(propertyName);
                    } else if (boundSql.hasAdditionalParameter(propertyName)) {
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

    private static String resolveParameterValue(Object obj) {
        if (obj instanceof CharSequence) {
            return Strings.HARD_QUOTE + obj + Strings.HARD_QUOTE;
        }
        if (obj instanceof Date) {
            DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);
            return Strings.HARD_QUOTE + formatter.format(obj) + Strings.HARD_QUOTE;
        }
        return obj == null ? Strings.EMPTY : String.valueOf(obj);
    }
}
