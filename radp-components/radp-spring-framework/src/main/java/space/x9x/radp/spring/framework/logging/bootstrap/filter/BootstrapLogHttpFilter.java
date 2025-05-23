package space.x9x.radp.spring.framework.logging.bootstrap.filter;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.slf4j.MDC;
import org.springframework.core.env.Environment;
import space.x9x.radp.commons.lang.StringUtils;
import space.x9x.radp.commons.lang.Strings;
import space.x9x.radp.commons.net.IpConfigUtils;
import space.x9x.radp.spring.framework.bootstrap.constant.SpringProperties;
import space.x9x.radp.spring.framework.logging.MdcConstants;
import space.x9x.radp.spring.framework.web.util.ServletUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author x9x
 * @since 2024-09-30 11:10
 */
@RequiredArgsConstructor
public class BootstrapLogHttpFilter extends HttpFilter {

    private static final long serialVersionUID = 1L;

    @Setter
    private boolean enabledMdc = false;

    private final Environment env;

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {

        if (enabledMdc) {
            String appName = StringUtils.trimToEmpty(env.getProperty(SpringProperties.SPRING_APPLICATION_NAME));
            String profile = StringUtils.trimToEmpty(String.join(Strings.COMMA, getActiveProfiles(env)));
            String requestURI = ServletUtils.getRequestURI(req);
            String remoteUser = ServletUtils.getRemoteUser(req);
            String remoteAddr = IpConfigUtils.parseIpAddress(req);
            String localAddr = ServletUtils.getLocalAddr(req);

            MDC.put(MdcConstants.APP, appName);
            MDC.put(MdcConstants.PROFILE, profile);
            MDC.put(MdcConstants.REQUEST_URI, requestURI);
            MDC.put(MdcConstants.REMOTE_USER, remoteUser);
            MDC.put(MdcConstants.REMOTE_ADDR, remoteAddr);
            MDC.put(MdcConstants.LOCAL_ADDR, localAddr);
        }

        chain.doFilter(req, res);
    }

    private static String[] getActiveProfiles(Environment env) {
        String[] profiles = env.getActiveProfiles();
        if (profiles.length == 0) {
            return env.getDefaultProfiles();
        }
        return profiles;
    }
}
