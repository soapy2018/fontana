package com.fontana.flowservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * online服务启动类。
 *
 * @author cqf
 * @date 2020-08-08
 */
@SpringBootApplication
//要使用online的功能，要扫描online的包
@ComponentScan("com.fontana.onlineservice")
public class flowApplication {

	public static void main(String[] args) {
		SpringApplication.run(flowApplication.class, args);
	}
}
