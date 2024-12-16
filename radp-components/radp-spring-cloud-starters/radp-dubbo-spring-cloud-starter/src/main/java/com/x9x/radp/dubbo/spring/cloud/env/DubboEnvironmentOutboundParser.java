package com.x9x.radp.dubbo.spring.cloud.env;

import com.x9x.radp.commons.lang.MessageFormatUtils;
import com.x9x.radp.commons.lang.Strings;
import com.x9x.radp.spring.boot.bootstrap.constants.Conditions;
import com.x9x.radp.spring.boot.bootstrap.env.EnvironmentOutboundParser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;

/**
 * @author x9x
 * @since 2024-10-03 01:20
 */
public class DubboEnvironmentOutboundParser implements EnvironmentOutboundParser {

    private static final String TEMPLATE = "Outbound Dubbo Registry: \t{}";

    @Override
    public String toString(Environment env) {
        boolean disabled = !Boolean.parseBoolean(env.getProperty(DubboEnvironment.ENABLED, Conditions.TRUE));
        if (disabled) {
            return Strings.EMPTY;
        }

        String registryAddress = StringUtils.trimToEmpty(env.getProperty(DubboEnvironment.REGISTRY_ADDRESS));
        return MessageFormatUtils.format(TEMPLATE, registryAddress);
    }
}
