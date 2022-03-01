package com.bluetron.nb.common.seata.account;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 账号
 *
 * @author cqf
 * @date 2019/9/14
 */
@EnableDiscoveryClient
@MapperScan({"com.bluetron.nb.common.seata.account.mapper"})
@SpringBootApplication
public class AccountServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(AccountServiceApplication.class, args);
    }
}
