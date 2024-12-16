package com.x9x.radp.spring.framework.web.rest.annotation;

import com.x9x.radp.spring.framework.beans.ApplicationContextHelper;
import com.x9x.radp.spring.framework.web.rest.handler.RestExceptionHandler;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author x9x
 * @since 2024-09-27 00:05
 */
public class RestExceptionHandlerRegister implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(@NotNull AnnotationMetadata importingClassMetadata,
                                        @NotNull BeanDefinitionRegistry registry) {
        ApplicationContextHelper.registerBean(RestExceptionHandler.class, registry);
    }
}
