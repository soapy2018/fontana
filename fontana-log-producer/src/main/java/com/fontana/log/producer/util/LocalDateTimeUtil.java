//package com.fontana.log.producer.util;
//
//import java.time.LocalDateTime;
//import java.time.ZoneOffset;
//import java.time.format.DateTimeFormatter;
//import java.util.Date;
//
///**
// * @author yegenchang
// * @description 日期时间处理类
// * @date 2022/6/16 10:08
// */
//public class LocalDateTimeUtil {
//
//  /**
//   * 格式化器
//   */
//  private static final DateTimeFormatter formatter =DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//
//  /**
//   * 毫秒转localDateTime
//   *
//   * @param milliseconds 毫秒
//   * @return
//   */
//  public static LocalDateTime fromMillSecond(long milliseconds) {
//    LocalDateTime localDateTime = LocalDateTime.ofEpochSecond(milliseconds / 1000, 0,
//        ZoneOffset.ofHours(8));
//    return localDateTime;
//  }
//
//  /**
//   * 将秒转换成localDate
//   *
//   * @param second 秒
//   * @return
//   */
//  public static LocalDateTime fromSecond(int second) {
//    LocalDateTime localDateTime = LocalDateTime.ofEpochSecond(second, 0,
//        ZoneOffset.ofHours(8));
//    return localDateTime;
//  }
//
//
//  /**
//   * 日期格式化
//   * @param date
//   * @return
//   */
//  public static String format(Date date) {
//    return fromMillSecond(date.getTime()).format(formatter);
//  }
//
//
//  /**
//   * 秒时间戳格式化
//   * @param second
//   * @return
//   */
//  public static String formatSecond(int second){
//    return fromSecond(second).format(formatter);
//  }
//
//  /**
//   * 毫秒时间戳格式化
//   * @param millSecond
//   * @return
//   */
//  public static String formatMillSecond(long millSecond){
//    return fromMillSecond(millSecond).format(formatter);
//  }
//
//}
