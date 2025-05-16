package space.x9x.radp.dubbo.spring.cloud.env;

import lombok.experimental.UtilityClass;

/**
 * @author x9x
 * @since 2024-10-01 23:41
 */
@UtilityClass
public class DubboEnvironment {
    /**
     * Property name for enabling/disabling Dubbo.
     * This constant defines the property name used to control whether Dubbo is enabled.
     */
    public static final String ENABLED = "dubbo.enabled";

    /**
     * Property name for the Dubbo protocol name configuration.
     * This constant defines the property name used to configure the protocol
     * that Dubbo will use for communication (e.g., dubbo, rest, http).
     */
    public static final String PROTOCOL = "dubbo.protocol.name";

    /**
     * Property name for the Dubbo protocol port configuration.
     * This constant defines the property name used to configure the port
     * on which the Dubbo service will listen for incoming requests.
     */
    public static final String PORT = "dubbo.protocol.port";

    /**
     * Property name for the Dubbo registry address configuration.
     * This constant defines the property name used to configure the address
     * of the registry service (e.g., ZooKeeper) that Dubbo will use for service discovery.
     */
    public static final String REGISTRY_ADDRESS = "dubbo.registry.address";
}
