package com.fontana.rabbitmq.event;

import com.fontana.util.lang.ObjectUtil;
import com.fontana.util.tools.SpringContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * 监听远程事件,并分发消息到业务模块消息处理器
 */
@Component
@Slf4j
public class BaseApplicationEvent implements ApplicationListener<MyRemoteApplicationEvent> {

    @Override
    public void onApplicationEvent(MyRemoteApplicationEvent myRemoteApplicationEvent) {
        EventObj eventObj = myRemoteApplicationEvent.getEventObj();
        if (ObjectUtil.isNotEmpty(eventObj)) {
            //获取业务模块消息处理器
            MyBusEventHandler busEventHandler = SpringContextHolder.getBean(eventObj.getHandlerName(), MyBusEventHandler.class);
            if (ObjectUtil.isNotEmpty(busEventHandler)) {
                log.info("busEventHandler: {}", busEventHandler);
                //通知业务模块
                busEventHandler.onMessage(eventObj);
            }
        }
    }

}
