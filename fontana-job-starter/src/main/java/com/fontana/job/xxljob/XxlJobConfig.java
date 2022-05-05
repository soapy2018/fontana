package com.fontana.job.xxljob;

import com.fontana.base.constant.CommonConstants;
import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author cqf
 */
@Component
@ConditionalOnProperty(prefix = CommonConstants.XXLJOB_PREFIX, name = "enable", havingValue = "true")
@ConfigurationProperties(prefix = CommonConstants.XXLJOB_PREFIX)
@Slf4j
public class XxlJobConfig {

    private String adminAddresses;
    private String appName;
    private int port;
    private String ip;
    private String logPath;
    private int logRetentionDays;
    private String accessToken;

    @Bean(destroyMethod = "destroy")
    public XxlJobSpringExecutor xxlJobExecutor() {
        log.info(">>>>>>>>>>> xxl-job config init.");
        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
        xxlJobSpringExecutor.setAdminAddresses(adminAddresses);
        xxlJobSpringExecutor.setAppname(appName);
        xxlJobSpringExecutor.setIp(ip);
        xxlJobSpringExecutor.setPort(port);
        xxlJobSpringExecutor.setAccessToken(accessToken);
        xxlJobSpringExecutor.setLogPath(logPath);
        xxlJobSpringExecutor.setLogRetentionDays(logRetentionDays);
        return xxlJobSpringExecutor;
    }

}
