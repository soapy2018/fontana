package com.fontana.datafilter.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * common-datafilter模块的自动配置引导类。
 *
 * @author cqf
 * @date 2021-06-06
 */
@Configuration
@EnableConfigurationProperties({DataFilterProperties.class})
@ComponentScan("com.fontana.datafilter.*")
@Slf4j
public class DataFilterAutoConfig {
    public DataFilterAutoConfig() {
        log.info(">>>> DATAFILTER START >>>> ");
    }
}
