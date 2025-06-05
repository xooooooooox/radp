/*
 * Copyright 2012-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package space.x9x.radp.commons.net;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;

import space.x9x.radp.commons.collections.CollectionUtils;
import space.x9x.radp.commons.lang.StringUtils;
import space.x9x.radp.commons.lang.Strings;

/**
 * Utility class for IP address operations and network-related functionality.
 *
 * @author IO x9x
 * @since 2024-09-28 21:12
 */
@UtilityClass
public class IpConfigUtils {

	/**
	 * HTTP header name for forwarded client IP addresses. This header is commonly used by
	 * proxies to pass the original client IP.
	 */
	private static final String X_FORWARDED_FOR = "X-Forwarded-For";

	/**
	 * HTTP header name for proxy client IP address. This header is used by some proxy
	 * servers to indicate the client's IP address.
	 */
	private static final String PROXY_CLIENT_IP = "Proxy-Client-IP";

	/**
	 * HTTP header name for WebLogic proxy client IP address. This header is specific to
	 * WebLogic proxy servers.
	 */
	private static final String WL_PROXY_CLIENT_IP = "WL-Proxy-Client-IP";

	/**
	 * The cached IP address of the local machine. This value is determined when the class
	 * is loaded.
	 */
	private static final String IP_ADDRESS = getIpAddress(null);

	/**
	 * Default subnet mask for IP subnet calculations. This mask (255.255.255.0)
	 * represents a standard Class C network.
	 */
	private static final String SUBNET_MASK = "255.255.255.0";

	/**
	 * Returns the IP address of the local machine.
	 *
	 * <p>
	 * This method returns a cached IP address determined when the class was loaded.
	 * @return the IP address of the local machine
	 */
	public static String getIpAddress() {
		return IP_ADDRESS;
	}

	/**
	 * Gets the IP address for the specified network interface.
	 *
	 * <p>
	 * This method retrieves the IP address associated with the specified network
	 * interface. If no interface name is provided, it returns the first available
	 * non-loopback IPv4 address.
	 * @param interfaceName the name of the network interface, or null for any interface
	 * @return the IP address, or an empty string if no address is found or an error
	 * occurs
	 */
	public static String getIpAddress(String interfaceName) {
		try {
			List<String> ipList = getHostAddress(interfaceName);
			return CollectionUtils.isNotEmpty(ipList) ? ipList.get(0) : Strings.EMPTY;
		}
		catch (SocketException ex) {
			return Strings.EMPTY;
		}
	}

	/**
	 * Gets the IP addresses of active network interfaces.
	 *
	 * <p>
	 * This method retrieves all IP addresses from active network interfaces. It filters
	 * out loopback addresses and IPv6 addresses.
	 * @param interfaceName the name of the network interface to filter by, or null for
	 * all interfaces
	 * @return a list of IP addresses from active network interfaces
	 * @throws SocketException if a network error occurs
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
				}
				else if (networkInterface.getDisplayName().equals(interfaceName)) {
					ipList.add(hostAddress);
				}
			}
		}
		return ipList;
	}

	/**
	 * Extracts the client IP address from an HTTP request.
	 *
	 * <p>
	 * This method attempts to find the client IP address by checking various HTTP headers
	 * that might contain the original client IP when the request passes through proxies.
	 * It checks the following headers in order:
	 * <ol>
	 * <li>X-Forwarded-For</li>
	 * <li>Proxy-Client-IP</li>
	 * <li>WL-Proxy-Client-IP</li>
	 * </ol>
	 * If none of these headers contain a valid IP, it falls back to the remote address
	 * from the request.
	 * @param request the HTTP servlet request
	 * @return the client IP address
	 */
	public static String parseIpAddress(HttpServletRequest request) {
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
	 * <p>
	 * This method uses the default subnet mask (255.255.255.0) to determine if the two
	 * provided IP addresses belong to the same subnet.
	 * @param ip1 the first IP address
	 * @param ip2 the second IP address
	 * @return true if both IP addresses are in the same subnet, false otherwise
	 * @throws UnknownHostException if either IP address cannot be resolved
	 */
	public static boolean isSameSubnet(String ip1, String ip2) throws UnknownHostException {
		return isSameSubnet(ip1, ip2, SUBNET_MASK);
	}

	/**
	 * Checks if two IP addresses are in the same subnet using a specified subnet mask.
	 *
	 * <p>
	 * This method determines if the two provided IP addresses belong to the same subnet
	 * by applying the specified subnet mask to both addresses and comparing the results.
	 * @param ip1 the first IP address
	 * @param ip2 the second IP address
	 * @param subnetMask the subnet mask to use for the comparison
	 * @return true if both IP addresses are in the same subnet, false otherwise
	 * @throws UnknownHostException if an IP address or subnet mask cannot be resolved
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
