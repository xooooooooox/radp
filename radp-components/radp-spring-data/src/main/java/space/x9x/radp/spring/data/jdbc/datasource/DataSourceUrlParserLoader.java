package space.x9x.radp.spring.data.jdbc.datasource;

import java.util.Set;

import javax.sql.DataSource;

import space.x9x.radp.commons.lang.StringUtils;
import space.x9x.radp.extension.ExtensionLoader;

/**
 * 数据源地址解释器支持类
 *
 * @author IO x9x
 * @since 2024-09-30 13:50
 */
public class DataSourceUrlParserLoader {
    /**
     * Default URL to be returned when no parser can determine the actual database URL.
     * This constant is used as a fallback when all available parsers fail to extract
     * a valid URL from the provided DataSource.
     */
    public static final String UNKNOWN_URL = "jdbc:database://host:port/unknown_db";

    /**
     * Parses the JDBC URL from the provided DataSource.
     * This method attempts to extract the database URL by trying all available
     * DataSourceResolver and DataSourceUrlParser extensions. If no parser can
     * determine the URL, it returns the UNKNOWN_URL constant.
     *
     * @param dataSource the DataSource to extract the URL from
     * @return the extracted database URL, or UNKNOWN_URL if no parser could determine it
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
