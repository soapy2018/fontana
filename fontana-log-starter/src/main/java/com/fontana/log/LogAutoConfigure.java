package com.fontana.log;

import com.fontana.log.apilog.ApiLogProperties;
import com.fontana.log.auditlog.AuditLogProperties;
import com.fontana.log.requestlog.config.RequestLogProperties;
import com.fontana.log.tracelog.TraceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 日志自动配置
 *
 * @author cqf
 * @date 2021/8/13
 */
@EnableConfigurationProperties({
        AuditLogProperties.class,
        TraceProperties.class,
        ApiLogProperties.class,
        RequestLogProperties.class})
@ComponentScan //总入口，模块里其他bean靠它拉起
@Configuration
public class LogAutoConfigure {

}
