//package com.fontana.log.producer.appender;
//
//import java.net.Inet4Address;
//import java.net.InetAddress;
//import java.net.NetworkInterface;
//import java.util.Enumeration;
//
///**
// * @author yegenchang
// * @description
// * @date 2022/6/14 16:28
// */
//public class LogComponentUtil {
//
//  private static String hostName;
//  private static String hostIp;a
//
//  /**
//   * 获取主机名称
//   * @return
//   */
//  public static String getHostname(){
//    if(hostName!=null){
//      return hostName;
//    }
//    try{
//      hostName= InetAddress.getLocalHost().getHostName();
//    }catch (Exception e){
//      e.printStackTrace();
//    }
//    return hostName;
//  }
//
//  /**
//   * 获取本机ip
//   * @return
//   */
//  public static String getHostIP() {
//    if (hostIp != null) {
//      return hostIp;
//    }
//    try {
//      Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
//      InetAddress ip = null;
//      while (allNetInterfaces.hasMoreElements()) {
//        NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
//        if (netInterface.isLoopback() || netInterface.isVirtual() || !netInterface.isUp()) {
//          continue;
//        } else {
//          if (netInterface.getDisplayName().equals("docker0")) {
//            continue;
//          }
//          Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
//          while (addresses.hasMoreElements()) {
//            ip = addresses.nextElement();
//            if (ip != null && ip instanceof Inet4Address) {
//              hostIp = ip.getHostAddress();
//              return hostIp;
//            }
//          }
//        }
//      }
//    } catch (Exception e) {
//      System.err.println("IP地址获取失败" + e.toString());
//    }
//    hostIp = "127.0.0.1";
//    return hostIp;
//  }
//}
