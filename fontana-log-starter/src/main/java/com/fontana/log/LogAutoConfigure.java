package com.fontana.log;

import com.fontana.base.constant.CommonConstants;
import com.fontana.log.apilog.ApiLogAdvice;
import com.fontana.log.apilog.ApiLogProperties;
import com.fontana.log.auditlog.AuditLogProperties;
import com.fontana.log.requestlog.config.RequestLogProperties;
import com.fontana.log.tracelog.TraceProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 日志自动配置
 *
 * @author cqf
 * @date 2021/8/13
 */
@Slf4j
@EnableConfigurationProperties({
        AuditLogProperties.class,
        TraceProperties.class,
        ApiLogProperties.class,
        RequestLogProperties.class})
//总入口，模块里其他bean靠它拉起
@ComponentScan
@Configuration
public class LogAutoConfigure {

    @Autowired ApiLogProperties apiLogProperties;
    public LogAutoConfigure(){
        log.info(">>>> LOG START >>>> ");
    }

    /**
     * api日志切面类
     * 支持自定义切面表达式
     * @return AspectJExpressionPointcutAdvisor
     */
    @Bean
    @ConditionalOnProperty(prefix = CommonConstants.APILOG_PREFIX, name = "enabled", havingValue = "true")
    public AspectJExpressionPointcutAdvisor apiLogAdvisor() {
        AspectJExpressionPointcutAdvisor advisor = new AspectJExpressionPointcutAdvisor();
        advisor.setAdvice(new ApiLogAdvice(apiLogProperties));
        advisor.setExpression(apiLogProperties.getPointcut());
        return advisor;
    }

}

