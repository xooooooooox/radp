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

package space.x9x.radp.extension.adaptive;

import lombok.RequiredArgsConstructor;
import space.x9x.radp.commons.lang.ClassLoaderUtils;
import space.x9x.radp.extension.ExtensionLoader;
import space.x9x.radp.extension.compile.Compiler;
import space.x9x.radp.extension.util.Holder;

/**
 * @author IO x9x
 * @since 2024-09-24 15:55
 */
@RequiredArgsConstructor
@SuppressWarnings({ "java:S3077", "unchecked" })
public class AdaptiveExtensionLoader<T> {

	/**
	 * 缓存自适应扩展点实例
	 */
	private final Holder<Object> cachedAdaptiveInstance = new Holder<>();

	/**
	 * 缓存自适应扩展类
	 */
	private volatile Class<?> cachedAdaptiveClass = null;

	/**
	 * 创建自适应实例时的错误信息
	 */
	private volatile Throwable createAdaptiveInstanceError;

	/**
	 * 扩展点加载器
	 */
	private final ExtensionLoader<T> extensionLoader;

	/**
	 * 获取自适应扩展实例
	 * @return 自适应扩展实例
	 * @throws IllegalStateException 如果创建自适应实例失败
	 */
	public T getAdaptiveExtension() {
		// 从缓存中获取已有的自适应扩展点实例
		Object instance = cachedAdaptiveInstance.get();
		// 如果缓存中没有实例，则需要创建一个新的实例
		if (instance == null) {
			// 如果之前创建自适应扩展点实例时发生过错误，记录该错误并抛出异常
			if (createAdaptiveInstanceError != null) {
				throw new IllegalStateException(
						"Failed to create adaptive instance: " + createAdaptiveInstanceError.toString(),
						createAdaptiveInstanceError);
			}

			// 同步块确保线程安全地创建和缓存自适应扩展点实例
			synchronized (cachedAdaptiveInstance) {
				// 再次检查实例是否存在，以防在进入同步块前被其他线程创建
				instance = cachedAdaptiveInstance.get();
				if (instance == null) {
					try {
						instance = createAdaptiveExtension();
						cachedAdaptiveInstance.set(instance);
					}
					catch (Throwable th) {
						createAdaptiveInstanceError = th;
						throw new IllegalStateException("Failed to create adaptive instance: " + th.toString(), th);
					}
				}
			}
		}
		return (T) instance;
	}

	/**
	 * 创建自适应扩展实例
	 * @return 自适应扩展实例
	 * @throws IllegalStateException 如果无法创建自适应扩展
	 */
	private T createAdaptiveExtension() {
		try {
			return extensionLoader
				.injectExtension((T) getAdaptiveExtensionClass().getDeclaredConstructor().newInstance());
		}
		catch (Exception ex) {
			throw new IllegalStateException(
					"Can't create adaptive extension " + extensionLoader.getType() + ", cause: " + ex.getMessage(), ex);
		}
	}

	/**
	 * 缓存适应性类 该方法用于缓存一个适应性类，如果已经缓存了一个不同的适应性类，则抛出异常
	 * @param clazz 要缓存的适应性类
	 * @param overridden 是否覆盖已缓存的适应性类 如果设为true，允许覆盖当前缓存的适应性类；
	 * 如果设为false且cachedAdaptiveClass不为空且不等于clazz， 则抛出IllegalArgumentException
	 */
	public void cachedAdaptiveClass(Class<?> clazz, boolean overridden) {
		// 当 cachedAdaptiveClass为空或者要求覆盖已缓存的类时
		// 将 clazz 赋值给 cachedAdaptiveClass
		if (cachedAdaptiveClass == null || overridden) {
			cachedAdaptiveClass = clazz;
		}
		else if (!cachedAdaptiveClass.equals(clazz)) {
			// 如果已缓存了一个不同的适应性类, 抛出异常
			throw new IllegalStateException(
					"More than one adaptive class found: " + cachedAdaptiveClass.getName() + ", " + clazz.getName());
		}
	}

	/**
	 * 获取已加载的自适应扩展实例
	 * @return 返回缓存的自适应实例对象，如果未缓存则可能返回null
	 */
	public Object getLoadedAdaptiveExtensionInstances() {
		return cachedAdaptiveInstance.get();
	}

	/**
	 * 获取自适应扩展类
	 * @return 自适应扩展类
	 */
	private Class<?> getAdaptiveExtensionClass() {
		// 确保扩展类只被加载一次
		extensionLoader.getExtensionClasses();
		if (cachedAdaptiveClass != null) {
			return cachedAdaptiveClass;
		}
		cachedAdaptiveClass = createAdaptiveExtensionClass();
		return cachedAdaptiveClass;
	}

	/**
	 * 创建自适应扩展类
	 * @return 新的自适应扩展类
	 */
	private Class<?> createAdaptiveExtensionClass() {
		// 生成自适应扩展类的字节码
		String code = new AdaptiveClassCodeGenerator(extensionLoader.getType(), extensionLoader.getCachedDefaultName())
			.generate();
		// 获取类加载器
		ClassLoader classLoader = ClassLoaderUtils.getClassLoader(ExtensionLoader.class);
		// 获取编译器扩展
		Compiler compiler = ExtensionLoader.getExtensionLoader(Compiler.class).getAdaptiveExtension();
		// 编译字节码并返回类
		return compiler.compile(code, classLoader);
	}

}
