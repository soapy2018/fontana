package com.fontana.rabbitmq.event;

import com.fontana.rabbitmq.base.BaseMap;
import lombok.Data;

import java.io.Serializable;

/**
 * 远程事件数据对象
 */
@Data
public class EventObj implements Serializable {
    /**
     * 数据对象
     */
    private BaseMap baseMap;
    /**
     * 自定义业务模块消息处理器beanName
     */
    private String handlerName;
}
