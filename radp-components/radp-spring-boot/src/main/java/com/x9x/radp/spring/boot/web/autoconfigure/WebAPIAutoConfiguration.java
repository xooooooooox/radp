package com.x9x.radp.spring.boot.web.autoconfigure;

import com.x9x.radp.spring.boot.web.env.WebAPIProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Role;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author x9x
 * @since 2024-09-30 23:08
 */
@AutoConfiguration
@EnableConfigurationProperties(WebAPIProperties.class)
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@RequiredArgsConstructor
@Slf4j
public class WebAPIAutoConfiguration implements WebMvcConfigurer {


    private final WebAPIProperties properties;

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.addPathPrefix(properties.getPrefix(), clazz -> clazz.isAnnotationPresent(RestController.class));
    }
}
