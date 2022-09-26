package com.fontana.upmsservice;

import com.fontana.cloud.feign.EnableFeignInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * Upms服务启动类。
 *
 * @author cqf
 * @date 2020-08-08
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableFeignInterceptor
public class UpmsApplication {

	public static void main(String[] args) {

		SpringApplication.run(UpmsApplication.class, args);

	}
}
