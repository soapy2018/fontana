package com.fontana.log.producer.appender.handler.impl;

import ch.qos.logback.classic.spi.LoggingEvent;
import com.fontana.log.producer.appender.handler.ILogHandler;
import com.fontana.log.producer.producer.BaseLogItem;

/**
 * @author yegenchang
 * @Title: requestHandler
 * @Description: 请求日志处理类
 * @date 2021/3/5 22:54
 */
public class OprLogHandler implements ILogHandler {
    /**
     * 处理消息
     * @param event
     * @param logItem
     */
    @Override
    public void handler(LoggingEvent event, BaseLogItem logItem) {
        //TODO 操作日志
    }
}
