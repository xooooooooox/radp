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
     * Used to identify which application generated the log entry.
     */
    public static final String APP = "app";
    /**
     * MDC key for the application profile.
     * Used to identify which environment profile (e.g., dev, test, prod) is active.
     */
    public static final String PROFILE = "profile";
    /**
     * MDC key for the class name.
     * Used to record the fully qualified name of the class that generated the log entry.
     */
    public static final String CLASS_NAME = "className";
    /**
     * MDC key for the method name.
     * Used to record the name of the method that generated the log entry.
     */
    public static final String METHOD_NAME = "methodName";
    /**
     * MDC key for method arguments.
     * Used to record the arguments passed to the method that generated the log entry.
     */
    public static final String ARGUMENTS = "arguments";
    /**
     * MDC key for method return value.
     * Used to record the value returned by the method that generated the log entry.
     */
    public static final String RETURN_VALUE = "returnValue";
    /**
     * MDC key for method execution duration.
     * Used to record the time taken to execute the method that generated the log entry.
     */
    public static final String DURATION = "duration";
    /**
     * MDC key for HTTP request URI.
     * Used to record the URI of the HTTP request that generated the log entry.
     */
    public static final String REQUEST_URI = "requestUri";
    /**
     * MDC key for remote user.
     * Used to record the username of the user who made the request that generated the log entry.
     */
    public static final String REMOTE_USER = "remoteUser";
    /**
     * MDC key for remote address.
     * Used to record the IP address of the client that made the request that generated the log entry.
     */
    public static final String REMOTE_ADDR = "remoteAddr";
    /**
     * MDC key for local address.
     * Used to record the local IP address that received the request that generated the log entry.
     */
    public static final String LOCAL_ADDR = "localAddr";
}
