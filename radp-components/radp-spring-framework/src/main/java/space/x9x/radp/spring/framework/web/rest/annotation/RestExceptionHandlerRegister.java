/*
 * Copyright 2012-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package space.x9x.radp.spring.framework.web.rest.annotation;

import org.jetbrains.annotations.NotNull;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import space.x9x.radp.spring.framework.beans.ApplicationContextHelper;
import space.x9x.radp.spring.framework.web.rest.handler.RestExceptionHandler;

/**
 * A Spring bean definition registrar that automatically registers the
 * RestExceptionHandler in the application context. This class implements
 * ImportBeanDefinitionRegistrar to programmatically register the exception handler bean.
 *
 * @author RADP x9x
 * @since 2024-09-27 00:05
 */
public class RestExceptionHandlerRegister implements ImportBeanDefinitionRegistrar {

	@Override
	public void registerBeanDefinitions(@NotNull AnnotationMetadata importingClassMetadata,
			@NotNull BeanDefinitionRegistry registry) {
		ApplicationContextHelper.registerBean(RestExceptionHandler.class, registry);
	}

}
