package com.bluetron.nb.common.onlineservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * online服务启动类。
 *
 * @author cqf
 * @date 2020-08-08
 */
@SpringBootApplication
@MapperScan(value = {"com.bluetron.nb.common.onlineservice.mapper"})
public class onlineApplication {

	public static void main(String[] args) {
		SpringApplication.run(onlineApplication.class, args);
	}
}
