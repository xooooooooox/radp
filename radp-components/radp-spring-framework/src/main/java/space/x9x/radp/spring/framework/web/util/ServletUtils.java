package space.x9x.radp.spring.framework.web.util;

import space.x9x.radp.commons.lang.StringUtils;
import space.x9x.radp.commons.lang.Strings;
import lombok.experimental.UtilityClass;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.stream.Collectors;

/**
 * @author x9x
 * @since 2024-09-27 10:54
 */
@UtilityClass
public class ServletUtils {
    public static final String ACCEPT_RANGES = "bytes";
    public static final String CONTENT_DISPOSITION_ATTACH = "attachment;filename={0}";
    /**
     * Spring 已标记弃用，但用户不升级 Chrome 是无法解决问题的
     */
    public static final String APPLICATION_JSON_UTF8_VALUE = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8";

    public static ServletRequestAttributes getRequestAttributes() {
        return (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    }

    public static HttpServletRequest getRequest() {
        ServletRequestAttributes attributes = getRequestAttributes();
        if (attributes != null) {
            return attributes.getRequest();
        }
        return null;
    }

    public static HttpServletResponse getResponse() {
        ServletRequestAttributes attributes = getRequestAttributes();
        if (attributes != null) {
            return attributes.getResponse();
        }
        return null;
    }

    public static String getRemoteUser() {
        HttpServletRequest request = getRequest();
        return getRemoteUser(request);
    }

    public static String getRemoteUser(HttpServletRequest request) {
        return StringUtils.trimToEmpty(request.getRemoteUser());
    }

    public static String getRequestBOdy(HttpServletRequest request) {
        try (BufferedReader reader = request.getReader()) {
            if (reader != null) {
                return reader.lines().collect(Collectors.joining(System.lineSeparator()));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public static String getResponseBody(HttpServletResponse response) {
        if (response instanceof CustomHttpServletResponseWrapper) {
            return ((CustomHttpServletResponseWrapper) response).getContent();
        }
        String contentType = response.getContentType();
        if (contentType != null && contentType.startsWith(MediaType.APPLICATION_JSON_VALUE)) {
            return response.toString();
        }
        return Strings.EMPTY;
    }

    public static String getRequestURI() {
        return getRequestURI(getRequest());
    }

    public static String getRequestURI(HttpServletRequest request) {
        return StringUtils.trimToEmpty(request.getRequestURI());
    }

    public static String getLocalAddr() {
        return getLocalAddr(getRequest());
    }

    public static String getLocalAddr(HttpServletRequest request) {
        return StringUtils.trimToEmpty(request.getLocalAddr());
    }
}