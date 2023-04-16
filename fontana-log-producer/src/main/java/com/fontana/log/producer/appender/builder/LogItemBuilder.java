package com.fontana.log.producer.appender.builder;

import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.classic.spi.StackTraceElementProxy;
import ch.qos.logback.classic.spi.ThrowableProxyUtil;
import ch.qos.logback.core.CoreConstants;
import com.fontana.log.producer.producer.BaseLogItem;
import com.fontana.util.date.DateTimeUtil;
import com.fontana.util.request.IpUtil;
import com.fontana.util.tools.MDCTraceUtil;

/**
 * @author yegenchang
 * @description 日志构造器
 * @date 2022/6/16 11:18
 */
public class LogItemBuilder {


  /**
   * 构建一个日志对象
   * @param event 日志事件
   * @param appName 服务名称
   * @return
   */
  public static BaseLogItem build(LoggingEvent event,String appName) {
    BaseLogItem item = new BaseLogItem();
    item.pushBack("logTime", DateTimeUtil.formatMillSecond(event.getTimeStamp()));

    item.pushBack("level", event.getLevel().toString());
    item.pushBack("thread", event.getThreadName());

    StackTraceElement[] caller = event.getCallerData();
    if (caller != null && caller.length > 0) {
      item.pushBack("location", caller[0].toString());
    }
    String traceId = MDCTraceUtil.getTraceId();
    if (traceId != null) {
      item.pushBack("traceId", traceId);
    }

    IThrowableProxy iThrowableProxy = event.getThrowableProxy();
    if (iThrowableProxy != null) {
      String throwable = getExceptionInfo(iThrowableProxy);
      throwable += fullDump(event.getThrowableProxy().getStackTraceElementProxyArray());
      item.pushBack("throwable", throwable);
    }

    if (appName != "" && appName.length() > 0) {
      item.pushBack("appName", appName);
    }

    item.pushBack("host", IpUtil.getFirstLocalIpAddress());
    item.pushBack("hostName", IpUtil.getHostname());
    return item;
  }


  /**
   * 获取异常信息
   * @param iThrowableProxy
   * @return
   */
  private static String getExceptionInfo(IThrowableProxy iThrowableProxy) {
    String s = iThrowableProxy.getClassName();
    String message = iThrowableProxy.getMessage();
    return (message != null) ? (s + ": " + message) : s;
  }


  /**
   * 获取异常堆栈信息
   * @param stackTraceElementProxyArray
   * @return
   */
  private static String fullDump(StackTraceElementProxy[] stackTraceElementProxyArray) {
    StringBuilder builder = new StringBuilder();
    for (StackTraceElementProxy step : stackTraceElementProxyArray) {
      builder.append(CoreConstants.LINE_SEPARATOR);
      String string = step.toString();
      builder.append(CoreConstants.TAB).append(string);
      ThrowableProxyUtil.subjoinPackagingData(builder, step);
    }
    return builder.toString();
  }
}
