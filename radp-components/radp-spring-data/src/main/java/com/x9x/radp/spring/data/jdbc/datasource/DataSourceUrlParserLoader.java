package com.x9x.radp.spring.data.jdbc.datasource;

import com.x9x.radp.commons.lang.StringUtils;
import com.x9x.radp.extension.ExtensionLoader;

import javax.sql.DataSource;
import java.util.Set;

/**
 * 数据源地址解释器支持类
 *
 * @author x9x
 * @since 2024-09-30 13:50
 */
public class DataSourceUrlParserLoader {
    public static final String UNKNOWN_URL = "jdbc:database://host:port/unknown_db";

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
