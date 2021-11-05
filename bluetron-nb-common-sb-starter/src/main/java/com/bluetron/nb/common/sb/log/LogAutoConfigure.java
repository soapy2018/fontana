package com.bluetron.nb.common.sb.log;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 日志自动配置
 *
 * @author cqf
 * @date 2021/8/13
 */
//@EnableConfigurationProperties({AuditLogProperties.class})
@Configuration
@Import({LoggerAuditServiceImpl.class,AuditLogAspect.class})
public class LogAutoConfigure {
    /**
     * 日志数据库配置
     */
//    @Configuration
//    @ConditionalOnClass(HikariConfig.class)
//    @EnableConfigurationProperties(LogDbProperties.class)
//    public static class LogDbAutoConfigure {}
}
