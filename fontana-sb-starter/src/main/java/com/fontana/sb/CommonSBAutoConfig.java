package com.fontana.sb;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

/**
 * @program: sb boot
 * @description: sb starter 的入口点
 * 包括所有提供的自配特性，先踢掉LoginUserFilter，还没想好怎么通用点
 * @author: cqf
 * @date: 2021-10-19 11:26
 **/
@Configuration
@ComponentScan(basePackages = {"com.fontana.sb"},
        excludeFilters = {@ComponentScan.Filter(type = FilterType.REGEX, pattern = "com.fontana.sb.filter.*")})
@Slf4j
public class CommonSBAutoConfig {

    public CommonSBAutoConfig() {
        log.info(">>>> SB START >>>> ");
    }

}


