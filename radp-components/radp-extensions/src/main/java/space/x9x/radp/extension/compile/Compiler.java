package space.x9x.radp.extension.compile;

import space.x9x.radp.extension.SPI;

/**
 * @author x9x
 * @since 2024-09-24 21:33
 */
@SPI("javassist")
public interface Compiler {

    /**
     * Compiles the given code using the specified class loader.
     *
     * @param code        The source code to compile
     * @param classLoader The class loader to use for compilation
     * @return The compiled class
     */
    Class<?> compile(String code, ClassLoader classLoader);
}
