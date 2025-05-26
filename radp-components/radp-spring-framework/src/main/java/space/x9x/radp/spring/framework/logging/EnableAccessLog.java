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

package space.x9x.radp.spring.framework.logging;

import space.x9x.radp.spring.framework.logging.access.config.AccessLogImportSelector;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;

import java.lang.annotation.*;

/**
 * @author IO x9x
 * @since 2024-09-30 09:53
 */
@Import(AccessLogImportSelector.class)
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EnableAccessLog {

    /**
     * @return 是否开启 CGLIB 代理
     */
    boolean proxyTargetClass() default false;

    /**
     * @return 代理模式。默认 JDK 动态代理
     */
    AdviceMode mode() default AdviceMode.PROXY;

    /**
     * @return PointcutAdvisor 优先级
     */
    int order() default Ordered.LOWEST_PRECEDENCE;

    /**
     * @return AspectJ 切面表达式
     */
    String expression() default "";
}
