package com.fontana.flowservice.config;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * common-flow模块的自动配置引导类。
 *
 * @author cqf
 * @date 2021-06-06
 */
@Configuration
@EnableConfigurationProperties({FlowProperties.class})
//若是被别人引入的，需要借助这个去扫描自己的包，若是自己启动的可以删掉
@ComponentScan("com.fontana.flowservice.*")
@MapperScan("com.fontana.flowservice.mapper")
@Slf4j
public class FlowAutoConfig {
    public FlowAutoConfig() {
        log.info(">>>> FLOW START >>>> ");
    }
}
