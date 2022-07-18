package com.fontana.log;

import com.fontana.log.apiLog.ApiLogProperties;
import com.fontana.log.auditLog.AuditLogProperties;
import com.fontana.log.solarLog.SolarLogProperties;
import com.fontana.log.traceLog.TraceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 日志自动配置
 *
 * @author cqf
 * @date 2021/8/13
 */
@EnableConfigurationProperties({AuditLogProperties.class, TraceProperties.class, ApiLogProperties.class, SolarLogProperties.class})
@ComponentScan
@Configuration
public class LogAutoConfigure {

}
