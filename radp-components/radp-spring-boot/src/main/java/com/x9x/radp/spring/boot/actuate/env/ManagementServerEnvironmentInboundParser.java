package com.x9x.radp.spring.boot.actuate.env;

import com.x9x.radp.commons.lang.MessageFormatUtils;
import com.x9x.radp.commons.lang.StringUtils;
import com.x9x.radp.commons.lang.Strings;
import com.x9x.radp.commons.lang.math.NumberUtils;
import com.x9x.radp.commons.net.IpConfigUtils;
import com.x9x.radp.spring.boot.bootstrap.env.EnvironmentInboundParser;
import org.springframework.core.env.Environment;

/**
 * @author x9x
 * @since 2024-09-30 09:21
 */
public class ManagementServerEnvironmentInboundParser implements EnvironmentInboundParser {
    private static final String TEMPLATE = "Inbound Management Server: \t{}://{}:{}{}";
    private static final String PROTOCOL = "http";

    @Override
    public String toString(Environment env) {
        if (!env.containsProperty(ManagementServerEnvironment.PORT)) {
            return Strings.EMPTY;
        }

        int port = NumberUtils.toInt(env.getProperty(ManagementServerEnvironment.PORT));
        String basePath = StringUtils.trimToEmpty(env.getProperty(ManagementServerEnvironment.ENDPOINTS_WEB_BASE_PATH));
        return MessageFormatUtils.format(TEMPLATE, PROTOCOL, IpConfigUtils.getIpAddress(), port, basePath);
    }
}
