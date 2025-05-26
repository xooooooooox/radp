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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import space.x9x.radp.commons.collections.CollectionUtils;
import space.x9x.radp.commons.lang.ArrayUtils;
import space.x9x.radp.commons.lang.ClassLoaderUtils;
import space.x9x.radp.commons.lang.StringUtils;
import space.x9x.radp.commons.lang.reflect.ReflectionUtils;
import space.x9x.radp.extension.active.ActiveExtensionLoader;
import space.x9x.radp.extension.adaptive.AdaptiveExtensionLoader;
import space.x9x.radp.extension.common.Constants;
import space.x9x.radp.extension.context.Lifecycle;
import space.x9x.radp.extension.strategy.LoadingStrategy;
import space.x9x.radp.extension.strategy.LoadingStrategyHolder;
import space.x9x.radp.extension.util.Holder;
import space.x9x.radp.extension.wrapper.WrapperComparator;
import space.x9x.radp.extension.wrapper.WrapperExtensionLoader;

/**
 * @author IO x9x
 * @since 2024-09-24 11:28
 */
@RequiredArgsConstructor
@Slf4j
public class ExtensionLoader<T> {

	/**
	 * 扩展点加载器(延迟加载)
	 */
	private static final ConcurrentMap<Class<?>, ExtensionLoader<?>> EXTENSION_LOADERS = new ConcurrentHashMap<>(64);

	/**
	 * 扩展点实例(延迟加载)
	 */
	private static final ConcurrentMap<Class<?>, Object> EXTENSION_INSTANCES = new ConcurrentHashMap<>(64);

	/**
	 * 缓存扩展点名称
	 */
	private final ConcurrentMap<Class<?>, String> cachedNames = new ConcurrentHashMap<>();

	/**
	 * 缓存扩展点 Class (重点)
	 */
	private final Holder<Map<String, Class<?>>> cachedClasses = new Holder<>();

	/**
	 * 缓存扩展点实例
	 */
	private final ConcurrentMap<String, Holder<Object>> cachedInstances = new ConcurrentHashMap<>();

	/**
	 * 缓存异常信息
	 */
	private final Map<String, IllegalStateException> exceptions = new ConcurrentHashMap<>();

	/**
	 * 处理 @Active 扩展点
	 */
	private final ActiveExtensionLoader<T> activeExtensionLoader = new ActiveExtensionLoader<>(this);

	/**
	 * 处理 @Wrapper 扩展点
	 */
	private final WrapperExtensionLoader wrapperExtensionLoader = new WrapperExtensionLoader();

	/**
	 * 处理 @Adaptive 扩展点
	 */
	private final AdaptiveExtensionLoader<T> adaptiveExtensionLoader = new AdaptiveExtensionLoader<>(this);

	/**
	 * 缓存默认的扩展点名称
	 */
	@Getter
	private String cachedDefaultName;

	/**
	 * 扩展点类型
	 */
	@Getter
	private final Class<?> type;

	/**
	 * 扩展点实例工厂
	 */
	private final ExtensionFactory objectFactory;

	private ExtensionLoader(Class<?> type) {
		this.type = type;
		this.objectFactory = (type == ExtensionFactory.class ? null
				: ExtensionLoader.getExtensionLoader(ExtensionFactory.class).getAdaptiveExtension());
	}

