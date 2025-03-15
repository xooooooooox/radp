#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import space.x9x.radp.spring.framework.bootstrap.constant.Globals;

/**
 * @author x9x
 * @since 2025-01-17 11:13
 */
@Data
@ConfigurationProperties(prefix = Globals.RADP_CONFIGURATION_PROPERTIES_PREFIX + "thread.pool.executor.config", ignoreInvalidFields = true)
public class ThreadPoolConfigProperties {

    /**
     * 核心线程数
     */
    private Integer corePoolSize = 20;

    /**
     * 最大线程数
     */
    private Integer maxPoolSize = 200;

    /**
     * 最大等待时间（单位：秒）
     */
    private Long keepAliveTime = 10L;

    /**
     * 最大队列数
     */
    private Integer blockQueueSize = 5000;

    /**
     * 拒绝策略
     */
    private Policy policy = Policy.ABORT_POLICY;


    public enum Policy {

        /**
         * 丢弃任务并抛出 {@link java.util.concurrent.RejectedExecutionException} 异常
         */
        ABORT_POLICY,

        /**
         * 直接丢弃任务,但不会抛出异常
         */
        DISCARD_POLICY,

        /**
         * 将最早进入队列的任务删除,之后再尝试加入队列的任务被拒绝
         */
        DISCARD_OLDEST_POLICY,

        /**
         * 如果任务添加线程池失败,那么主线程自己执行该任务
         */
        CALLER_RUNS_POLICY,

        ;
    }
}
