package com.fontana.rabbitmq.core;

import com.fontana.rabbitmq.listenter.MqListener;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 *
 * @author cqf
 */
@Slf4j
public class BaseRabbiMqHandler<T> {


    public void onMessage(T t, Long deliveryTag, Channel channel, MqListener<T> mqListener) {
        try {

            mqListener.handler(t, channel);
            channel.basicAck(deliveryTag, false);

        } catch (Exception e) {
            log.info("接收消息失败,重新放回队列");
            try {
                /**
                 * deliveryTag:该消息的index
                 * multiple：是否批量.true:将一次性拒绝所有小于deliveryTag的消息。
                 * requeue：被拒绝的是否重新入队列
                 */
                channel.basicNack(deliveryTag, false, true);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }
}
