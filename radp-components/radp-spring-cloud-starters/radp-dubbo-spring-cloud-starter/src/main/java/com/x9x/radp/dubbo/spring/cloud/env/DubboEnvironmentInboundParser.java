package com.x9x.radp.dubbo.spring.cloud.env;

import com.x9x.radp.commons.lang.MessageFormatUtils;
import com.x9x.radp.commons.lang.StringUtils;
import com.x9x.radp.commons.lang.Strings;
import com.x9x.radp.commons.lang.math.NumberUtils;
import com.x9x.radp.spring.boot.bootstrap.constants.Conditions;
import com.x9x.radp.spring.boot.bootstrap.env.EnvironmentInboundParser;
import org.springframework.core.env.Environment;

/**
 * @author x9x
 * @since 2024-10-03 01:15
 */
public class DubboEnvironmentInboundParser implements EnvironmentInboundParser {
    private static final String TEMPLATE = "Inbound Dubbo Provider: \t{}://{}:{}";

    @Override
    public String toString(Environment env) {
        boolean disabled = !Boolean.parseBoolean(env.getProperty(DubboEnvironment.ENABLED, Conditions.TRUE));
        if (disabled) {
            return Strings.EMPTY;
        }
        String protocol = StringUtils.trimToEmpty(env.getProperty(DubboEnvironment.PROTOCOL));
        int port = NumberUtils.toInt(env.getProperty(DubboEnvironment.PORT));
        return MessageFormatUtils.format(TEMPLATE, protocol, port, protocol);
    }
}
