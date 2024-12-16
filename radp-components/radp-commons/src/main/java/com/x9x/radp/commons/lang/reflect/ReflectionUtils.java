package com.x9x.radp.commons.lang.reflect;

import cn.hutool.core.util.ReflectUtil;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Date;

/**
 * 反射工具类
 *
 * @author x9x
 * @since 2024-09-24 16:25
 */
@UtilityClass
public class ReflectionUtils extends ReflectUtil {

    public static boolean isPublic(@NonNull Method method) {
        return Modifier.isPublic(method.getModifiers());
    }

    public static boolean isSetter(Method method) {
        return method.getName().startsWith("set")
                && method.getParameterTypes().length == 1
                && isPublic(method);
    }

    /**
     * 检查类类型是否为基本类型或常用的包装类。
     *
     * @param clazz 要检查的类。
     * @return 如果是基本类型或常用的包装类，则返回 true；否则返回 false。
     * <p>
     * 说明：这里的基本类型包括 int、char、boolean、String、Number 子类以及 Date。此外，该方法还考虑了 Boolean、Character 和 Number 子类作为基本类型。
     */
    public static boolean isPrimitive(Class<?> clazz) {
        return clazz.isPrimitive() || clazz == String.class || clazz == Boolean.class || clazz == Character.class
                || Number.class.isAssignableFrom(clazz) || Date.class.isAssignableFrom(clazz);
    }

    /**
     * 检查类类型是否为基本类型或常用包装类的数组。
     *
     * @param clazz 要检查的类。
     * @return 如果是基本类型或常用包装类的数组，则返回 true；否则返回 false。
     * <p>
     * 说明：此方法首先检查传递的类类型是否为数组类型。如果是，则获取其元素类型，并检查元素类型是否为基本类型或常用包装类。
     */
    public static boolean isPrimitives(Class<?> clazz) {
        // 当 clazz 代表的是数组类型时，解开数组层数，获取其组件类型（即数组的元素类型）
        while (clazz.isArray()) {
            clazz = clazz.getComponentType();
        }
        // 判断解开数组层数后的 clazz 是否为原始类型
        return isPrimitive(clazz);
    }

    /**
     * 获取setter方法对应属性的名称
     *
     * @param method Method对象，代表一个setter方法
     * @return 返回该setter方法所操作的属性名称如果方法名不符合setter模式，则返回空字符串
     */
    public static String getSetterProperty(Method method) {
        // 当方法名长度大于3时，表明可能符合setter方法的命名规则，提取属性名部分并转换为小写
        // 否则，返回空字符串，表示无法从方法名中识别出属性名称
        return method.getName().length() > 3 ? method.getName().substring(3, 4).toLowerCase() + method.getName().substring(4) : "";
    }

}
