package com.fontana.oss;

import com.fontana.util.tools.SpringContextHolder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * Upms服务启动类。
 *
 * @author cqf
 * @date 2020-08-08
 */
@SpringBootApplication
public class OssApplication {

	public static void main(String[] args) {

		SpringApplication.run(OssApplication.class, args);
//		SpringContextHolder.getBean("S3Template");
//		SpringContextHolder.getBean("OssUpDownloader");
		System.out.println("启动成功-------");

	}
}
