package space.x9x.radp.dubbo.spring.cloud.env;

import lombok.experimental.UtilityClass;

/**
 * @author x9x
 * @since 2024-10-01 23:41
 */
@UtilityClass
public class DubboEnvironment {
    /**
     * Property key for enabling or disabling Dubbo.
     * This property controls whether Dubbo services are activated in the application.
     */
    public static final String ENABLED = "dubbo.enabled";

    /**
     * Property key for the Dubbo protocol name.
     * This property specifies which protocol (e.g., dubbo, rest, http) to use for RPC communication.
     */
    public static final String PROTOCOL = "dubbo.protocol.name";

    /**
     * Property key for the Dubbo protocol port.
     * This property specifies which port the Dubbo server will listen on.
     */
    public static final String PORT = "dubbo.protocol.port";

    /**
     * Property key for the Dubbo registry address.
     * This property specifies the address of the service registry (e.g., ZooKeeper, Nacos)
     * that Dubbo will use for service discovery.
     */
    public static final String REGISTRY_ADDRESS = "dubbo.registry.address";
}
