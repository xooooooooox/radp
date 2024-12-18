package space.x9x.radp.spring.framework.logging;

import lombok.experimental.UtilityClass;

/**
 * @author x9x
 * @since 2024-09-28 21:08
 */
@UtilityClass
public class MdcConstants {
    public static final String APP = "app";
    public static final String PROFILE = "profile";
    public static final String CLASS_NAME = "className";
    public static final String METHOD_NAME = "methodName";
    public static final String ARGUMENTS = "arguments";
    public static final String RETURN_VALUE = "returnValue";
    public static final String DURATION = "duration";
    public static final String REQUEST_URI = "requestUri";
    public static final String REMOTE_USER = "remoteUser";
    public static final String REMOTE_ADDR = "remoteAddr";
    public static final String LOCAL_ADDR = "localAddr";
}
