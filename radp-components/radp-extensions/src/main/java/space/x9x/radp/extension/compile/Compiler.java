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

package space.x9x.radp.extension.compile;

import space.x9x.radp.extension.SPI;

/**
 * 编译器接口，用于动态编译Java源代码.
 * <p>
 * Compiler interface for dynamically compiling Java source code. This interface defines
 * methods for compiling Java source code into Class objects at runtime. It is used by the
 * extension system to generate and compile adaptive classes dynamically.
 *
 * @author IO x9x
 * @since 2024-09-24 21:33
 */
@SPI("javassist")
public interface Compiler {

	/**
	 * Compiles Java source code into a Class object. This method takes a string
	 * containing Java source code and compiles it into a Class object using the specified
	 * ClassLoader.
	 * @param code the Java source code to compile
	 * @param classLoader the ClassLoader to use for loading the compiled class
	 * @return the compiled Class object
	 */
	Class<?> compile(String code, ClassLoader classLoader);

}
