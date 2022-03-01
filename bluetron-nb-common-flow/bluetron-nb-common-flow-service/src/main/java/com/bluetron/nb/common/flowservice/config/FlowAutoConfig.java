package com.bluetron.nb.common.flowservice.config;

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
@ComponentScan("com.bluetron.nb.common.flowservice.*")
@MapperScan("com.bluetron.nb.common.flowservice.mapper")
@Slf4j
public class FlowAutoConfig {
    public FlowAutoConfig() {
        log.info(">>>> FLOW START >>>> ");
    }
}
