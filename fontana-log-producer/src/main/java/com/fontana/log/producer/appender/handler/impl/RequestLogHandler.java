package com.fontana.log.producer.appender.handler.impl;

import ch.qos.logback.classic.spi.LoggingEvent;
import com.alibaba.fastjson.JSON;
import com.fontana.log.producer.appender.handler.ILogHandler;
import com.fontana.log.producer.producer.BaseLogItem;
import com.fontana.log.producer.producer.RequestLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

/**
 * @author yegenchang
 * @Title: requestHandler
 * @Description: 请求日志处理类
 * @date 2021/3/5 22:54
 */
public class RequestLogHandler implements ILogHandler {

    private Logger logger = LoggerFactory.getLogger(RequestLogHandler.class);

    /**
     * 最大消息长度
     */
    private static final int MAX_MESSAGE_CONTENT = 5000;

    /**
     * 忽略的字段，不进行存储
     */
    private static final List<String> ignoreField = Arrays.asList("serialVersionUID");

    /**
     * 处理消息
     *
     * @param event
     * @param logItem
     */
    @Override
    public void handler(LoggingEvent event, BaseLogItem logItem) {
        //提取消息
        String message = event.getFormattedMessage();
        try {
            logItem.mContents.removeIf((m) -> {
                return m.getKey().equals("level");
            });
            logItem.mContents.removeIf((m) -> {
                return m.getKey().equals("location");
            });
            RequestLog requestLog = JSON.parseObject(message, RequestLog.class);
            if (requestLog != null) {
                Class c = RequestLog.class;
                Field[] fields = c.getDeclaredFields();
                for (int i = 0; i < fields.length; i++) {
                    Field f = fields[i];
                    f.setAccessible(true);
                    String key = f.getName();
                    if (ignoreField.contains(key)) {
                        continue;
                    }
                    String value = String.valueOf(f.get(requestLog));
                    if (value != null) {
                        long l = value.length();
                        if (l > MAX_MESSAGE_CONTENT) {
                            value = value.substring(0, MAX_MESSAGE_CONTENT) + "……内容超过"
                                + MAX_MESSAGE_CONTENT + " 总长度: " + l;
                        }
                    }
                    logItem.pushBack(key, value);
                }
            }
        } catch (Exception ex) {
            logger.error("记录接口日志异常", ex);
        }
    }
}
