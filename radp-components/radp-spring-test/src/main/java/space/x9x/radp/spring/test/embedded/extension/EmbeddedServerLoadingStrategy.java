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

package space.x9x.radp.spring.test.embedded.extension;

import space.x9x.radp.extension.strategy.LoadingStrategy;

/**
 * Loading strategy for embedded server extensions. This class defines how embedded server
 * implementations are loaded from the classpath. It specifies the directory where
 * extension definitions are located and the priority of this loading strategy.
 *
 * @author RADP x9x
 * @since 2024-10-13 17:44
 */
public class EmbeddedServerLoadingStrategy implements LoadingStrategy {

	/**
	 * The directory path where embedded server extension definitions are located. This
	 * path is relative to the classpath root.
	 */
	public static final String META_INF = "META-INF/embedded-server/";

	/**
	 * Returns the directory path where embedded server extension definitions are located.
	 * @return the directory path for extension definitions
	 */
	@Override
	public String directory() {
		return META_INF;
	}

	/**
	 * Returns the priority of this loading strategy. Higher priority strategies are
	 * processed first.
	 * @return the maximum priority value
	 */
	@Override
	public int getPriority() {
		return MAX_PRIORITY;
	}

}
