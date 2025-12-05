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

package space.x9x.radp.extension.strategy;

/**
 * 内部扩展点加载策略.
 * <p>
 * Internal extension loading strategy. This class implements the LoadingStrategy
 * interface to provide a strategy for loading internal extensions from a specific
 * directory. Internal extensions are those that are bundled with the framework itself.
 *
 * @author RADP x9x
 * @since 2024-09-24 19:54
 */
public class InternalLoadingStrategy implements LoadingStrategy {

	/**
	 * Directory path for internal extensions. This constant defines the location where
	 * internal extension configuration files are stored. Internal extensions are loaded
	 * from this directory during the extension loading process.
	 */
	public static final String META_INF = "META-INF/internal/";

	@Override
	public String directory() {
		return META_INF;
	}

	@Override
	public int getPriority() {
		return MAX_PRIORITY;
	}

}
