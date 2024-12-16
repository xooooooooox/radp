package com.x9x.radp.spring.framework.logging.access.config;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 日志切面配置
 *
 * @author x9x
 * @since 2024-09-30 09:45
 */
@EqualsAndHashCode
@ToString
@Setter
@Getter
public class AccessLogConfig {

    /**
     * 是否保存到 MDC
     */
    private boolean enabledMdc = true;

    /**
     * 需要输出日志的包名
     */
    private String expression;

    /**
     * 日志输出采样率
     */
    private double sampleRate = 1.0;

    /**
     * 是否输出入参
     */
    private boolean logArguments = true;

    /**
     * 是否输出返回值
     */
    private boolean logReturnValue = true;

    /**
     * 是否输出方法执行耗时
     */
    private boolean logExecutionTime = true;

    /**
     * 最大长度
     */
    private int maxLength = 500;

    /**
     * 慢日志
     */
    private long slowThreshold = 1000;
}
