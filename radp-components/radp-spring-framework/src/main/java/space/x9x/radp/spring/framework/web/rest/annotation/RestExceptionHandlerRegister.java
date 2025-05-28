package space.x9x.radp.spring.framework.web.rest.annotation;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import org.jetbrains.annotations.NotNull;

import space.x9x.radp.spring.framework.beans.ApplicationContextHelper;
import space.x9x.radp.spring.framework.web.rest.handler.RestExceptionHandler;

/**
 * @author IO x9x
 * @since 2024-09-27 00:05
 */
public class RestExceptionHandlerRegister implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(@NotNull AnnotationMetadata importingClassMetadata,
                                        @NotNull BeanDefinitionRegistry registry) {
        ApplicationContextHelper.registerBean(RestExceptionHandler.class, registry);
    }
}