	/**
	 * 获取指定类型的扩展加载器 该方法用于获取一个扩展加载器实例，该实例负责根据指定类型（接口）加载其实现类
	 * 首先检查指定类型是否正确注解，然后从缓存中获取对应的扩展加载器， 如果没有则创建一个新的扩展加载器并放入缓存中
	 * @param <T> 扩展点类型参数，表示要加载的扩展点接口类型
	 * @param type 扩展接口类型，即需要加载扩展的接口类型
	 * @return 返回该类型的扩展加载器实例，通过该实例可以加载并获取该接口的所有实现类
	 */
	@SuppressWarnings("unchecked")
	public static <T> ExtensionLoader<T> getExtensionLoader(Class<T> type) {
		// 检查指定类型是否正确地使用了扩展点注解
		checkExtensionAnnotation(type);

		ExtensionLoader<T> loader = (ExtensionLoader<T>) EXTENSION_LOADERS.get(type);
		if (loader == null) {
			EXTENSION_LOADERS.putIfAbsent(type, new ExtensionLoader<>(type));
			loader = (ExtensionLoader<T>) EXTENSION_LOADERS.get(type);
		}
		return loader;
	}

	/**
	 * 检查扩展类型是否符合规范 此方法用于确保传入的扩展类型（接口）符合特定的规则：非空、为接口类型且带有SPI注解
	 * 它是为了保证扩展机制的正确性而设计的，对于不符合规范的类型，会抛出异常，阻止其被用作扩展
	 * @param type 扩展类型，必须是一个带有SPI注解的接口
	 * @throws IllegalArgumentException 如果扩展类型为null、不是接口或者没有SPI注解，则抛出此异常
	 */
	private static <T> void checkExtensionAnnotation(Class<T> type) {
		if (type == null) {
			throw new IllegalArgumentException("Extension type == null");
		}
		if (!type.isInterface()) {
			throw new IllegalArgumentException("Extension type(" + type + ") is not interface");
		}
		if (!type.isAnnotationPresent(SPI.class)) {
			throw new IllegalArgumentException("Extension type(" + type
					+ ") is not extension, because it is not annotated with @" + SPI.class.getSimpleName());
		}
	}

	/**
	 * 获取自适应扩展实例
	 * @return 自适应扩展的实例
	 */
	public T getAdaptiveExtension() {
		return adaptiveExtensionLoader.getAdaptiveExtension();
	}

