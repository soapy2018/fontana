package com.fontana.log.producer.appender;

import com.fontana.log.producer.appender.handler.impl.RequestLogHandler;
import com.fontana.log.producer.producer.persistence.Constants;

/**
 * @author yegenchang
 * @description 系统日志
 * @date 2022/6/16 16:15
 */
public class RequestLogAppender extends BaseAppender {

  public RequestLogAppender() {
    super(new RequestLogHandler());
  }

  @Override
  public String getLogStore() {
    return Constants.REQUEST_LOG_STORE_NAME;
  }
}
