package space.x9x.radp.spring.boot.jdbc.env;

import space.x9x.radp.commons.lang.MessageFormatUtils;
import space.x9x.radp.commons.lang.StringUtils;
import space.x9x.radp.commons.lang.Strings;
import space.x9x.radp.spring.boot.bootstrap.env.EnvironmentOutboundParser;
import org.springframework.core.env.Environment;

/**
 * @author x9x
 * @since 2024-09-30 09:38
 */
public class DatasourceEnvironmentOutboundParser implements EnvironmentOutboundParser {

    private static final String TEMPLATE = "Outbound Datasource: \t{}";

    @Override
    public String toString(Environment env) {
        if (!env.containsProperty(DatasourceEnvironment.URL)) {
            return Strings.EMPTY;
        }

        String url = env.getProperty(DatasourceEnvironment.URL);
        if (StringUtils.isNotBlank(url) && url.contains(Strings.PLACEHOLDER)) {
            url = url.substring(0, url.indexOf(Strings.PLACEHOLDER));
        }
        return MessageFormatUtils.format(TEMPLATE, url);
    }
}