	/**
	 * 为给定实例中的字段注入扩展
	 * @param instance 要注入扩展的实例
	 * @return 注入扩展后的实例
	 */
	public T injectExtension(T instance) {
		// 如果对象工厂为空, 则直接返回实例
		if (objectFactory == null) {
			return instance;
		}

		try {
			// 遍历实例的所有方法
			for (Method method : instance.getClass().getMethods()) {
				// 如果方法不是 setter 方法, 则跳过
				if (!ReflectionUtils.isSetter(method)) {
					continue;
				}

				// 获取 setter 方法的参数类型
				Class<?> parameterType = method.getParameterTypes()[0];
				// 如果参数类型是基本类型, 则跳过
				if (ReflectionUtils.isPrimitive(parameterType)) {
					continue;
				}

				// 获取 setter 方法对应属性的名称
				String property = ReflectionUtils.getSetterProperty(method);
				// 获取 @Inject 注解
				Inject inject = method.getAnnotation(Inject.class);
				if (inject == null) {
					// 如果没有 @Inject 注解, 直接进行值注入
					injectValue(instance, method, parameterType, property);
				}
				else {
					// 如果有 @Inject 注解
					if (!inject.enable()) {
						continue;
					}
					// 根据注解的类型进行不同的注入操作
					if (inject.type() == Inject.InjectType.BY_TYPE) {
						injectValue(instance, method, parameterType, null);
					}
					else {
						injectValue(instance, method, parameterType, property);
					}
				}
			}
		}
		catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		}
		return instance;
	}

	private void injectValue(T instance, Method method, Class<?> parameterType, String property) {
		Object object = objectFactory.getExtension(parameterType, property);
		if (object != null) {
			try {
				method.invoke(instance, object);
			}
			catch (Exception ex) {
				log.error("Failed to inject via method {} of interface {}: {}", method.getName(),
						parameterType.getName(), ex.getMessage(), ex);
			}
		}
	}

	/**
	 * 获取所有扩展类的映射
	 * @return 包含所有扩展类的映射，其中键是扩展名，值是对应的类
	 */
	public Map<String, Class<?>> getExtensionClasses() {
		Map<String, Class<?>> classes = cachedClasses.get();
		if (classes == null) {
			synchronized (cachedClasses) {
				classes = cachedClasses.get();
				if (classes == null) {
					// 缓存默认的扩展点名称
					this.cacheDefaultExtensionName();
					// 遍历 LoadingStrategy, 获取扩展点 classes
					classes = this.loadExtensionClasses();
					cachedClasses.set(classes);
				}
			}
		}
		return classes;
	}

	/**
	 * 缓存默认的扩展点名称
	 */
	private void cacheDefaultExtensionName() {
		final SPI defaultAnnotation = type.getAnnotation(SPI.class);
		if (defaultAnnotation == null) {
			return;
		}
		String value = defaultAnnotation.value().trim();
		if (!value.isEmpty()) {
			String[] names = Constants.COMMA_SPLIT_PATTERN.split(value);
			if (names.length > 1) {
				throw new IllegalArgumentException("More than 1 default extension name on extension " + type.getName()
						+ ": " + Arrays.toString(names));
			}
			if (names.length == 1) {
				this.cachedDefaultName = names[0];
			}
		}
	}

	private Map<String, Class<?>> loadExtensionClasses() {
		Map<String, Class<?>> extensionClasses = new HashMap<>();
		for (LoadingStrategy strategy : LoadingStrategyHolder.strategies) {
			loadDirectory(extensionClasses, strategy.directory(), type.getName(), strategy.preferExtensionClassLoader(),
					strategy.overridden(), strategy.excludedPackages());
		}
		return extensionClasses;
	}

	/**
	 * 加载指定目录下的扩展类
	 * @param extensionClasses 扩展类的映射表，键为扩展类的名称，值为扩展类的Class对象
	 * @param directory 扩展类文件所在的目录
	 * @param type 扩展类的类型（通常为接口的名称）
	 * @param extensionLoaderClassLoaderFirst 是否优先使用ExtensionLoader类的类加载器去加载资源
	 * @param overridden 是否允许已加载的类被覆盖
	 * @param excludedPackages 被排除的包名称数组
	 */
	private void loadDirectory(Map<String, Class<?>> extensionClasses, String directory, String type,
			boolean extensionLoaderClassLoaderFirst, boolean overridden, String... excludedPackages) {
		// 构造资源文件的完整路径
		String fileName = directory + type;
		try {
			Enumeration<URL> urls = null;
			// 获取当前类的类加载器
			ClassLoader classLoader = ClassLoaderUtils.getClassLoader(ExtensionLoader.class);

			// 如果设置了优先使用ExtensionLoader的类加载器
			if (extensionLoaderClassLoaderFirst) {
				ClassLoader extensionLoaderClassLoader = ExtensionLoader.class.getClassLoader();
				if (ClassLoader.getSystemClassLoader() != extensionLoaderClassLoader) {
					// 尝试使用ExtensionLoader的类加载器加载资源
					urls = extensionLoaderClassLoader.getResources(fileName);
				}
			}
			if (urls == null || !urls.hasMoreElements()) {
				if (classLoader != null) {
					// 尝试使用当前类的类加载器加载资源
					urls = classLoader.getResources(fileName);
				}
				else {
					// 尝试使用系统的类加载器加载资源
					urls = ClassLoader.getSystemResources(fileName);
				}
			}
			if (urls != null) {
				while (urls.hasMoreElements()) {
					URL resourceURL = urls.nextElement();
					// 加载并解析每个资源
					loadResource(extensionClasses, classLoader, resourceURL, overridden, excludedPackages);
				}
			}
		}
		catch (IOException ex) {
			log.error("Exception occurred when loading extension class (interface: {}, description file: {}).", type,
					fileName, ex);
		}
	}

	/**
	 * 加载扩展类资源
	 * @param extensionClasses 保存扩展类的Map，键为扩展类的名称，值为扩展类的Class对象
	 * @param classLoader 类加载器，用于加载类
	 * @param resourceURL 资源文件的URL，指向包含类信息的配置文件
	 * @param overridden 是否允许覆盖，如果为true，允许通过配置文件加载的类覆盖现有类
	 * @param excludedPackages 排除的包名数组，用于控制哪些包的类不应该被加载
	 */
	private void loadResource(Map<String, Class<?>> extensionClasses, ClassLoader classLoader, URL resourceURL,
			boolean overridden, String... excludedPackages) {
		try (BufferedReader reader = new BufferedReader(
				new InputStreamReader(resourceURL.openStream(), StandardCharsets.UTF_8))) {
			String line;
			String clazz;
			while ((line = reader.readLine()) != null) {
				final int ci = line.indexOf('#');
				if (ci >= 0) {
					line = line.substring(0, ci);
				}
				line = line.trim();
				if (!line.isEmpty()) {
					try {
						String name = null;
						int i = line.indexOf('=');
						if (i > 0) {
							name = line.substring(0, i).trim();
							// 要加载的类
							clazz = line.substring(i + 1).trim();
						}
						else {
							clazz = line;
						}
						if (StringUtils.isNotEmpty(clazz) && !isExcluded(clazz, excludedPackages)) {
							loadClass(extensionClasses, resourceURL, Class.forName(clazz, true, classLoader), name,
									overridden);
						}
					}
					catch (Exception ex) {
						IllegalStateException illegalStateException = new IllegalStateException(
								"Failed to load extension class (interface: " + type + ", class line: " + line + ") in "
										+ resourceURL + ", cause: " + ex.getMessage(),
								ex);
						exceptions.put(line, illegalStateException);
					}
				}
			}
		}
		catch (Exception ex) {
			log.error("Exception occurred when loading extension class (interface: {}, class file: {}) in {}", type,
					resourceURL, resourceURL, ex);
		}
	}

	private void loadClass(Map<String, Class<?>> extensionClasses, URL resourceURL, Class<?> clazz, String name,
			boolean overridden) throws NoSuchMethodException {
		if (!type.isAssignableFrom(clazz)) {
			throw new IllegalArgumentException(
					"Error occurred when loading extension class (interface: " + type + ", class line: "
							+ clazz.getName() + "), class " + clazz.getName() + " is not subtype of interface.");
		}

		// 处理 @Adaptive 注解
		if (isAdaptiveClass(clazz)) {
			adaptiveExtensionLoader.cachedAdaptiveClass(clazz, overridden);
			return;
		}

		// 处理 @Wrapper 注解
		if (isWrapperClass(clazz)) {
			wrapperExtensionLoader.cacheWrapperClass(clazz);
			return;
		}

		// 检测 clazz 是否有默认的构造方法, 如果没有, 则抛出异常
		clazz.getConstructor();

		// 如果名称为空, 从类名匹配
		if (StringUtils.isEmpty(name)) {
			name = findExtensionName(clazz);
			if (name.isEmpty()) {
				throw new IllegalStateException(
						"No such extension name for the class " + clazz.getName() + " in the config " + resourceURL);
			}
		}

		// 根据名称缓存扩展类
		String[] names = Constants.COMMA_SPLIT_PATTERN.split(name);
		if (ArrayUtils.isNotEmpty(names)) {
			activeExtensionLoader.cacheActiveClass(clazz, names[0]);
			for (String n : names) {
				saveInCacheName(clazz, n);
				saveInExtensionClass(extensionClasses, clazz, n, overridden);
			}
		}
	}

	/**
	 * 缓存扩展类名称
	 * @param clazz 扩展类类型
	 * @param name 扩展类名称
	 */
	private void saveInCacheName(Class<?> clazz, String name) {
		cachedNames.computeIfAbsent(clazz, k -> name);
	}

	/**
	 * 保存扩展类到扩展类映射中 如果存在相同名称的扩展类，默认情况下优先使用已存在的类除非允许覆盖 如果两个类都有Order注解，优先使用Order值较小的类
	 * @param extensionClasses 扩展类映射
	 * @param clazz 待保存的扩展类
	 * @param name 扩展类名
	 * @param overridden 是否允许覆盖已存在的类
	 */
	private void saveInExtensionClass(Map<String, Class<?>> extensionClasses, Class<?> clazz, String name,
			boolean overridden) {
		// 尝试获取已存在的同名扩展类
		Class<?> c = extensionClasses.get(name);
		if (c == null || overridden) {
			extensionClasses.put(name, clazz);
		}
		else {
			if (clazz.isAnnotationPresent(Order.class) || c.isAnnotationPresent(Order.class)) {
				// 获取当前类的Order注解和值
				Order destOrder = clazz.getAnnotation(Order.class);
				int destValue = destOrder != null ? destOrder.value() : 0;
				// 获取已存在类的Order注解和值
				Order srcOrder = c.getAnnotation(Order.class);
				int srcValue = srcOrder != null ? srcOrder.value() : 0;
				// 如果当前类的Order值小于已存在类的Order值，则优先使用当前类
				if (srcValue > destValue) {
					log.debug("Compare extension {} name {} use {} instead of {}", type.getName(), name,
							clazz.getName(), c.getName());
					extensionClasses.put(name, clazz);
				}
				// 否则，优先使用已存在类
				log.debug("Compare extension {} name {} use {} instead of {}", type.getName(), name, c.getName(),
						clazz.getName());
				return;
			}

			// 如果到达这里，说明有重复的扩展类名，抛出异常
			String duplicateMsg = "Duplicate extension " + type.getName() + " name " + name + " on " + c.getName()
					+ " and " + clazz.getName();
			log.error(duplicateMsg);
			throw new IllegalStateException(duplicateMsg);
		}
	}

	/**
	 * Gets all supported extension names for this extension type. This method returns an
	 * unmodifiable set of all extension names that have been loaded or can be loaded for
	 * the current extension type.
	 * @return an unmodifiable set of all supported extension names
	 */
	public Set<String> getSupportedExtensions() {
		Map<String, Class<?>> extensionClasses = getExtensionClasses();
		return Collections.unmodifiableSet(new TreeSet<>(extensionClasses.keySet()));
	}

	/**
	 * Gets the extension with the specified name. This method is a convenience method
	 * that calls {@link #getExtension(String, boolean)} with wrapping enabled.
	 * @param name the name of the extension to get
	 * @return the extension with the specified name
	 * @throws IllegalArgumentException if the name is null or empty
	 */
	public T getExtension(String name) {
		return getExtension(name, true);
	}

	/**
	 * Gets the extension with the specified name, with optional wrapping. This method
	 * retrieves an extension instance by its name. If the extension has not been loaded
	 * yet, it will be created and initialized. If wrapping is enabled, the extension will
	 * be wrapped with any applicable wrapper classes.
	 * @param name the name of the extension to get
	 * @param wrap whether to wrap the extension with wrapper classes
	 * @return the extension with the specified name
	 * @throws IllegalArgumentException if the name is null or empty
	 */
	@SuppressWarnings("unchecked")
	public T getExtension(String name, boolean wrap) {
		if (StringUtils.isEmpty(name)) {
			throw new IllegalArgumentException("Extension name == null");
		}
		final Holder<Object> holder = getOrCreateHolder(name);
		Object instance = holder.get();
		if (instance == null) {
			synchronized (holder) {
				instance = holder.get();
				if (instance == null) {
					instance = createExtension(name, wrap);
					holder.set(instance);
				}
			}
		}
		return (T) instance;
	}

	/**
	 * Gets the extension with the specified name, or the default extension if the
	 * specified name doesn't exist. This method provides a convenient way to get an
	 * extension while falling back to the default when the requested extension is not
	 * available.
	 * @param name the name of the extension to get
	 * @return the extension with the specified name, or the default extension if the
	 * specified name doesn't exist
	 */
	public T getOrDefaultExtension(String name) {
		return containsExtension(name) ? getExtension(name) : getDefaultExtension();
	}

	private boolean containsExtension(String name) {
		return getExtensionClasses().containsKey(name);
	}

	@SuppressWarnings("unchecked")
	private T createExtension(String name, boolean wrap) {
		Class<?> clazz = getExtensionClasses().get(name);
		if (clazz == null) {
			throw findException(name);
		}
		try {
			T instance = (T) EXTENSION_INSTANCES.get(clazz);
			if (instance == null) {
				EXTENSION_INSTANCES.putIfAbsent(clazz, clazz.getConstructor().newInstance());
				instance = (T) EXTENSION_INSTANCES.get(clazz);
			}
			injectExtension(instance);

			if (wrap) {
				List<Class<?>> wrapperClassesList = new ArrayList<>();
				if (wrapperExtensionLoader.getCachedWrapperClasses() != null) {
					wrapperClassesList.addAll(wrapperExtensionLoader.getCachedWrapperClasses());
					wrapperClassesList.sort(WrapperComparator.COMPARATOR);
					Collections.reverse(wrapperClassesList);
				}
				if (CollectionUtils.isNotEmpty(wrapperClassesList)) {
					for (Class<?> wrapperClass : wrapperClassesList) {
						Wrapper wrapper = wrapperClass.getAnnotation(Wrapper.class);
						if (wrapper == null || (ArrayUtils.contains(wrapper.matches(), name)
								&& !ArrayUtils.contains(wrapper.mismatches(), name))) {
							instance = injectExtension((T) wrapperClass.getConstructor(type).newInstance(instance));
						}
					}
				}
			}

			initExtension(instance);
			return instance;
		}
		catch (Exception ex) {
			throw new IllegalStateException("Extension instance (name: " + name + ", class: " + type
					+ ") couldn't be instantiated: " + ex.getMessage(), ex);
		}
	}

	// TODO 2024/9/24: 初始化扩展点
	private void initExtension(T instance) {
		if (instance instanceof Lifecycle) {
			Lifecycle lifecycle = (Lifecycle) instance;
			lifecycle.initialize();
		}
	}

	private IllegalStateException findException(String name) {
		StringBuilder buf = new StringBuilder("No such extension " + type.getName() + " by name " + name);

		int i = 1;
		for (Map.Entry<String, IllegalStateException> entry : exceptions.entrySet()) {
			if (entry.getKey().toLowerCase().startsWith(name.toLowerCase())) {
				if (i == 1) {
					buf.append(", possible causes: ");
				}
				buf.append("\r\n(");
				buf.append(i++);
				buf.append(") ");
				buf.append(entry.getKey());
				buf.append(":\r\n");
				buf.append(entry.getValue());
			}
		}

		if (i == 1) {
			buf.append(", no related exception was found, please check whether related SPI module is missing.");
		}
		return new IllegalStateException(buf.toString());
	}

	private Holder<Object> getOrCreateHolder(String name) {
		Holder<Object> holder = cachedInstances.get(name);
		if (holder == null) {
			// 如果持有者为空, 这创建一个对象并放入缓存
			cachedInstances.putIfAbsent(name, new Holder<>());
			holder = cachedInstances.get(name);
		}
		return holder;
	}

	/**
	 * Gets the extension class with the specified name. This method returns the Class
	 * object for the extension with the given name. It can be used to inspect the
	 * extension class without instantiating it.
	 * @param name the name of the extension class to get
	 * @return the Class object for the extension with the specified name, or null if not
	 * found
	 * @throws IllegalArgumentException if the extension type or name is null
	 */
	public Class<?> getExtensionClass(String name) {
		if (type == null) {
			throw new IllegalArgumentException("Extension type == null");
		}
		if (name == null) {
			throw new IllegalArgumentException("Extension name == null");
		}
		return getExtensionClasses().get(name);
	}

	private String findExtensionName(Class<?> clazz) {
		String name = clazz.getSimpleName();
		if (name.endsWith(type.getSimpleName())) {
			name = name.substring(0, name.length() - type.getSimpleName().length());
		}
		return name.toLowerCase();
	}

	private boolean isAdaptiveClass(Class<?> clazz) {
		return clazz.isAnnotationPresent(Adaptive.class);
	}

	private boolean isWrapperClass(Class<?> clazz) {
		try {
			clazz.getConstructor(type);
			return true;
		}
		catch (NoSuchMethodException ex) {
			return false;
		}
	}

	private boolean isExcluded(String className, String... excludedPackages) {
		if (excludedPackages != null) {
			for (String excludedPackage : excludedPackages) {
				if (className.startsWith(excludedPackage + ".")) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Gets the extension name for the specified extension class. This method returns the
	 * name that was used to register the extension class. It ensures that extension
	 * classes are loaded before attempting to get the name.
	 * @param extensionClass the extension class to get the name for
	 * @return the name of the extension class, or null if the class is not registered
	 */
	public String getExtensionName(Class<?> extensionClass) {
		this.getExtensionClasses();
		return cachedNames.get(extensionClass);
	}

	/**
	 * Gets the extension name for the specified extension instance. This is a convenience
	 * method that calls {@link #getExtensionName(Class)} with the class of the provided
	 * instance.
	 * @param extensionInstance the extension instance to get the name for
	 * @return the name of the extension instance's class, or null if the class is not
	 * registered
	 */
	public String getExtensionName(T extensionInstance) {
		return getExtensionName(extensionInstance.getClass());
	}

	/**
	 * Gets an already loaded extension with the specified name. Unlike
	 * {@link #getExtension(String)}, this method only returns extensions that have
	 * already been loaded and does not attempt to create new instances.
	 * @param name the name of the extension to get
	 * @return the loaded extension with the specified name, or null if not loaded
	 * @throws IllegalArgumentException if the name is null or empty
	 */
	@SuppressWarnings("unchecked")
	public T getLoadedExtension(String name) {
		if (StringUtils.isEmpty(name)) {
			throw new IllegalArgumentException("Extension name == null");
		}
		Holder<Object> holder = getOrCreateHolder(name);
		return (T) holder.get();
	}

	/**
	 * Gets the names of all extensions that have been loaded. This method returns an
	 * unmodifiable set of the names of all extensions that have been loaded for the
	 * current extension type.
	 * @return an unmodifiable set of loaded extension names
	 */
	public Set<String> getLoadedExtensions() {
		return Collections.unmodifiableSet(new TreeSet<>(cachedInstances.keySet()));
	}

	/**
	 * Gets all extension instances that have been loaded. This method returns a list of
	 * all extension instances that have been loaded for the current extension type.
	 * @return a list of all loaded extension instances
	 */
	@SuppressWarnings("unchecked")
	public List<T> getLoadedExtensionInstances() {
		List<T> instances = new ArrayList<>();
		cachedInstances.values().forEach(holder -> instances.add((T) holder.get()));
		return instances;
	}

	/**
	 * Gets the default extension for this extension type. The default extension is
	 * specified by the {@link SPI} annotation on the extension interface. If no default
	 * is specified, or if the specified default extension cannot be found, this method
	 * returns null.
	 * @return the default extension, or null if no default is specified or found
	 */
	public T getDefaultExtension() {
		this.getExtensionClasses();
		if (StringUtils.isBlank(cachedDefaultName)) {
			return null;
		}
		return getExtension(cachedDefaultName);
	}

}
