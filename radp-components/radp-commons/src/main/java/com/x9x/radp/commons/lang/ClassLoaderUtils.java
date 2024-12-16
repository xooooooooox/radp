package com.x9x.radp.commons.lang;

import lombok.experimental.UtilityClass;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author x9x
 * @since 2024-09-24 20:23
 */
@UtilityClass
public class ClassLoaderUtils {

    private static final Map<String, Class<?>> PRIMITIVE_CLASSES;

    static {
        final Map<String, Class<?>> primitives = new HashMap<>(10, 1.0f);
        primitives.put("boolean", boolean.class);
        primitives.put("byte", byte.class);
        primitives.put("char", char.class);
        primitives.put("short", short.class);
        primitives.put("int", int.class);
        primitives.put("long", long.class);
        primitives.put("float", float.class);
        primitives.put("double", double.class);
        primitives.put("void", void.class);
        PRIMITIVE_CLASSES = Collections.unmodifiableMap(primitives);
    }

    public static ClassLoader getClassLoader(Class<?> clazz) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader == null) {
            classLoader = clazz.getClassLoader();
            if (classLoader == null) {
                classLoader = ClassLoader.getSystemClassLoader();
            }
        }
        return classLoader;
    }
}
