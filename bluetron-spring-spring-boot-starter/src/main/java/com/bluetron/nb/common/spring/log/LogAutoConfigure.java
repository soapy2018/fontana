package com.bluetron.nb.common.spring.log;

import com.zaxxer.hikari.HikariConfig;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 日志自动配置
 *
 * @author bcloud
 * @date 2019/8/13
 */
//@EnableConfigurationProperties({AuditLogProperties.class})
@Configuration
@Import({LoggerAuditServiceImpl.class,DbAuditServiceImpl.class,AuditLogAspect.class})
public class LogAutoConfigure {
    /**
     * 日志数据库配置
     */
//    @Configuration
//    @ConditionalOnClass(HikariConfig.class)
//    @EnableConfigurationProperties(LogDbProperties.class)
//    public static class LogDbAutoConfigure {}
}
