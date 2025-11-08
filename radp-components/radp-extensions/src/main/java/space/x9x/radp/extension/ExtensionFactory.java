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

/**
 * 使用SPI（服务提供者接口）注解标记的服务接口. 此接口定义了获取扩展类实例的方法，用于动态加载和创建指定类型的扩展实例。
 * <p>
 * Service interface marked with the SPI (Service Provider Interface) annotation. This
 * interface defines methods for obtaining extension instances, used for dynamically
 * loading and creating extension instances of specified types.
 *
 * @author x9x
 * @since 2024-09-24 11:19
 */
@SPI
public interface ExtensionFactory {

	/**
	 * 根据给定的类类型和名称获取扩展实例.
	 * <p>
	 * Gets an extension instance based on the given class type and name.
	 * @param type the class type of the extension
	 * @param name the identifier of the extension
	 * @param <T> the type of the extension instance to be returned
	 * @return an extension instance of type {@code T}
	 */
	<T> T getExtension(Class<T> type, String name);

}
