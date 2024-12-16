package com.x9x.radp.mybatis.spring.boot.autoconfigure;

import com.x9x.radp.mybatis.spring.boot.env.MybatisPluginProperties;
import com.x9x.radp.spring.data.mybatis.plugin.MybatisSqlLogInterceptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Role;

/**
 * Mybatis 插件扩展自动装配
 *
 * @author x9x
 * @since 2024-09-30 13:34
 */
@ConditionalOnMissingBean(SqlSessionFactory.class)
@ConditionalOnProperty(name = MybatisPluginProperties.SQL_LOG_ENABLED)
@EnableConfigurationProperties(MybatisPluginProperties.class)
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@AutoConfiguration(after = DataSourceAutoConfiguration.class)
@RequiredArgsConstructor
@Slf4j
public class RadpMybatisPluginAutoConfiguration {
    private static final String AUTOWIRED_MYBATIS_SQL_LOG_INTERCEPTOR = "Autowired mybatisSqlLogInterceptor";

    private final MybatisPluginProperties mybatisPluginProperties;

    @Bean
    public MybatisSqlLogInterceptor mybatisSqlLogInterceptor() {
        log.debug(AUTOWIRED_MYBATIS_SQL_LOG_INTERCEPTOR);
        MybatisSqlLogInterceptor interceptor = new MybatisSqlLogInterceptor();
        interceptor.setSlownessThreshold(mybatisPluginProperties.getSqlLog().getSlownessThreshold());
        return interceptor;
    }
}
