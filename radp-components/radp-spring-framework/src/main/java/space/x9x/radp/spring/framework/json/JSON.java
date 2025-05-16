package space.x9x.radp.spring.framework.json;

import space.x9x.radp.extension.SPI;

import java.util.List;

/**
 * @author x9x
 * @since 2024-09-26 11:07
 */
@SPI("jackson")
public interface JSON {

    /**
     * 将对象转换为JSON字符串
     *
     * @param <T> 对象的类型
     * @param object 要转换的对象
     * @return 对象对应的JSON字符串
     */
    <T> String toJSONString(T object);

    /**
     * 将JSON字符串解析为指定类的对象
     *
     * @param <T> 目标对象的类型
     * @param text JSON字符串
     * @param clazz 对象的类类型
     * @return 解析出来的对象
     */
    <T> T parseObject(String text, Class<T> clazz);

    /**
     * 将JSON字符串解析为指定类的列表
     *
     * @param <T> 列表中对象的类型
     * @param text JSON字符串
     * @param clazz 列表中对象的类类型
     * @return 解析出来的对象列表
     */
    <T> List<T> parseList(String text, Class<T> clazz);
}
