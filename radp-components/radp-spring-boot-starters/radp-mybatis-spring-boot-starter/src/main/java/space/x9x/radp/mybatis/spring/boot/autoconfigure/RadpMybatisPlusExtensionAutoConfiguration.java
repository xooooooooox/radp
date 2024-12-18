package space.x9x.radp.mybatis.spring.boot.autoconfigure;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import space.x9x.radp.mybatis.spring.boot.env.MybatisPlusExtensionProperties;
import space.x9x.radp.spring.data.mybatis.autofill.AutofillMetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Role;

/**
 * @author x9x
 * @since 2024-09-30 14:45
 */
@Slf4j
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@EnableConfigurationProperties(MybatisPlusExtensionProperties.class)
@ConditionalOnClass({
        SqlSessionFactory.class,
        SqlSessionFactoryBean.class,
        MybatisConfiguration.class
})
@ConditionalOnProperty(name = MybatisPlusExtensionProperties.AUTO_FILL_ENABLED, havingValue = "true")
@AutoConfiguration(after = MybatisPlusAutoConfiguration.class)
public class RadpMybatisPlusExtensionAutoConfiguration {

    private static final String AUTOWIRED_META_OBJECT_HANDLER = "Autowired metaObjectHandler";

    @ConditionalOnMissingBean
    @Bean
    public MetaObjectHandler metaObjectHandler(MybatisPlusExtensionProperties properties) {
        log.debug(AUTOWIRED_META_OBJECT_HANDLER);
        return new AutofillMetaObjectHandler(properties.getAutoFill().getCreatedDataFieldName(), properties.getAutoFill().getLastModifiedDateFieldName());
    }
}
