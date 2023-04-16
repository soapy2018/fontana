package com.fontana.rabbitmq.event;

import lombok.Data;
import org.springframework.cloud.bus.event.RemoteApplicationEvent;

/**
 * 自定义网关刷新远程事件
 *
 * @author : cqf
 * @date :2020-11-10
 */
@Data
public class MyRemoteApplicationEvent extends RemoteApplicationEvent {

    private MyRemoteApplicationEvent() {
    }

    private EventObj eventObj;

    public MyRemoteApplicationEvent(EventObj source, String originService, String destinationService) {
        super(source, originService, destinationService);
        this.eventObj = source;
    }

    public MyRemoteApplicationEvent(EventObj source, String originService) {
        super(source, originService, null);
        this.eventObj = source;
    }
}
