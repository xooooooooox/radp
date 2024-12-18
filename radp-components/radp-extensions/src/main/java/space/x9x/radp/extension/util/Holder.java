package space.x9x.radp.extension.util;

/**
 * 通用类型持有者类
 * <p>
 * 该类用于存储任意类型的对象，并提供设置和获取该对象的方法
 * 主要应用场景是在需要传递一个类型安全的值容器时使用，特别是当该容器需要在多线程环境中被访问时
 * 使用了 volatile 关键字以确保在多线程环境中的可见性
 *
 * @param <T> 任意类型，表示该持有者可以持有任何类型的对象
 * @author x9x
 * @since 2024-09-24 11:33
 */
public class Holder<T> {

    /**
     * 存储的值，使用 volatile 关键字修饰以保证多线程间的可见性
     * volatile 变量不会被线程本地缓存，对 volatile 变量的读写都是直接从主内存中进行
     */
    @SuppressWarnings("squid:S3077")
    private volatile T value;

    public T get() {
        return value;
    }

    public void set(T value) {
        this.value = value;
    }
}
