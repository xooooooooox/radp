package space.x9x.radp.extension.context;

/**
 * 组件生命周期
 *
 * @author x9x
 * @since 2024-09-24 23:16
 */
public interface Lifecycle {

    void initialize() throws IllegalStateException;

    void start() throws IllegalStateException;

    void destroy() throws IllegalStateException;
}
