package com.fontana.util.request;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;

import javax.servlet.http.HttpServletRequest;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Ip工具类。
 *
 * @author cqf
 * @date 2021-06-06
 */
@Slf4j
public class IpUtil {

    private static final String UNKNOWN = "unknown";

    /**
     * 私有构造函数，明确标识该常量类的作用。
     */
    private IpUtil() {
    }

    /**
     * 通过Servlet的HttpRequest对象获取Ip地址。
     *
     * @param request HttpRequest对象。
     * @return 本次请求的Ip地址。
     */
    public static String getRemoteIpAddress(HttpServletRequest request) {
        String ip = null;
        // X-Forwarded-For：Squid 服务代理
        String ipAddresses = request.getHeader("X-Forwarded-For");
        if (StringUtils.isBlank(ipAddresses) || UNKNOWN.equalsIgnoreCase(ipAddresses)) {
            // Proxy-Client-IP：apache 服务代理
            ipAddresses = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ipAddresses) || UNKNOWN.equalsIgnoreCase(ipAddresses)) {
            // WL-Proxy-Client-IP：weblogic 服务代理
            ipAddresses = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ipAddresses) || UNKNOWN.equalsIgnoreCase(ipAddresses)) {
            // HTTP_CLIENT_IP：有些代理服务器
            ipAddresses = request.getHeader("HTTP_CLIENT_IP");
        }
        if (StringUtils.isBlank(ipAddresses) || UNKNOWN.equalsIgnoreCase(ipAddresses)) {
            // X-Real-IP：nginx服务代理
            ipAddresses = request.getHeader("X-Real-IP");
        }
        // 有些网络通过多层代理，那么获取到的ip就会有多个，一般都是通过逗号（,）分割开来，并且第一个ip为客户端的真实IP
        if (StringUtils.isNotBlank(ipAddresses)) {
            ip = ipAddresses.split(",")[0];
        }
        // 还是不能获取到，最后再通过request.getRemoteAddr();获取
        if (StringUtils.isBlank(ipAddresses) || UNKNOWN.equalsIgnoreCase(ipAddresses)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 通过Reactive的ServerHttpRequest对象获取Ip地址。
     *
     * @param request ServerHttpRequest对象。
     * @return 本次请求的Ip地址。
     */
    public static String getRemoteIpAddress(ServerHttpRequest request) {
        String ip = null;
        // X-Forwarded-For：Squid 服务代理
        String ipAddresses = request.getHeaders().getFirst("X-Forwarded-For");
        if (StringUtils.isBlank(ipAddresses) || UNKNOWN.equalsIgnoreCase(ipAddresses)) {
            // Proxy-Client-IP：apache 服务代理
            ipAddresses = request.getHeaders().getFirst("Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ipAddresses) || UNKNOWN.equalsIgnoreCase(ipAddresses)) {
            // WL-Proxy-Client-IP：weblogic 服务代理
            ipAddresses = request.getHeaders().getFirst("WL-Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ipAddresses) || UNKNOWN.equalsIgnoreCase(ipAddresses)) {
            // HTTP_CLIENT_IP：有些代理服务器
            ipAddresses = request.getHeaders().getFirst("HTTP_CLIENT_IP");
        }
        if (StringUtils.isBlank(ipAddresses) || UNKNOWN.equalsIgnoreCase(ipAddresses)) {
            // X-Real-IP：nginx服务代理
            ipAddresses = request.getHeaders().getFirst("X-Real-IP");
        }
        // 有些网络通过多层代理，那么获取到的ip就会有多个，一般都是通过逗号（,）分割开来，并且第一个ip为客户端的真实IP
        if (StringUtils.isNotBlank(ipAddresses)) {
            ip = ipAddresses.split(",")[0];
        }
        // 还是不能获取到，最后再通过request.getRemoteAddr();获取
        if (StringUtils.isBlank(ipAddresses) || UNKNOWN.equalsIgnoreCase(ipAddresses)) {
            ip = request.getRemoteAddress().getAddress().getHostAddress();
        }
        return ip;
    }

    /**
     * 获取本机ip
     *
     * @return
     */
    public static String getFirstLocalIpAddress() {
        String ip;
        try {
            List<String> ipList = getHostAddress();
            // default the first
            ip = (!ipList.isEmpty()) ? ipList.get(0) : "";
        } catch (Exception ex) {
            ip = "";
            log.error("Failed to call ", ex);
        }
        return ip;
    }

    private static List<String> getHostAddress() throws SocketException {
        List<String> ipList = new ArrayList<>(5);
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements()) {
            NetworkInterface ni = interfaces.nextElement();
            if (ni.isLoopback() || ni.isVirtual() || !ni.isUp()) {
                continue;
            }
            if (ni.getDisplayName().equals("docker0")) {
                continue;
            }
            Enumeration<InetAddress> allAddress = ni.getInetAddresses();
            while (allAddress.hasMoreElements()) {
                InetAddress address = allAddress.nextElement();
                // skip the IPv6 addr
                if (address.isLoopbackAddress() || address instanceof Inet6Address) {
                    continue;
                }
                String hostAddress = address.getHostAddress();
                if(isIPAddr(hostAddress)) {
                    ipList.add(hostAddress);
                }
            }
        }
        return ipList;
    }

    /**
     * Checks if the specified string is a valid IP address.
     *
     * @param ipAddress the string to check
     * @return true if the string validates as an IP address
     */
    public static boolean isIPAddr(final String ipAddress) {
        if (ipAddress == null || ipAddress.isEmpty()) {
            return false;
        }
        try {
            final String[] tokens = ipAddress.split("\\.");
            if (tokens.length != 4) {
                return false;
            }
            for (String token : tokens) {
                int i = Integer.parseInt(token);
                if (i < 0 || i > 255) {
                    return false;
                }
            }
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * 获取主机名称
     *
     * @return
     */
    public static String getHostname() {
        String hostName = "";
        if (hostName != null) {
            return hostName;
        }
        try {
            hostName = InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hostName;
    }
}
