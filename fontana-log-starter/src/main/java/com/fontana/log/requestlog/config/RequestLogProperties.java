package com.fontana.log.requestlog.config;

import com.fontana.base.constant.CommonConstants;
import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * @program: log
 * @description: requestlog相关开放的参数
 * @author: cqf
 * @date: 2022-7-19 13:12
 **/
@ConfigurationProperties(prefix = CommonConstants.REQUESTLOG_PREFIX)
@Data
@ToString
@RefreshScope
public class RequestLogProperties {

    /**
     * requestlog是否开启
     */
    private Boolean enable = false;

    /**
     * 记录中是否显示来源IP
     */
    private Boolean showIP = false;

    /**
     * 记录中是否显示入参
     */
    private Boolean showArgs = true;

    /**
     * 记录中是否显示请求头
     */
    private Boolean showHead = false;

    /**
     * 记录中是否显示response
     */
    private Boolean showRes = false;

}
