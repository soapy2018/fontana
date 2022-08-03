package com.fontana.log.requestlog.config;

import com.fontana.base.constant.CommonConstants;
import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @program: log
 * @description: requestlog相关开放的参数
 * @author: cqf
 * @date: 2022-7-19 13:12
 **/
@ConfigurationProperties(prefix = CommonConstants.REQUESTLOG_PREFIX)
@Data
@ToString
public class RequestLogProperties {

    /**
     * requestlog是否开启
     */
    private Boolean enable = false;

}
