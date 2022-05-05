package com.fontana.rabbitmq.event;

/**
 * 业务模块消息处理器接口
 */
public interface MyBusEventHandler {
    void onMessage(EventObj map);
}
