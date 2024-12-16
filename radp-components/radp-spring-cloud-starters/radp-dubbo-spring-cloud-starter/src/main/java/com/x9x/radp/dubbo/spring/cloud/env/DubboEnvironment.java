package com.x9x.radp.dubbo.spring.cloud.env;

import lombok.experimental.UtilityClass;

/**
 * @author x9x
 * @since 2024-10-01 23:41
 */
@UtilityClass
public class DubboEnvironment {
    public static final String ENABLED = "dubbo.enabled";
    public static final String PROTOCOL = "dubbo.protocol.name";
    public static final String PORT = "dubbo.protocol.port";
    public static final String REGISTRY_ADDRESS = "dubbo.registry.address";
}
