package com.fontana.rabbitmq;

import com.fontana.rabbitmq.base.BaseMap;
import com.fontana.rabbitmq.client.RabbitMqClient;
import com.fontana.util.tools.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @className: RabbitMqTest
 * @Description: TODO
 * @version: v1.0.0
 * @author: cqf
 * @date: 2022/4/24 15:14
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SpringBootApplication(scanBasePackages = "com.fontana.rabbitmq")
@Slf4j
public class RabbitMqTest {

    @Autowired
    RabbitMqClient rabbitMqClient;

    @Test
    public void testRabbitMq() throws InterruptedException {
        //rabbitmq消息队列测试
        BaseMap map = new BaseMap();
        map.put("orderId", RandomUtil.randomNumbers(10));

        rabbitMqClient.sendMessage("test_place_order", map);
        rabbitMqClient.sendMessage("test_place_order_time", map, 10);

        //rabbitmq消息总线测试
        BaseMap params = new BaseMap();
        params.put("orderId", "123456");
        rabbitMqClient.publishEvent("demoBusEvent", params);
        Thread.sleep(1000);

    }
}


