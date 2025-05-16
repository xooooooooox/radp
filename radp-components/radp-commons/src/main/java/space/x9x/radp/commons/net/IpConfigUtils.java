package space.x9x.radp.commons.net;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import space.x9x.radp.commons.collections.CollectionUtils;
import space.x9x.radp.commons.lang.StringUtils;
import space.x9x.radp.commons.lang.Strings;

import javax.servlet.http.HttpServletRequest;
import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * @author x9x
 * @since 2024-09-28 21:12
 */
@UtilityClass
public class IpConfigUtils {
    private static final String X_FORWARDED_FOR = "X-Forwarded-For";
    private static final String PROXY_CLIENT_IP = "Proxy-Client-IP";
    private static final String WL_PROXY_CLIENT_IP = "WL-Proxy-Client-IP";
    private static final String IP_ADDRESS = getIpAddress(null);
    private static final String SUBNET_MASK = "255.255.255.0";

    /**
     * Returns the IP address of the local machine.
     * <p>
     * This method returns a cached IP address that was determined when the class was loaded.
     *
     * @return the IP address of the local machine
     */
    public static String getIpAddress() {
        return IP_ADDRESS;
    }

    /**
     * 根据网卡名称获取 IP 地址
     *
     * @param interfaceName 网卡名称
     * @return IP 地址
     */
    public static String getIpAddress(String interfaceName) {
        try {
            List<String> ipList = getHostAddress(interfaceName);
            return CollectionUtils.isNotEmpty(ipList) ? ipList.get(0) : Strings.EMPTY;
        } catch (SocketException e) {
            return Strings.EMPTY;
        }
    }

    /**
     * 获取已激活网卡的 IP 地址
     *
     * @param interfaceName 网卡名称
     * @return 已激活网卡的 IP 地址
     * @throws SocketException 套接字异常
     */
    private static List<String> getHostAddress(String interfaceName) throws SocketException {
        List<String> ipList = new ArrayList<>();
        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
        while (networkInterfaces.hasMoreElements()) {
            NetworkInterface networkInterface = networkInterfaces.nextElement();
            Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
            while (inetAddresses.hasMoreElements()) {
                InetAddress inetAddress = inetAddresses.nextElement();
                if (inetAddress.isLoopbackAddress()) {
                    continue;
                }
                if (inetAddress instanceof Inet6Address) {
                    continue;
                }
                String hostAddress = inetAddress.getHostAddress();
                if (interfaceName == null) {
                    ipList.add(hostAddress);
                } else if (networkInterface.getDisplayName().equals(interfaceName)) {
                    ipList.add(hostAddress);
                }
            }
        }
        return ipList;
    }

    /**
     * Extracts the client's IP address from an HTTP request.
     * <p>
     * This method attempts to find the client IP by checking various HTTP headers
     * that might contain proxy information, falling back to the remote address
     * if no valid IP is found in the headers.
     *
     * @param request the HTTP servlet request
     * @return the client's IP address
     */
    public static String parseIpAddress(@NonNull HttpServletRequest request) {
        String ip = request.getHeader(X_FORWARDED_FOR);
        if (StringUtils.isEmpty(ip) || IpConfig.UNKNOWN_IP.equalsIgnoreCase(ip)) {
            ip = request.getHeader(PROXY_CLIENT_IP);
        }
        if (StringUtils.isEmpty(ip) || IpConfig.UNKNOWN_IP.equalsIgnoreCase(ip)) {
            ip = request.getHeader(WL_PROXY_CLIENT_IP);
        }
        if (StringUtils.isEmpty(ip) || IpConfig.UNKNOWN_IP.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * Checks if two IP addresses are in the same subnet using the default subnet mask.
     *
     * @param ip1 the first IP address
     * @param ip2 the second IP address
     * @return true if both IP addresses are in the same subnet, false otherwise
     * @throws UnknownHostException if either IP address cannot be resolved
     */
    public static boolean isSameSubnet(String ip1, String ip2) throws UnknownHostException {
        return isSameSubnet(ip1, ip2, SUBNET_MASK);
    }


    /**
     * 判断两个 IP 是否在同一网段
     *
     * @param ip1        第一个IP
     * @param ip2        第二个IP
     * @param subnetMask 子网掩码
     * @return true 如果两个IP在同一网段, 否则返回 false
     * @throws UnknownHostException If an IP address or subnet mask cannot be resolved.
     */
    public static boolean isSameSubnet(String ip1, String ip2, String subnetMask) throws UnknownHostException {
        InetAddress address1 = InetAddress.getByName(ip1);
        InetAddress address2 = InetAddress.getByName(ip2);
        InetAddress subnet = InetAddress.getByName(subnetMask);

        byte[] b1 = address1.getAddress();
        byte[] b2 = address2.getAddress();
        byte[] b3 = subnet.getAddress();
        for (int i = 0; i < b1.length; i++) {
            if ((b1[i] & b3[i]) != (b2[i] & b3[i])) {
                return false;
            }
        }
        return true;
    }
}
