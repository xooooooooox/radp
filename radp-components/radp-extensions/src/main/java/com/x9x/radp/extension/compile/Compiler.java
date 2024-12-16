package com.x9x.radp.extension.compile;

import com.x9x.radp.extension.SPI;

/**
 * @author x9x
 * @since 2024-09-24 21:33
 */
@SPI("javassist")
public interface Compiler {

    Class<?> compile(String code, ClassLoader classLoader);
}
