package space.x9x.radp.extension.context;

/**
 * 组件生命周期
 *
 * @author IO x9x
 * @since 2024-09-24 23:16
 */
public interface Lifecycle {

    /**
     * Initializes the component.
     * This method is called during the initialization phase of the component's lifecycle.
     * It should perform any necessary setup operations before the component is started.
     *
     * @throws IllegalStateException if the component is in an invalid state for initialization
     */
    void initialize() throws IllegalStateException;

    /**
     * Starts the component.
     * This method is called after initialization to start the component's operation.
     * It should activate the component and make it ready for use.
     *
     * @throws IllegalStateException if the component is in an invalid state for starting
     */
    void start() throws IllegalStateException;

    /**
     * Destroys the component.
     * This method is called when the component is no longer needed.
     * It should release any resources held by the component and perform cleanup operations.
     *
     * @throws IllegalStateException if the component is in an invalid state for destruction
     */
    void destroy() throws IllegalStateException;
}
