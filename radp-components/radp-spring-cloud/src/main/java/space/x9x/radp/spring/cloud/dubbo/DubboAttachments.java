package space.x9x.radp.spring.cloud.dubbo;

import lombok.experimental.UtilityClass;

/**
 * @author x9x
 * @since 2024-10-01 22:06
 */
@UtilityClass
public class DubboAttachments {
    /**
     * Attachment key for the client IP address.
     * This constant is used to store and retrieve the client IP address
     * in Dubbo RPC attachments for tracing and logging purposes.
     */
    public static final String IP = "ip";
}
