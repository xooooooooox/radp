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

package space.x9x.radp.extension

import spock.lang.Specification

/**
 * @author IO x9x
 * @since 2024-09-25 18:42
 */
class ExtensionLoaderSpec extends Specification {

    def "test get extension"() {
        when:
        Demo demo1 = ExtensionLoader.getExtensionLoader(Demo.class).getExtension("demo1")
        Demo demo2 = ExtensionLoader.getExtensionLoader(Demo.class).getExtension("demo2")
        Demo demo3 = ExtensionLoader.getExtensionLoader(Demo.class).getDefaultExtension()

        then:
        demo1 instanceof DemoImpl1
        demo2 instanceof DemoImpl2
        demo3 instanceof DemoImpl2
    }
}
