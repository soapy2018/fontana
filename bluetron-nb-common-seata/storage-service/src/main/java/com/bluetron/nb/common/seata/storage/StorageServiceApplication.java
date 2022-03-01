package com.bluetron.nb.common.seata.storage;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 库存
 *
 * @author cqf
 * @date 2019/9/14
 */
@EnableDiscoveryClient
@EnableFeignClients
@MapperScan({"com.bluetron.nb.common.seata.storage.mapper"})
@SpringBootApplication//(exclude = {DataSourceAutoConfiguration.class})
public class StorageServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(StorageServiceApplication.class, args);
    }
}
