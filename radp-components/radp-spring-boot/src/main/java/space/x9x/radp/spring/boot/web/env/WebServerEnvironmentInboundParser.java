package space.x9x.radp.spring.boot.web.env;

import space.x9x.radp.commons.lang.MessageFormatUtils;
import space.x9x.radp.commons.lang.StringUtils;
import space.x9x.radp.commons.lang.Strings;
import space.x9x.radp.commons.lang.math.NumberUtils;
import space.x9x.radp.commons.net.IpConfigUtils;
import space.x9x.radp.spring.boot.bootstrap.env.EnvironmentInboundParser;
import org.springframework.core.env.Environment;

/**
 * @author x9x
 * @since 2024-09-30 09:28
 */
public class WebServerEnvironmentInboundParser implements EnvironmentInboundParser {
    private static final String TEMPLATE = "Inbound Web Server: \t{}://{}:{}{}";

    @Override
    public String toString(Environment env) {
        if (!env.containsProperty(WebServerEnvironment.SERVER_PORT)) {
            return Strings.EMPTY;
        }

        boolean protocol = env.containsProperty(WebServerEnvironment.SERVER_SSL_KEY_STORE);
        int port = NumberUtils.toInt(WebServerEnvironment.SERVER_PORT);
        String contextPath = StringUtils.trimToEmpty(env.getProperty(WebServerEnvironment.SERVER_SERVLET_CONTEXT_PATH));
        return MessageFormatUtils.format(TEMPLATE, protocol, IpConfigUtils.getIpAddress(), port, contextPath);
    }
}
