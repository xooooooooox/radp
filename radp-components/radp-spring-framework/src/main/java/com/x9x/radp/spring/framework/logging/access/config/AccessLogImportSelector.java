package com.x9x.radp.spring.framework.logging.access.config;

import com.x9x.radp.spring.framework.logging.EnableAccessLog;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.AdviceModeImportSelector;
import org.springframework.context.annotation.AutoProxyRegistrar;

/**
 * 访问日志切面装配选择器
 * @author x9x
 * @since 2024-09-30 09:56
 */
public class AccessLogImportSelector extends AdviceModeImportSelector<EnableAccessLog> {
    @Override
    protected String[] selectImports(AdviceMode adviceMode) {
        switch (adviceMode) {
            case PROXY:
                return new String[]{
                        AutoProxyRegistrar.class.getName(),
                        AccessLogConfiguration.class.getName()
                };
            case ASPECTJ:
                return new String[]{
                        AccessLogConfiguration.class.getName()
                };
            default:
        }
        return new String[0];
    }
}
