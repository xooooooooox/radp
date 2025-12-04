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

package space.x9x.radp.extension;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 这是一个用于标识SPI（Service Provider Interface）的注解.
 * 它的主要作用是标记一个接口为SPI接口，以便于Java运行时环境可以找到并加载相应的实现类。 该注解会被文档工具记录，并在运行时保留，允许通过反射机制获取。
 * <p>
 * This is an annotation used to identify a Service Provider Interface (SPI). Its main
 * purpose is to mark an interface as an SPI interface, allowing the Java runtime
 * environment to find and load the corresponding implementation classes. This annotation
 * is documented by documentation tools and retained at runtime, allowing it to be
 * accessed through reflection.
 *
 * @author RADP x9x
 * @since 2024-09-24 11:20
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SPI {

	/**
	 * Specifies the default implementation name for this SPI. When a client requests an
	 * implementation without specifying a name, the implementation with this name will be
	 * returned.
	 * @return the default implementation name, or an empty string if no default is
	 * specified
	 */
	String value() default "";

}
