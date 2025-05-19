package space.x9x.radp.extension.context;

/**
 * 组件生命周期
 *
 * @author x9x
 * @since 2024-09-24 23:16
 */
public interface Lifecycle {

    /**
     * Initializes the component.
     * This method is called during the initialization phase of the component's lifecycle.
     *
     * @throws IllegalStateException if the component is in an illegal state for initialization
     */
    void initialize() throws IllegalStateException;

    /**
     * Starts the component.
     * This method is called after initialization to start the component's functionality.
     *
     * @throws IllegalStateException if the component is in an illegal state for starting
     */
    void start() throws IllegalStateException;

    /**
     * Destroys the component.
     * This method is called during shutdown to release resources and perform cleanup.
     *
     * @throws IllegalStateException if the component is in an illegal state for destruction
     */
    void destroy() throws IllegalStateException;
}
