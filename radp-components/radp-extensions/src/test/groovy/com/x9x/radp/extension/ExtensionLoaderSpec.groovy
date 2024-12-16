package com.x9x.radp.extension

import spock.lang.Specification

/**
 * @author x9x
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
