package space.x9x.radp.extension;

/**
 * 使用SPI（服务提供者接口）注解标记的服务接口。 此接口定义了获取扩展类实例的方法，用于动态加载和创建指定类型的扩展实例。
 *
 * @author IO x9x
 * @since 2024-09-24 11:19
 */
@SPI
public interface ExtensionFactory {

    /**
     * 根据给定的类类型和名称获取扩展实例
     *
     * @param type 扩展类的类类型
     * @param name 扩展列的标识
     * @param <T>  标识返回的扩展类实例的类型
     * @return 类型为 {@code T} 的扩展类实例
     */
    <T> T getExtension(Class<T> type, String name);
}
