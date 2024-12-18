package space.x9x.radp.spring.framework.beans;

import space.x9x.radp.spring.framework.bootstrap.constant.SpringProperties;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.DefaultBeanNameGenerator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author x9x
 * @since 2024-09-27 00:07
 */
public class ApplicationContextHelper implements ApplicationContextAware, BeanFactoryPostProcessor {

    public static final String SPRING_APPLICATION_NAME = SpringProperties.SPRING_APPLICATION_NAME;
    private static final BeanNameGenerator beanNameGenerator = new DefaultBeanNameGenerator();
    private static ApplicationContext applicationContext;
    private static ConfigurableListableBeanFactory beanFactory;

    @Override
    public void postProcessBeanFactory(@NotNull ConfigurableListableBeanFactory beanFactory) throws BeansException {
        ApplicationContextHelper.beanFactory = beanFactory;
    }

    @Override
    public void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException {
        ApplicationContextHelper.applicationContext = applicationContext;
    }

    public static void registerBean(Class<?> beanClass,
                                    BeanDefinitionRegistry registry) {
        BeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(beanClass).getBeanDefinition();
        String beanName = beanNameGenerator.generateBeanName(beanDefinition, registry);
        registry.registerBeanDefinition(beanName, beanDefinition);
    }
}