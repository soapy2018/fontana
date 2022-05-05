package com.fontana.onlineservice.config;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * common-online模块的自动配置引导类。
 *
 * @author cqf
 * @date 2021-06-06
 */
@Configuration
@EnableConfigurationProperties({OnlineProperties.class})
@ComponentScan("com.fontana.onlineservice.*")
@MapperScan("com.fontana.onlineservice.mapper")
@Slf4j
public class OnlineAutoConfig {
    public OnlineAutoConfig() {
        log.info(">>>> ONLINE START >>>> ");
    }
}
