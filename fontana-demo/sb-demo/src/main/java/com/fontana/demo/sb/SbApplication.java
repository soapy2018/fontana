package com.fontana.demo.sb;

import com.fontana.util.lang.StringUtil;
import com.fontana.util.request.IpUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.spring.starter.RedissonAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

/**
 * sb服务启动类。
 *
 * @author cqf
 * @date 2020-08-08
 */

//@SpringBootApplication(exclude = {RedissonAutoConfiguration.class, RedisAutoConfiguration.class})
//@SpringBootApplication(exclude = {RedisAutoConfiguration.class})
@SpringBootApplication
@Slf4j
public class SbApplication {

	public static void main(String[] args) {

		ConfigurableApplicationContext application = SpringApplication.run(SbApplication.class, args);
		Environment env = application.getEnvironment();
		String ip = IpUtil.getFirstLocalIpAddress();
		String port = env.getProperty("server.port");
		String path = StringUtil.defaultString(env.getProperty("server.servlet.context-path"));
		log.info("\n----------------------------------------------------------\n\t" +
				"Application Fisp is running! Access URLs:\n\t" +
				"Local: \t\thttp://localhost:" + port + path + "/\n\t" +
				"External: \thttp://" + ip + ":" + port + path + "/\n\t" +
				"Swagger文档: \thttp://" + ip + ":" + port + path + "/doc.html\n" +
				"----------------------------------------------------------");
	}
}
