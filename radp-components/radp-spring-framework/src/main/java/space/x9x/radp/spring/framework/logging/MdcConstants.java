package space.x9x.radp.spring.framework.logging;

import lombok.experimental.UtilityClass;

/**
 * @author x9x
 * @since 2024-09-28 21:08
 */
@UtilityClass
public class MdcConstants {
    /**
     * MDC key for the application name.
     */
    public static final String APP = "app";

    /**
     * MDC key for the active Spring profile.
     */
    public static final String PROFILE = "profile";

    /**
     * MDC key for the class name of the current execution context.
     */
    public static final String CLASS_NAME = "className";

    /**
     * MDC key for the method name of the current execution context.
     */
    public static final String METHOD_NAME = "methodName";

    /**
     * MDC key for the method arguments of the current execution.
     */
    public static final String ARGUMENTS = "arguments";

    /**
     * MDC key for the return value of the method execution.
     */
    public static final String RETURN_VALUE = "returnValue";

    /**
     * MDC key for the execution duration in milliseconds.
     */
    public static final String DURATION = "duration";

    /**
     * MDC key for the HTTP request URI.
     */
    public static final String REQUEST_URI = "requestUri";

    /**
     * MDC key for the remote user information.
     */
    public static final String REMOTE_USER = "remoteUser";

    /**
     * MDC key for the remote client IP address.
     */
    public static final String REMOTE_ADDR = "remoteAddr";

    /**
     * MDC key for the local server IP address.
     */
    public static final String LOCAL_ADDR = "localAddr";
}
