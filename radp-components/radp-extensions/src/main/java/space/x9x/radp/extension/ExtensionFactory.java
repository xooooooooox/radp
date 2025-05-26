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
 * 使用SPI（服务提供者接口）注解标记的服务接口。 此接口定义了获取扩展类实例的方法，用于动态加载和创建指定类型的扩展实例。
 *
 * @author IO x9x
 * @since 2024-09-24 11:19
 */
@SPI
public interface ExtensionFactory {

	/**
	 * 根据给定的类类型和名称获取扩展实例
	 * @param type 扩展类的类类型
	 * @param name 扩展列的标识
	 * @param <T> 标识返回的扩展类实例的类型
	 * @return 类型为 {@code T} 的扩展类实例
	 */
	<T> T getExtension(Class<T> type, String name);

}
