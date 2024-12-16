package com.x9x.radp.mybatis.spring.boot.autoconfigure;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Role;

/**
 * Mybatis 分页插件自动装配
 *
 * @author x9x
 * @since 2024-09-30 13:25
 */
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@AutoConfiguration(after = DataSourceAutoConfiguration.class)
@Slf4j
public class RadpMybatisPageHelperAutoConfiguration {

    private static final String AUTOWIRED_MYBATIS_PLUS_INTERCEPTOR = "Autowired mybatisPlusInterceptor";

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        log.debug(AUTOWIRED_MYBATIS_PLUS_INTERCEPTOR);
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        return interceptor;
    }
}
