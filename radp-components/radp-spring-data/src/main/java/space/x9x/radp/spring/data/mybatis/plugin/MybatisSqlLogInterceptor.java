package space.x9x.radp.spring.data.mybatis.plugin;


import com.baomidou.mybatisplus.core.toolkit.SystemClock;
import space.x9x.radp.spring.data.mybatis.util.MybatisUtils;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.time.Duration;

/**
 * Mybatis SQL 日志拦截器
 *
 * @author x9x
 * @since 2024-09-30 13:38
 */
@Intercepts({
        @Signature(method = "query", type = Executor.class, args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(method = "query", type = Executor.class, args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
        @Signature(method = "update", type = Executor.class, args = {MappedStatement.class, Object.class})
})
@Slf4j
public class MybatisSqlLogInterceptor implements Interceptor {

    private static final String MYBATIS_SQL_LOG = "MybatisSqlLog";
    private static final String INFO_SQL = "{} execute sql: {} ({} ms)";
    private static final String WARN_SQL = "{} execute sql took more than {} ms: {} ({} ms)";

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
        if (Duration.ofMillis(duration).compareTo(slownessThreshold) < 0) {
            log.info(INFO_SQL, mappedStatementId, originalSql, duration);
        } else {
            log.warn(WARN_SQL, mappedStatementId, slownessThreshold.toMillis(), originalSql, duration);
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
