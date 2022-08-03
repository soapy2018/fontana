//package com.fontana.log.producer.util;
//
//import java.net.InetAddress;
//import java.net.NetworkInterface;
//import java.net.SocketException;
//import java.util.Enumeration;
//
///**
// * @author yegenchang
// * @description 网络工具，获取IP
// * @date 2022/6/16 16:10
// */
//public final class NetworkUtils {
//
//  private NetworkUtils() {
//  }
//
//  /**
//   * Checks if the specified string is a valid IP address.
//   *
//   * @param ipAddress the string to check
//   * @return true if the string validates as an IP address
//   */
//  public static boolean isIPAddr(final String ipAddress) {
//    if (ipAddress == null || ipAddress.isEmpty()) {
//      return false;
//    }
//    try {
//      final String[] tokens = ipAddress.split("\\.");
//      if (tokens.length != 4) {
//        return false;
//      }
//      for (String token : tokens) {
//        int i = Integer.parseInt(token);
//        if (i < 0 || i > 255) {
//          return false;
//        }
//      }
//      return true;
//    } catch (Exception ex) {
//      return false;
//    }
//  }
//
//  /**
//   * Get the IP address of current machine.
//   *
//   * @return An IP address or {@code null} if unable to get the IP address
//   */
//  public static String getLocalMachineIP() {
//    try {
//      Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
//      while (networkInterfaces.hasMoreElements()) {
//        NetworkInterface ni = networkInterfaces.nextElement();
//        if (!ni.isUp()) {
//          continue;
//        }
//        Enumeration<InetAddress> addresses = ni.getInetAddresses();
//        while (addresses.hasMoreElements()) {
//          final InetAddress address = addresses.nextElement();
//          if (!address.isLinkLocalAddress() && address.getHostAddress() != null) {
//            String ipAddress = address.getHostAddress();
//            if (ipAddress.equals("127.0.0.1")) {
//              continue;
//            }
//            if (isIPAddr(ipAddress)) {
//              return ipAddress;
//            }
//          }
//        }
//      }
//    } catch (SocketException ex) {
//      // swallow it
//    }
//    return null;
//  }
//}
