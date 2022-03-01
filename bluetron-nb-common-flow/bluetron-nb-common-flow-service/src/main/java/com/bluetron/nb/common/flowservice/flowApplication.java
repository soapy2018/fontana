package com.bluetron.nb.common.flowservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * online服务启动类。
 *
 * @author cqf
 * @date 2020-08-08
 */
@SpringBootApplication
@MapperScan(value = {"com.bluetron.nb.common.flowservice.mapper"})
public class flowApplication {

	public static void main(String[] args) {
		SpringApplication.run(flowApplication.class, args);
	}
}
