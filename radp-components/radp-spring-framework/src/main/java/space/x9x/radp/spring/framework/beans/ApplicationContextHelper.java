package space.x9x.radp.spring.framework.beans;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.DefaultBeanNameGenerator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import space.x9x.radp.spring.framework.bootstrap.constant.SpringProperties;

/**
 * @author x9x
 * @since 2024-09-27 00:07
 */
public class ApplicationContextHelper implements ApplicationContextAware, BeanFactoryPostProcessor {

    /**
     * Constant for the Spring application name property key.
     * This is used to identify the application in various contexts.
     */
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

    /**
     * Registers a bean in the given registry.
     *
     * @param beanClass the class of the bean to register
     * @param registry  the registry to register the bean in
     */
    public static void registerBean(Class<?> beanClass,
                                    BeanDefinitionRegistry registry) {
        BeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(beanClass).getBeanDefinition();
        String beanName = beanNameGenerator.generateBeanName(beanDefinition, registry);
        registry.registerBeanDefinition(beanName, beanDefinition);
    }

    /**
     * Returns the bean factory.
     *
     * @return the bean factory, either from the beanFactory field or from the applicationContext
     */
    public static ListableBeanFactory getBeanFactory() {
        return beanFactory == null ? applicationContext : beanFactory;
    }

    /**
     * Gets a bean of the specified type from the bean factory.
     * First tries to get the bean by type, then by name derived from the class name.
     *
     * @param clazz the class of the bean to get
     * @param <T> the type of the bean
     * @return the bean instance, or null if not found
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(Class<T> clazz) {
        T beanInstance = null;
        try {
            beanInstance = getBeanFactory().getBean(clazz);
        } catch (Exception ignore) {
            // eat it
        }

        if (beanInstance == null) {
            String simpleName = clazz.getSimpleName();
            String beanName = Character.toLowerCase(simpleName.charAt(0)) + simpleName.substring(1);
            try {
                Object bean = getBeanFactory().getBean(beanName);
                if (clazz.isInstance(bean)) {
                    beanInstance = clazz.cast(bean);
                }
            } catch (Exception ignore) {
                // eat it
            }
        }
        return beanInstance;
    }
}
