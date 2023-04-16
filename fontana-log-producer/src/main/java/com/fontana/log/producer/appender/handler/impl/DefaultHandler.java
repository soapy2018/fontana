package com.fontana.log.producer.appender.handler.impl;

import ch.qos.logback.classic.spi.LoggingEvent;
import com.fontana.log.producer.appender.handler.ILogHandler;
import com.fontana.log.producer.producer.BaseLogItem;

/**
 * @author yegenchang
 * @Title: DefaultHandler
 * @Description:
 * @date 2021/3/5 23:47
 */
public class DefaultHandler implements ILogHandler {

    /**
     * 最大消息长度
     */
    private static final int MAX_MESSAGE_CONTENT = 5000;

    @Override
    public void handler(LoggingEvent event, BaseLogItem logItem) {
        String message = event.getFormattedMessage();
        if (message != null) {
            long l = message.length();
            if (l > MAX_MESSAGE_CONTENT) {
                message = message.substring(0, MAX_MESSAGE_CONTENT) + "……内容超过"
                    + MAX_MESSAGE_CONTENT + " 总长度: " + l;
            }
        }
        logItem.pushBack("message", message);
    }
}
