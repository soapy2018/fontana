package com.fontana.log.producer.appender.handler;

import ch.qos.logback.classic.spi.LoggingEvent;
import com.fontana.log.producer.producer.BaseLogItem;

/**
 * @author yegenchang
 * @description 日志前置处理
 * @date 2022/6/14 15:24
 */
public interface ILogHandler {


  /**
   *
   * @param event
   * @param logItem
   */
  void handler(LoggingEvent event, BaseLogItem logItem);
}
