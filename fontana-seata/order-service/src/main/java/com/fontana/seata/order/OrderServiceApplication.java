package com.fontana.seata.order;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 订单
 *
 * @author cqf
 * @date 2019/9/14
 */
@EnableDiscoveryClient
@EnableFeignClients
@MapperScan({"com.fontana.seata.order.mapper"})
@SpringBootApplication//(exclude = {DataSourceAutoConfiguration.class})
public class OrderServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }
}
