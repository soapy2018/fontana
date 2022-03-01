package com.bluetron.nb.common.log;

import com.bluetron.nb.common.log.apiLog.ApiLogProperties;
import com.bluetron.nb.common.log.auditLog.AuditLogProperties;
import com.bluetron.nb.common.log.traceLog.TraceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 日志自动配置
 *
 * @author cqf
 * @date 2021/8/13
 */
@EnableConfigurationProperties({AuditLogProperties.class, TraceProperties.class, ApiLogProperties.class})
@ComponentScan
@Configuration
public class LogAutoConfigure {

}
