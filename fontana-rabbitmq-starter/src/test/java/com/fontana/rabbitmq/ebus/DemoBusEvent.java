package com.fontana.rabbitmq.ebus;

import cn.hutool.core.util.ObjectUtil;
import com.fontana.rabbitmq.base.BaseMap;
import com.fontana.rabbitmq.event.EventObj;
import com.fontana.rabbitmq.event.MyBusEventHandler;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

/**
 * 消息处理器【发布订阅】
 */
@Slf4j
@Component("demoBusEvent")
public class DemoBusEvent implements MyBusEventHandler {

    public void onMessage(EventObj obj) {
        if (ObjectUtil.isNotEmpty(obj)) {
            BaseMap baseMap = obj.getBaseMap();
            String orderId = baseMap.get("orderId");
            log.info("业务处理----订单ID:" + orderId);
        }
    }
}
