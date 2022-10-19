package com.fontana.xxljob;

import com.fontana.base.constant.CommonConstants;
import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author cqf
 */
@Configuration
@Data
@ConditionalOnProperty(prefix = CommonConstants.XXLJOB_PREFIX, name = "enabled", havingValue = "true")
@ConfigurationProperties(prefix = CommonConstants.XXLJOB_PREFIX)
@ComponentScan
@Slf4j
public class XxlJobConfig {

    private String adminAddresses;
    private String appName;
    private String address;
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
        xxlJobSpringExecutor.setAddress(address);
        xxlJobSpringExecutor.setIp(ip);
        xxlJobSpringExecutor.setPort(port);
        xxlJobSpringExecutor.setAccessToken(accessToken);
        xxlJobSpringExecutor.setLogPath(logPath);
        xxlJobSpringExecutor.setLogRetentionDays(logRetentionDays);
        return xxlJobSpringExecutor;
    }

}
