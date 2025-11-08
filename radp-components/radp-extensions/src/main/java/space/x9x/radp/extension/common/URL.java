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

package space.x9x.radp.extension.common;

import java.io.Serializable;
import java.util.Map;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Represents a URL with parameters for extension loading and configuration. This class is
 * used in the extension system to pass configuration parameters to extension
 * implementations.
 *
 * @author x9x
 * @since 2024-09-25 00:29
 */
@RequiredArgsConstructor
public class URL implements Serializable {

	private static final long serialVersionUID = 6346161581235040323L;

	/**
	 * The parameters map containing key-value pairs for configuration. This map stores
	 * all the parameters associated with this URL, which can be used to configure
	 * extension behavior.
	 */
	@Getter
	private final Map<String, String> parameters;

	/**
	 * Gets the value of a parameter by its key.
	 * @param key the key of the parameter to retrieve
	 * @return the value of the parameter, or null if the parameter does not exist
	 */
	public String getParameter(String key) {
		return this.parameters.get(key);
	}

}
