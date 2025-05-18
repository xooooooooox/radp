package space.x9x.radp.spring.framework.logging.access.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.context.annotation.Role;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import space.x9x.radp.spring.framework.logging.EnableAccessLog;
import space.x9x.radp.spring.framework.logging.access.aop.AccessLogAdvisor;
import space.x9x.radp.spring.framework.logging.access.aop.AccessLogInterceptor;

/**
 * @author x9x
 * @since 2024-09-30 09:50
 */
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@Configuration(proxyBeanMethods = false)
@Slf4j
public class AccessLogConfiguration implements ImportAware {

    private static final String IMPORTING_META_NOT_FOUND = "@EnableAccessLog is not present on importing class";
    private AnnotationAttributes annotationAttributes;

    @Override
    public void setImportMetadata(AnnotationMetadata importMetadata) {
        this.annotationAttributes = AnnotationAttributes.fromMap(
                importMetadata.getAnnotationAttributes(EnableAccessLog.class.getName(), false)
        );
        if (this.annotationAttributes == null) {
            log.warn(IMPORTING_META_NOT_FOUND);
        }
    }

    /**
     * Creates and configures the AccessLogInterceptor bean.
     * This interceptor handles the actual logging of method invocations.
     *
     * @param configs provider for AccessLogConfig instances
     * @return configured AccessLogInterceptor instance
     */
    @Bean
    public AccessLogInterceptor loggingAspectInterceptor(ObjectProvider<AccessLogConfig> configs) {
        return new AccessLogInterceptor(getAccessLogConfig(configs));
    }

    /**
     * Creates and configures the AccessLogAdvisor bean.
     * This advisor defines which methods should be intercepted for logging based on the pointcut expression.
     *
     * @param configs     provider for AccessLogConfig instances
     * @param interceptor the interceptor that will be applied to matched methods
     * @return configured AccessLogAdvisor instance
     */
    @Bean
    public AccessLogAdvisor loggingAspectPointcutAdvisor(ObjectProvider<AccessLogConfig> configs,
                                                         AccessLogInterceptor interceptor) {
        AccessLogAdvisor accessLogAdvisor = new AccessLogAdvisor();
        String expression = getAccessLogConfig(configs).getExpression();
        accessLogAdvisor.setExpression(expression);
        accessLogAdvisor.setAdvice(interceptor);
        if (annotationAttributes == null) {
            accessLogAdvisor.setOrder(annotationAttributes.getNumber("order"));
        }
        return accessLogAdvisor;
    }

    private AccessLogConfig getAccessLogConfig(ObjectProvider<AccessLogConfig> accessLogConfigs) {
        return accessLogConfigs.getIfUnique(() -> {
            AccessLogConfig config = new AccessLogConfig();
            config.setExpression(annotationAttributes.getString("expression"));
            return config;
        });
    }
}
