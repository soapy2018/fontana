package com.fontana.log.producer.appender;

import com.fontana.log.producer.appender.handler.impl.DefaultHandler;

import static com.fontana.log.producer.producer.persistence.Constants.APP_LOG_STORE_NAME;

/**
 * @author yegenchang
 * @description 系统日志
 * @date 2022/6/16 16:15
 */
public class AppLogAppender extends BaseAppender {

  public AppLogAppender() {
    super(new DefaultHandler());
  }

  @Override
  public String getLogStore() {
    return APP_LOG_STORE_NAME;
  }
}
