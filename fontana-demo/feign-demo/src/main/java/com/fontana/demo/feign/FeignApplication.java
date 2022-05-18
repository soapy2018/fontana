package com.fontana.demo.feign;

import com.fontana.cloud.feign.EnableFeignInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * Upms服务启动类。
 *
 * @author cqf
 * @date 2020-08-08
 */

@EnableFeignClients(basePackages = "com.fontana.upmsapi")
@SpringBootApplication
@EnableFeignInterceptor
//要将fallbackFactory实现类初始化成bean
@ComponentScan({"com.fontana.demo.feign", "com.fontana.upmsapi"})
public class FeignApplication {

	public static void main(String[] args) {
		SpringApplication.run(FeignApplication.class, args);
	}
}
