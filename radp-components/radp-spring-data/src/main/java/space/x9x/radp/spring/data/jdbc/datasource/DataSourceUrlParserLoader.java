package space.x9x.radp.spring.data.jdbc.datasource;

import space.x9x.radp.commons.lang.StringUtils;
import space.x9x.radp.extension.ExtensionLoader;

import javax.sql.DataSource;
import java.util.Set;

/**
 * 数据源地址解释器支持类
 *
 * @author x9x
 * @since 2024-09-30 13:50
 */
public class DataSourceUrlParserLoader {
    /**
     * Default URL to be returned when a data source URL cannot be determined.
     * This constant provides a fallback value for cases where the actual database URL is unavailable.
     */
    public static final String UNKNOWN_URL = "jdbc:database://host:port/unknown_db";

    /**
     * Parses the URL from the provided DataSource by using available DataSourceResolver and DataSourceUrlParser extensions.
     * This method first resolves the DataSource using all available DataSourceResolver extensions,
     * then attempts to extract the URL using available DataSourceUrlParser extensions.
     *
     * @param dataSource the DataSource to parse the URL from
     * @return the parsed URL string, or UNKNOWN_URL if no parser could extract a valid URL
     */
    public static String parse(DataSource dataSource) {
        ExtensionLoader<DataSourceResolver> resolverExtensionLoader = ExtensionLoader.getExtensionLoader(DataSourceResolver.class);
        Set<String> resolverExtensions = resolverExtensionLoader.getSupportedExtensions();
        for (String extension : resolverExtensions) {
            DataSourceResolver resolver = resolverExtensionLoader.getExtension(extension);
            dataSource = resolver.resolveDataSource(dataSource);
        }

        ExtensionLoader<DataSourceUrlParser> extensionLoader = ExtensionLoader.getExtensionLoader(DataSourceUrlParser.class);
        Set<String> extensions = extensionLoader.getSupportedExtensions();
        for (String extension : extensions) {
            DataSourceUrlParser parser = extensionLoader.getExtension(extension);
            String url = parser.getDatasourceUrl(dataSource);
            if (StringUtils.isNoneEmpty(url)) {
                return url;
            }
        }
        return UNKNOWN_URL;
    }
}
