package space.x9x.radp.extension.compile;

import space.x9x.radp.extension.SPI;

/**
 * @author x9x
 * @since 2024-09-24 21:33
 */
@SPI("javassist")
public interface Compiler {

    /**
     * Compiles Java source code into a Class object.
     * This method takes a string containing Java source code and compiles it
     * into a Class object using the specified ClassLoader.
     *
     * @param code        the Java source code to compile
     * @param classLoader the ClassLoader to use for loading the compiled class
     * @return the compiled Class object
     */
    Class<?> compile(String code, ClassLoader classLoader);
}
