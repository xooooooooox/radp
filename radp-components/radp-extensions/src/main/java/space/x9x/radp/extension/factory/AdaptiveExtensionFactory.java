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

package space.x9x.radp.extension.factory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import space.x9x.radp.extension.Adaptive;
import space.x9x.radp.extension.ExtensionFactory;
import space.x9x.radp.extension.ExtensionLoader;

/**
 * 自适应扩展点实例工厂.
 * <p>
 * Adaptive extension instance factory. This factory implements the ExtensionFactory
 * interface and delegates to other extension factories to find and create extension
 * instances. It uses the SPI mechanism to discover all available extension factories and
 * tries each one in sequence until an extension is found.
 *
 * @author RADP x9x
 * @since 2024-09-25 10:30
 */
@Adaptive
public class AdaptiveExtensionFactory implements ExtensionFactory {

	/**
	 * List of extension factories that this adaptive factory delegates to. This list is
	 * initialized during construction and contains all available ExtensionFactory
	 * implementations discovered through the SPI mechanism. The list is unmodifiable to
	 * ensure thread safety.
	 */
	private final List<ExtensionFactory> factories;

	/**
	 * Constructs a new AdaptiveExtensionFactory. This constructor initializes the factory
	 * by loading all available ExtensionFactory implementations using the ExtensionLoader
	 * mechanism. It creates an unmodifiable list of these factories, which will be used
	 * to delegate extension lookup requests. The factories are queried in the order they
	 * were loaded, with the first factory that returns a non-null extension being used.
	 */
	public AdaptiveExtensionFactory() {
		ExtensionLoader<ExtensionFactory> loader = ExtensionLoader.getExtensionLoader(ExtensionFactory.class);
		List<ExtensionFactory> list = new ArrayList<>();
		for (String name : loader.getSupportedExtensions()) {
			list.add(loader.getExtension(name));
		}
		this.factories = Collections.unmodifiableList(list);
	}

	/**
	 * Gets an extension instance of the specified type and name. This method delegates to
	 * each of the available extension factories in sequence, returning the first non-null
	 * extension found. If no factory can provide the requested extension, this method
	 * returns null.
	 * @param type the class type of the extension to get
	 * @param name the name of the extension to get
	 * @param <T> the type parameter of the extension
	 * @return the extension instance, or null if no matching extension is found
	 */
	@Override
	public <T> T getExtension(Class<T> type, String name) {
		for (ExtensionFactory factory : this.factories) {
			T extension = factory.getExtension(type, name);
			if (extension != null) {
				return extension;
			}
		}
		return null;
	}

}
